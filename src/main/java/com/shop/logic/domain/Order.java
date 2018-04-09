package com.shop.logic.domain;

import com.shop.logic.util.DateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.crypto.Data;
import java.util.Date;

/**
 * 订单实体
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */
@Entity
@Table(name = "sys_order")
public class Order {

    // 操作类型常量
    public static final int STOP = 0;//停用
    public static final int START =1;//启用
    public static final int ADD = 2;//新增
    public static final int UPDATE = 3;//修改
    public static final int DELETE = 4;//删除
    public static final int LOGIN = 5;//登录
    public static final int FILEUPlOAD = 6;//文件上传

    @Id
    @Column(name = "ID")
    private String id;//订单id

    @Column(name = "DELIVERYTIME")
    private Date deliveryTime;//收货时间

    @Column(name = "ORDERDESC")
    private String orderDesc;//订单备注

    @Column(name = "ADDRESSID")
    private String addressId;//订单收货信息

    @Column(name = "USERCODE")
    private String userCode;//订单对应的用户

    @Column(name = "STATE")
    private int state;//订单状态

    @Column(name = "CREATETIME")
    private Date createTime;//创建时间

    @Column(name = "UPDATETIME")
    private Date updateTime;//修改时间



    //setter 和getter方法

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
