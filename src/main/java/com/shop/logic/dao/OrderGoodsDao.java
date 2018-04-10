package com.shop.logic.dao;/**
 * Created by Admin on 2018/4/10.
 */

import com.shop.logic.domain.OrderGoods;
import org.springframework.data.repository.CrudRepository;

/**
 * 订单商品中间表
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/10
 */
public interface OrderGoodsDao extends CrudRepository<OrderGoods,Integer>{

}
