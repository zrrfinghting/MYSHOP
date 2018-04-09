package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.GoodsDao;
import com.shop.logic.dao.LogDao;
import com.shop.logic.domain.Goods;
import com.shop.logic.domain.Log;
import com.shop.logic.util.DateUtil;
import com.shop.logic.util.JsonUtil;
import com.shop.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;

/**
 * 商品对外类
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */

@RestController
@RequestMapping(name = "/goods")
public class GoodsControl {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "addGoods", method = RequestMethod.POST)
    @ApiOperation(value = "新增商品信息", notes = "新增商品信息")
    public String addGoods(@RequestBody Goods goods){
        try {
            goods.setId(String.valueOf(System.currentTimeMillis()));
            goods.setCreateTime(new Date());
            goods.setUpdateTime(new Date());
            goodsDao.save(goods);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.ADD,"新增商品"+goods.getGoodsName()+"成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"新增商品信息成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"新增失败");
        }
    }
    @RequestMapping(value = "editGoods", method = RequestMethod.POST)
    @ApiOperation(value = "修改商品信息", notes = "修改商品信息")
    public String editGoods(@RequestBody Object json){
        try {
            Goods oldGoods = goodsDao.findById(JsonUtil.getString("id",json));
            if (oldGoods==null){
                return JsonUtil.returnStr(JsonUtil.FAIL,"修改的商品不存在");
            }
            oldGoods = (Goods) JsonUtil.jsonToBean(oldGoods,json);
            goodsDao.save(oldGoods);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.UPDATE,"修改商品："+ oldGoods.getGoodsName()+"信息成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"修改失败");
        }
    }
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除", notes = "删除")
    public String deleteById(@RequestParam String id){
        try {
            goodsDao.deleteById(id);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.DELETE,"删除商品："+id+"成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"删除失败");
        }
    }

    @RequestMapping(value = "getById", method = RequestMethod.GET)
    @ApiOperation(value = "获取商品信息", notes = "获取商品信息")
    public String getById(@RequestParam String id){
        try {
            Goods goods = goodsDao.findById(id);
            return JsonUtil.fromObject(goods);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取失败");
        }
    }

    @RequestMapping(value = "getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取商品的信息可以根据keyword进行模糊查询", notes = "分页获取商品的信息")
    public String getPageByKeyword(
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize ){
        try {
            Pageable pageable = new PageRequest(pageNum-1,pageSize,new Sort(Sort.Direction.DESC, "createTime"));
            Page<Goods> goodss = goodsDao.findAllByKeyword(keyword,pageable);
            LinkedList<Goods> list = new LinkedList<>();
            for (Goods goods:goodss){
                list.add(goods);
            }
            long total = goodsDao.countAllByKeyword(keyword);
            long totalPage = total/pageSize==0? total/pageSize:total/pageSize+1;
            if (totalPage==0){
                totalPage=1;
            }
            return TableUtil.createTableDate(list,total,pageNum,totalPage,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取失败");
        }
    }

    @RequestMapping(value = "getByCatagoryId", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取指定分类的商品可以根据keyword进行模糊查询", notes = "分页获取商品的信息")
    public String getByCatagoryId(
            @RequestParam String catagoryId,
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize ){
        try {
            Pageable pageable = new PageRequest(pageNum-1,pageSize,new Sort(Sort.Direction.DESC, "createTime"));
            Page<Goods> goodss = goodsDao.findByCatagoryId(catagoryId,keyword,pageable);
            LinkedList<Goods> list = new LinkedList<>();
            for (Goods goods:goodss){
                list.add(goods);
            }
            long total = goodsDao.countAllByKeyword(catagoryId,keyword);
            long totalPage = total/pageSize==0? total/pageSize:total/pageSize+1;
            if (totalPage==0){
                totalPage=1;
            }
            return TableUtil.createTableDate(list,total,pageNum,totalPage,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取失败");
        }
    }
}
