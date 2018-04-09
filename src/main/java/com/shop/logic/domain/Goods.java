package com.shop.logic.domain;

import com.shop.logic.util.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 商品实体
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */
@Entity
@Table(name = "sys_goods")
public class Goods {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "GOODSNAME")
    private String goodsName;//名称

    @Column(name = "CATAGORYID")
    private String catagoryId;//商品的种类

    @Column(name = "PRICE")
    private double price;//价格

    @Column(name = "NUMBER")
    private int number;//数量(库存)

    @Column(name = "GOODSIMG")
    private String goodsImg;//商品图片

    @Column(name = "GOODSDESC")
    private String goodsDesc;//商品备注

    @Column(name = "STATE")
    private int state;//0--上架，1--下架，

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCatagoryId() {
        return catagoryId;
    }

    public void setCatagoryId(String catagoryId) {
        this.catagoryId = catagoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
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
