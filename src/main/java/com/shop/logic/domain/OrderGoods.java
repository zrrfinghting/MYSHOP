package com.shop.logic.domain;

import com.shop.logic.key.OrderGoodsPK;
import com.shop.logic.util.DateUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单-商品实体
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */
@Entity
@IdClass(OrderGoodsPK.class)
@Table(name = "sys_order_goods")
public class OrderGoods {

    @Id
    @Column(name = "ORDERID")
    private String orderId;//订单id

    @Id
    @Column(name = "GOODSID")
    private String goodsId;//商品id

    @Column(name = "NUMBER")
    private int number;//订单中的商品数量

    @Column(name = "CREATETIME")
    private Date createTime;//创建时间

    @Column(name = "UPDATETIME")
    private Date updateTime;//修改时间


    //setter 和getter方法
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public String getCreateTime() {
        return DateUtil.formatDate(DateUtil.FORMAT2,createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return DateUtil.formatDate(DateUtil.FORMAT2,updateTime);
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

