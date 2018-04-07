package com.shop.logic.dao;

import com.shop.logic.domain.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

/**
 * @Author:Dreram
 * @Description: 地址操作接口
 * @Date:created in :23:57 2018/4/6
 * @Modified By:
 */
public interface AddrDao extends CrudRepository<Address,Integer>{
    /**
     * 通过地址id 获取地址
     * @param addrId
     * @return Address对象
     */
    public Address findById(String addrId);

    /**
     * 通过地址id删除地址信息
     * @param addrId
     */
    @Modifying
    @Transactional
    public void deleteById(String addrId);

}
