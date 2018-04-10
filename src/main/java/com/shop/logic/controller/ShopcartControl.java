package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.GoodsDao;
import com.shop.logic.dao.LogDao;
import com.shop.logic.dao.ShopCartDao;
import com.shop.logic.domain.Goods;
import com.shop.logic.domain.Log;
import com.shop.logic.domain.ShopCart;
import com.shop.logic.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 购物车对外类
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/10
 */
@RestController
@RequestMapping("/shopcart")
public class ShopcartControl {
    @Autowired
    private ShopCartDao shopCartDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private LogDao logDao;

    /**
     *添加商品到购物车不减少商品库存数量（只有在提交结算生成订单时才去做库存数量减少）
     * @param shopCart
     * @return
     */
    @RequestMapping(value = "addToCart", method = RequestMethod.POST)
    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车")
    public String add(@RequestBody ShopCart shopCart){
        try {
            //判断不能大于库存
            Goods goods = goodsDao.findById(shopCart.getGoodsId());
            if (goods.getNumber()<shopCart.getNumber()){
                return JsonUtil.returnStr(JsonUtil.FAIL,"库存没那么多货了,请刷新下页面");
            }
            shopCart.setId(String.valueOf(System.currentTimeMillis()));
            shopCart.setUserCode(BasicFilter.user_id);
            shopCart.setUpdateTime(new Date());
            shopCart.setCreateTime(new Date());
            shopCartDao.save(shopCart);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.ADD,"添加商品："+shopCart.getGoodsId()+"数量："+shopCart.getNumber()+"",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"添加失败");
        }
    }

    /**
     *可以在购物车里修改商品的数量（暂时不支持修改商品信息后期再扩展）
     * @param id
     * @param number
     * @return
     */
    @RequestMapping(value = "editCart", method = RequestMethod.POST)
    @ApiOperation(value = "修改购物车", notes = "修改购物车")
    public String editCart(@RequestParam String id,int number){
        try {
            ShopCart shopCart = shopCartDao.findById(id);
            shopCart.setNumber(number);
            shopCart.setUpdateTime(new Date());
            shopCartDao.save(shopCart);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.UPDATE,"修改购物车商品数量："+shopCart.getNumber()+"to"+number+"",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改购物车失败");
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除购物车商品信息", notes = "删除购物车商品信息")
    public String deleteById(@RequestParam String id){
        try {
            shopCartDao.deleteById(id);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.DELETE,"删除购物车里的商品",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"删除失败");
        }
    }
}
