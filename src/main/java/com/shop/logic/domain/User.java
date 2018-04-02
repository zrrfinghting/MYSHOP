package com.shop.logic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户实体
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/2
 */
@Entity
@Table(name = "sys_user")
public class User {
    @Id
    @Column(name = "USERCODE")
    private String userCode;//用户账号，使用手机号码

    @Column(name = "USERNAME")
    private String userName;//用户名称

    @Column(name = "ADDRESS")
    private String address;//用户地址

    @Column(name = "STATE")
    private String state;//用户状态0--未审核，1--审核通过，2--审核不通过

    @Column(name = "USERDESC")
    private String userDesc;//用户备注

    @Column(name = "CREATETIME")
    private Date createTime;//创建时间

    @Column(name = "UPDATETIME")
    private Date updateTime;//修改时间

    //setter和getter方法


    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
