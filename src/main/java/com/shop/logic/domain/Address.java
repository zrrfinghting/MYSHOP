package com.shop.logic.domain;

import com.shop.logic.util.DateUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author:Dreram
 * @Description: 用户地址(用于订单的收获地址)
 * @Date:created in :23:28 2018/4/6
 * @Modified By:
 */
@Entity
@Table(name = "sys_address")
public class Address {
    @Id
    @Column(name = "ID")
    private String id;//地址Id

    @Column(name = "ADDRESS")
    private String address;//地址

    @Column(name = "TELL")
    private String tell;//收货人电话

    @Column(name = "RECEIVENAME")
    private String receiveName;//收货人姓名

    @Column(name = "DEFAULTADDR")
    private int defaultAddr;//1--默认收获地址

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public int getDefaultAddr() {
        return defaultAddr;
    }

    public void setDefaultAddr(int defaultAddr) {
        this.defaultAddr = defaultAddr;
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
