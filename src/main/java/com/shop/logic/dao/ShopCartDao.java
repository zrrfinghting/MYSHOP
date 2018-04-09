package com.shop.logic.dao;

import com.shop.logic.domain.ShopCart;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 购物车接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */

public interface ShopCartDao extends CrudRepository<ShopCart,Integer>{

    /**
     * 获取用户购物车的所有
     * @param userCode
     * @return
     */
    public List<ShopCartDao> findAllByUserCode(String userCode);
}
