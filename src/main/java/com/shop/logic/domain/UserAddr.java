package com.shop.logic.domain;

import com.shop.logic.key.UserAddrPK;
import com.shop.logic.util.DateUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author:Dreram
 * @Description: 用户和地址的中间表
 * @Date:created in :15:43 2018/4/7
 * @Modified By:
 */
@Entity
@IdClass(UserAddrPK.class)
@Table(name = "sys_user_addr")
public class UserAddr {
    @Id
    @Column(name = "USERCODE")
    private String userCode;//用户账号

    @Id
    @Column(name = "ADDRID")
    private String addrId;//地址信息id

    @Column(name = "STATE")
    private int state;//0--不是默认收获地址，1---默认收获地址

    @Column(name = "CREATETIME")
    private Date createTime;//创建时间

    @Column(name = "UPDATETIME")
    private Date updateTime;//修改时间


    //setter 和getter方法

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAddrId() {
        return addrId;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
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
