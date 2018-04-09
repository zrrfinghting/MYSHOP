package com.shop.logic.dao;

import com.shop.logic.domain.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品操作接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */
public interface GoodsDao extends CrudRepository<Goods,Integer> {
    /**
     * 通过商品id获取商品信息
     *
     * @param id
     * @return
     */
    public Goods findById(String id);

    /**
     * 通过商品id删除商品信息
     *
     * @param id
     */
    @Modifying
    @Transactional
    public void deleteById(String id);


    /**
     *分页获取商品信息（可以根据keyword进行模糊查询）
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from Goods t where  t.goodsName like %?1% or t.goodsDesc like %?1% or t.id like %?1%")
    public Page<Goods> findAllByKeyword(String keyword, Pageable pageable);

    @Query("select count(*) from Goods t where  t.goodsName like %?1% or t.goodsDesc like %?1% or t.id like %?1%")
    public long countAllByKeyword(String keyword);


    /**
     * 分页获取指定分类的商品信息（可以根据keyword在这个分类中进行模糊查询）
     * @param catagoryId 分类id
     * @param keyword 搜索关键字
     * @param pageable
     * @return
     */
    @Query("select t from  Goods t where t.catagoryId=?1 and (t.goodsName like %?2% or t.goodsDesc like %?2% or t.id like %?2%)")
    public Page<Goods> findByCatagoryId(String catagoryId,String keyword,Pageable pageable);

    @Query("select count(*) from  Goods t where t.catagoryId=?1 and (t.goodsName like %?2% or t.goodsDesc like %?2% or t.id like %?2%)")
    public long countAllByKeyword(String catagoryId,String keyword);
}
