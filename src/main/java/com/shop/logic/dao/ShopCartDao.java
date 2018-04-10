package com.shop.logic.dao;

import com.shop.logic.domain.ShopCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */

public interface ShopCartDao extends CrudRepository<ShopCart,Integer>{

    /**
     * 获取用户购物车的所有商品信息
     * @param userCode
     * @return
     */
    public List<ShopCartDao> findAllByUserCode(String userCode);

    /**
     * 获取购物车里的指定商品信息
     * @param id
     * @return
     */
    public ShopCart findById(String id);


    /**
     * 删除用户里的商品信息
     * @param id
     */
    @Modifying
    @Transactional
    public void deleteById(String id);


}
