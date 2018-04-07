package com.shop.logic.dao;

import com.shop.logic.domain.UserAddr;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author:Dreram
 * @Description: 用户地址中间表操作接口
 * @Date:created in :15:57 2018/4/7
 * @Modified By:
 */

public interface UserAddrDao extends CrudRepository<UserAddr,Integer>{
    /**
     * 获取所有的该用户账号的对应地址信息记录
     * @param userCode
     * @return
     */
    public List<UserAddr> findAllByUserCode(String userCode);

    /**
     * 获取指定状态的地址（1--默认订单收货地址，0--不是）
     * @param userCode
     * @param state
     * @return
     */
    public UserAddr findByUserCodeAndState(String userCode,int state);

    /**
     * 删除用户地址中间表的 用户与地址的关联关系
     * @param addrId
     */
    @Modifying
    @Transactional
    public void deleteByAddrId(String addrId);


    /**
     * 通过地址id获取中间表的维护关系
     * @param addrId
     * @return
     */
    public UserAddr findByAddrId(String addrId);
}
