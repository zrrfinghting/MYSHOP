package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.LogDao;
import com.shop.logic.dao.UserDao;
import com.shop.logic.domain.Log;
import com.shop.logic.domain.User;
import com.shop.logic.util.JsonUtil;
import com.shop.logic.util.TableUtil;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户逻辑实现、对外暴露接口类
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/2
 */
@RestController
@RequestMapping("/user")
public class UserControl {
    @Autowired
    private UserDao userDao;
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
    public String addUser(@RequestBody User user){
        try {
            User oldUser = userDao.findByUserCode(user.getUserCode());
            if (oldUser!=null)
                return JsonUtil.returnStr(JsonUtil.FAIL,"该账号用户已经存在");
            user.setCreateTime(new Date());
            userDao.save(user);
            //记录日志
            logDao.save(new Log(BasicFilter.user_id,Log.ADD,"新增用户："+user.getUserCode()+"成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"新增用户成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "新增用户失败");
        }
    }

    @RequestMapping(value = "editUser", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    public String editUser(@RequestParam String json){
        try {
            User oldUser = userDao.findByUserCode(JsonUtil.getString("userCode",json));
            if (oldUser==null){
                return JsonUtil.returnStr(JsonUtil.FAIL,"修改的用户不存在");
            }
            oldUser = (User) JsonUtil.jsonToBean(oldUser,json);
            userDao.save(oldUser);
            //记录日志
            logDao.save(new Log(BasicFilter.user_id,Log.UPDATE,"修改用户："+oldUser.getUserCode()+"成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改用户信息成功");

        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"修改用户信息失败");
        }
    }
    @RequestMapping(value = "getByUserCode", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public String getByUserCode(@RequestParam String userCode){
        try {
            User user = userDao.findByUserCode(userCode);
            return JsonUtil.fromObject(user);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"获取用户信息失败");
        }
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户信息", notes = "删除用户信息")
    public String deleteById(@RequestParam String userCode){
        try {
            userDao.deleteByUserCode(userCode);
            //记录日志
            logDao.save(new Log(BasicFilter.user_id,Log.UPDATE,"删除用户："+userCode+"成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"删除用户信息成功");

        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"删除用户信息失败");
        }
    }

    @RequestMapping(value = "getPageByState", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取指定状态的用户的信息", notes = "分页获取指定状态的用户的信息")
    public String getPageByState(
            @RequestParam int state,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize ){
        try {
            Pageable pageable = new PageRequest(pageNum-1,pageSize,new Sort(Sort.Direction.DESC, "createTime"));
            Page<User> users = userDao.findAllByState(state,pageable);
            LinkedList<User> list = new LinkedList<>();
            for (User user:users){
                list.add(user);
            }
            long total = userDao.countAllByState(state);
            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            if (totalPage==0){//第一页开始
                totalPage=1;
            }
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"分页获取指定状态的用户的信息失败");
        }
    }

    @RequestMapping(value = "getPageByKeyword", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取用户的信息可以根据keyword进行模糊查询", notes = "分页获取用户的信息")
    public String getPageByKeyword(
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize ){
        try {
            Pageable pageable = new PageRequest(pageNum-1,pageSize,new Sort(Sort.Direction.DESC, "createTime"));
            Page<User> users = userDao.findAllByKeyword(keyword,pageable);
            LinkedList<User> list = new LinkedList<>();
            for (User user:users){
                list.add(user);
            }
            long total = userDao.countAllByKeyword(keyword);
            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            if (totalPage==0){//第一页开始
                totalPage=1;
            }
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"分页获用户的信息失败");
        }
    }
}
