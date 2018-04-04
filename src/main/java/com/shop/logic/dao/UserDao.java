package com.shop.logic.dao;

import com.shop.logic.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/3
 */
public interface UserDao extends CrudRepository<User,Integer>{

    /**
     * 根据用户的账号查询用户
     * @param userCode
     * @return User
     */
    public User findByUserCode(String userCode);

    /**
     * 根据用户账号删除用户信息
     * @param userCode
     */
    @Modifying
    @Transactional
    public void deleteByUserCode(String userCode);


    /**
     * 分页获取用户信息（可以根据keyword进行模糊查询）
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from  User t where t.userName like %?1% or t.userCode like %?1% or t.userDesc like %?1% or t.address like %?1%")
    public Page<User> findAllByKeyword(String keyword, Pageable pageable);
    @Query("select count (*) from  User t where t.userName like %?1% or t.userCode like %?1% or t.userDesc like %?1% or t.address like %?1%")
    public long countAllByKeyword(String keyword);


    /**
     * 分页获取指定状态的用户信息
     * @param state
     * @param pageable
     * @return
     */
    @Query("select t from User t where t.state=?1")
    public Page<User> findAllByState(int state,Pageable pageable);
    @Query("select count (*) from User t where t.state=?1")
    public long countAllByState(int state);

}
