package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.*;
import com.shop.logic.domain.*;
import com.shop.logic.util.JsonUtil;
import com.shop.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.filter.OrderedRequestContextFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    private AddrDao addrDao;
    @Autowired
    private GoodsDao goodsDao;
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

    /**
     * 返回给用户的信息有  订单地址信息、商品信息、下单时间、订单编号、商品总额
     * @param id
     * @return
     */
    @RequestMapping(value = "getById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id获取订单信息", notes = "根据Id获取订单信息")
    public String getById(@RequestParam String id){
        try {
            Order order = orderDao.findById(id);
            Address address = addrDao.findById(order.getAddressId());
            List<OrderGoods> ogs = orderGoodsDao.findAllByOrderId(id);
            LinkedList<Object> goodss = new LinkedList<>();
            double totalPrice = 0;
            int totalNumber = 0;
            for (OrderGoods og:ogs){
                Goods goods = goodsDao.findById(og.getGoodsId());
                JSONObject goodsJson = JSONObject.fromObject(goods);
                goodsJson.put("number",og.getNumber());
                goodss.add(goodsJson);
                totalPrice += og.getNumber()*goods.getPrice();
                totalNumber +=og.getNumber();
            }

            JSONObject orderJson = JSONObject.fromObject(order);
            JSONObject addressJson = JSONObject.fromObject(address);
            JSONArray goodsArray = JSONArray.fromObject(goodss);
            JSONObject resultJson = new JSONObject();
            resultJson.putAll(orderJson);
            resultJson.putAll(addressJson);
            resultJson.put("list",goodsArray);
            resultJson.put("totalPrice",totalPrice);
            resultJson.put("totalNumber",totalNumber);
            return resultJson.toString();
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取订单失败");
        }
    }

    /**
     * 只给返回每条订单的  商品名称，商品数量，商品总价
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getByPage", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户所有订单信息", notes = "获取用户所有订单信息")
    public String getByPage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize){
        try {
            Pageable pageable = new PageRequest(pageNum-1,pageSize,new Sort(Sort.Direction.DESC,"createTime"));
            Page<Order> orders = orderDao.findAllByUserCode(BasicFilter.user_id,pageable);
            LinkedList<String> orderIds = new LinkedList<>();
            for (Order order:orders){
                orderIds.add(order.getId());
            }
            LinkedList<Object> list = new LinkedList<>();
            for (String id:orderIds){
                List<OrderGoods> ogs = orderGoodsDao.findAllByOrderId(id);
                LinkedList<Goods> goodss = new LinkedList<>();
                int totalNumber = 0;
                for (OrderGoods og:ogs){
                    Goods goods = goodsDao.findById(og.getGoodsId());
                    goodss.add(goods);
                    totalNumber += og.getNumber();
                }
                JSONArray array = JSONArray.fromObject(goodss);
                JSONObject json = JSONObject.fromObject(array);
                json.put("orderid",id);
                json.put("totalNumber",totalNumber);
                list.add(json);
            }

            long total = orderDao.countAllByUserCode(BasicFilter.user_id);
            long totalPage = total/pageSize==0 ? total/pageSize:total/pageSize+1;
            if (totalPage==0){
                totalPage=1;
            }
            return TableUtil.createTableDate(list,total,pageNum,totalPage,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取失败");
        }
    }
}
