package com.shop.logic.dao;

import com.shop.logic.domain.Catagory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品种类操作接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/8
 */
public interface CatagoryDao extends CrudRepository<Catagory,Integer>{

    /**
     * 根据分类id获取分类信息
     * @param id
     * @return
     */
    public Catagory findById(String id);

    /**
     * 根据父分类id获取分类信息
     * @param parentId
     * @return
     */
    public Catagory findByParentId(String parentId);

    @Modifying
    @Transactional
    public void deleteById(String id);

    /**
     * 分页获取分类信息
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from Catagory  t where t.catagoryName like %?1% or t.id like %?1% or t.catagoryDesc like %?1% ")
    public Page<Catagory> findAllByKeyword(String keyword, Pageable pageable);

    @Query("select count (*) from Catagory  t where t.catagoryName like %?1% or t.id like %?1%  or t.catagoryDesc like %?1% ")
    public long countAllByKeyword(String keyword);

    @Query("select t from Catagory t")
    public List<Catagory> findAll();
}
