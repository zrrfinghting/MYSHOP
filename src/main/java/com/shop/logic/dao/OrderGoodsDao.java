package com.shop.logic.dao;/**
 * Created by Admin on 2018/4/10.
 */

import com.shop.logic.domain.OrderGoods;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * 订单商品中间表
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/10
 */
public interface OrderGoodsDao extends CrudRepository<OrderGoods,Integer>{

    /**
     * 获取订单商品中间表对象  从而得到订单对应的商品
     * @param orderId
     * @return
     */
    public List<OrderGoods> findAllByOrderId(String orderId);

}
