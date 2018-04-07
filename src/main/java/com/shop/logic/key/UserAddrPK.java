package com.shop.logic.key;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author:Dreram
 * @Description: 用户--地址   复合主键
 * @Date:created in :15:38 2018/4/7
 * @Modified By:
 */
public class UserAddrPK implements Serializable{
    private String userCode;//用户表id
    private String addrId;//地址表id

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
}
