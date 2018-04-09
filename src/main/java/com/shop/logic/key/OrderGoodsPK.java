package com.shop.logic.key;

import java.io.Serializable;

/**
 * 订单和商品复合主键
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */

public class OrderGoodsPK implements Serializable{
    private String orderId;//订单id
    private String goodsId;//商品id
}
