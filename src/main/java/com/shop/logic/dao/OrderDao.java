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
     * 指定用户
     *
     * 分页获取指定状态的订单
     * @param userCode
     * @param state
     * @return
     */
    public Page<Order> findAllByUserCodeAndState(String userCode,int state,Pageable pageable);
    public long countAllByUserCodeAndState(String userCode,int state);

    /**
     * 所有用户
     *
     * @param state
     * @return
     */
    public Page<Order> findAllByState(int state,Pageable pageable);
    public long countAllByState(int state);


    /**
     * 所有用户
     *
     * 订单产生时间区间查找
     *
     * @param startTime
     * @param endTime
     * @param state
     * @return
     */
    @Query("select t from Order t where t.state=?3 and (t.createTime between ?1 and ?2)")
    public Page<Order> findAllByTime(String startTime,String endTime,int state,Pageable pageable);

    @Query("select count (*) from Order t where t.state=?3 and (t.createTime between ?1 and ?2)")
    public long countAllByTime(String startTime,String endTime,int state);

}
