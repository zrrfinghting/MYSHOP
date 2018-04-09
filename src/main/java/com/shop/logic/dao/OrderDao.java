package com.shop.logic.dao;

import com.shop.logic.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单操作接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */
public interface OrderDao extends CrudRepository<Order,Integer>{

    /**
     *
     * @param id 订单id
     * @return
     */
    public Order findById(String id);


    /**
     * 删除订单
     * @param id
     */
    @Modifying
    @Transactional
    public void deleteById(String id);


    /**
     * 分页获取用户的订单
     * @param userCode
     * @param pageable
     * @return
     */
    public Page<Order> findAllByUserCode(String userCode, Pageable pageable);
    public long countAllByUserCode(String userCode);


    /**
     * 分页获取指定状态的订单
     * @param userCode
     * @param state
     * @return
     */
    public Page<Order> findAllByUserCodeAndState(String userCode,int state,Pageable pageable);
    public long countAllByUserCodeAndState(String userCode,int state);



}
