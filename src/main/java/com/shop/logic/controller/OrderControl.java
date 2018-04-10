package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.LogDao;
import com.shop.logic.dao.OrderDao;
import com.shop.logic.dao.OrderGoodsDao;
import com.shop.logic.dao.ShopCartDao;
import com.shop.logic.domain.Order;
import com.shop.logic.domain.OrderGoods;
import com.shop.logic.domain.ShopCart;
import com.shop.logic.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.filter.OrderedRequestContextFilter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;

/**
 * 订单对外服务类
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/10
 */

@RestController
@RequestMapping("/order")
public class OrderControl {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ShopCartDao shopCartDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private LogDao logDao;


    /**
     * 一、保存order，
     * 二、根据传入的购物车id去获取购物车中的商品id和number 将其保存到订单商品中间表中order_goods，
     * 三、清掉购物车中的商品
     *
     * 结算时提交购物车的id列表
     * 使用{"carIds"："xxxxxx,xxxxx,xxxxx"}   英文逗号隔开
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    @ApiOperation(value = "新增订单", notes = "新增订单")
    @Transactional
    public String addOrder(@RequestBody Object json){
        try {
            //保存order实体的信息
            Order order = new Order();
            order = (Order) JsonUtil.jsonToBean(order,json);
            String orderId = String.valueOf(System.currentTimeMillis());
            order.setId(orderId);
            order.setUserCode(BasicFilter.user_id);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderDao.save(order);

            //获取提交的购物车里的id，并根据id得到购物车包含的商品id和数量，然后将其保存到 order_goods中间表中
            String [] ids =JsonUtil.getString("carIds",json).split(",");
            LinkedList<ShopCart> carts = new LinkedList<>();
            LinkedList<OrderGoods> ogs = new LinkedList<>();
            for (String id:ids){
                ShopCart cart = shopCartDao.findById(id);
                OrderGoods og = new OrderGoods();
                og.setOrderId(orderId);
                og.setGoodsId(cart.getGoodsId());
                og.setNumber(cart.getNumber());
                og.setCreateTime(new Date());
                og.setUpdateTime(new Date());
                ogs.add(og);
                carts.add(cart);
            }
            orderGoodsDao.save(ogs);
            shopCartDao.delete(carts);//结算后清空购物车中结算的商品
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"新增成功");
        }catch (Exception e){
            e.printStackTrace();
            //因为sping 默认trycatch是不进行事务回滚的，可以在手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtil.returnStr(JsonUtil.FAIL,"新增失败");
        }
    }

    /**
     * 已经打印的订单就不能修改了，只能联系商家
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "editOrder", method = RequestMethod.POST)
    @ApiOperation(value = "修改订单", notes = "修改订单")
    public String editOrder(@RequestBody Object json){
        try {
             String orderId = JsonUtil.getString("id",json);
             int state = Integer.parseInt(JsonUtil.getString("state",json));
             if (state==Order.PRINT)
                 return JsonUtil.returnStr(JsonUtil.FAIL,"订单已经打印请联系商家");
             Order order = orderDao.findById(orderId);
             order = (Order) JsonUtil.jsonToBean(order,json);
             order.setCreateTime(new Date());
             order.setUpdateTime(new Date());
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"修改失败");
        }
    }

}
