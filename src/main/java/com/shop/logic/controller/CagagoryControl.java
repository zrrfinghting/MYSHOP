package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.CatagoryDao;
import com.shop.logic.dao.LogDao;
import com.shop.logic.domain.Catagory;
import com.shop.logic.domain.Log;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 商品类
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/8
 */
@RestController
@RequestMapping("/catagory")
public class CagagoryControl {
    @Autowired
    private CatagoryDao catagoryDao;
    @Autowired
    private LogDao logDao;

    /**
     * 当新增同级分类时前端将当前选择的分类id设置到catagory对象的parentId上
     * @param catagory
     * @param level 1--新分类，2--同级分类，3--下级分类
     * @return
     */
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @ApiOperation(value = "新增分类", notes = "新增分类")
    public String addCatagory(@RequestBody Catagory catagory, @RequestParam int level){
        try {
            if (level==1 || level==3){
                catagory.setId(String.valueOf(System.currentTimeMillis()));
                catagory.setCreateTime(new Date());
                catagory.setUpdateTime(new Date());
                catagoryDao.save(catagory);
            }else if (level==2){
                Catagory oldCatagory = catagoryDao.findById(catagory.getParentId());
                catagory.setId(String.valueOf(System.currentTimeMillis()));
                catagory.setParentId(oldCatagory.getParentId());
                catagory.setCreateTime(new Date());
                catagory.setUpdateTime(new Date());
                catagoryDao.save(catagory);
            }else {
                return JsonUtil.returnStr(JsonUtil.FAIL,"新增分类失败，没有指定分类级别");
            }
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.ADD,"新增分类",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"新增分类成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"新增分类失败");
        }
    }
    @RequestMapping(value = "/editCatagory", method = RequestMethod.POST)
    @ApiOperation(value = "修改分类", notes = "修改分类")
    public String editCatagory(@RequestBody Object json){
        try {
            Catagory oldCatagory = catagoryDao.findById(JsonUtil.getString("id",json));
            if (oldCatagory==null){
                return JsonUtil.returnStr(JsonUtil.FAIL,"修改分类不存在");
            }
            oldCatagory = (Catagory)JsonUtil.jsonToBean(oldCatagory,json);
            oldCatagory.setUpdateTime(new Date());
            catagoryDao.save(oldCatagory);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.UPDATE,"修改分类:"+oldCatagory.getId()+"",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"修改失败");
        }
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "通过ID删除分类", notes = "通过ID删除分类")
    public String deleteById(@RequestParam String id){
        try {
            Catagory catagory = catagoryDao.findByParentId(id);
            if (catagory==null){
                catagoryDao.deleteById(id);
                return JsonUtil.returnStr(JsonUtil.SUCCESS,"删除成功");
            }else {
                return JsonUtil.returnStr(JsonUtil.FAIL,"存在子分类："+catagory.getCatagoryName()+"不能删除");
            }

        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"删除失败");
        }
    }
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取分类信息", notes = "通过id获取分类信息")
    public String getById(@RequestParam String id){
        try {
            Catagory catagory = catagoryDao.findById(id);
            return JsonUtil.fromObject(catagory);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取失败");
        }
    }
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取分类信息", notes = "")
    public String getPageByKeyword(
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize){
        try {
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "createDate"));
            Page<Catagory> catagories = catagoryDao.findAllByKeyword(keyword,pageable);
            LinkedList<Catagory> list = new LinkedList<>();
            for (Catagory catagory:catagories){
                list.add(catagory);
            }
            long total = catagoryDao.countAllByKeyword(keyword);
            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            if (totalPage==0){
                totalPage=1;
            }
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"分页获取分类信息失败");
        }
    }
    @RequestMapping(value = "/getTrees", method = RequestMethod.GET)
    @ApiOperation(value = "获取分类树", notes = "获取分类树")
    public String getTrees(){
        try {
            List<Catagory> catagories = catagoryDao.findAll();//所有分类
            List<Catagory> roots = new ArrayList<>();
            List<Catagory> list = new ArrayList<>();
            List<Object> trees = new ArrayList<>();

            return "";
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取失败");
        }
    }
}
