package com.shop.logic.domain;

import com.shop.logic.util.DateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 商品类别实体
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/8
 */

@Entity
@Table(name = "sys_catagory")
public class Catagory {
    @Id
    @Column(name = "ID")
    private String id;//分类ID

    @Column(name = "CATAGORYNAME")//分类名称
    private String catagoryName;

    @Column(name = "PARENTID")//父分类ID
    private String parentId;

    @Column(name = "CATAGORYDESC")//分类备注
    private String catagoryDesc;

    @Column(name = "CREATETIME")
    private Date createTime;//创建时间

    @Column(name = "UPDATETIME")
    private Date updateTime;//修改时间

    //setter方法和getter方法


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatagoryName() {
        return catagoryName;
    }

    public void setCatagoryName(String catagoryName) {
        this.catagoryName = catagoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCatagoryDesc() {
        return catagoryDesc;
    }

    public void setCatagoryDesc(String catagoryDesc) {
        this.catagoryDesc = catagoryDesc;
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
