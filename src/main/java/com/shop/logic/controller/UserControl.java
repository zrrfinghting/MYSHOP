package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.LogDao;
import com.shop.logic.dao.UserDao;
import com.shop.logic.domain.Log;
import com.shop.logic.domain.User;
import com.shop.logic.util.JsonUtil;
import com.shop.logic.util.MD5Util;
import com.shop.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
    public String addUser(@RequestBody User user){
        try {
            User oldUser = userDao.findByUserCode(user.getUserCode());
            if (oldUser!=null)
                return JsonUtil.returnStr(JsonUtil.FAIL,"该账号用户已经存在");
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setPassword(MD5Util.getMd5(user.getUserCode(),user.getPassword()));
            userDao.save(user);
            //记录日志
            Log log = new Log(BasicFilter.user_id,Log.ADD,"新增用户："+user.getUserCode()+"成功",new Date());
            logDao.save(log);
           //logDao.save(new Log(BasicFilter.user_id,Log.ADD,"新增用户："+user.getUserCode()+"成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"新增用户成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "新增用户失败");
        }
    }

    @RequestMapping(value = "editUser", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    public String editUser(@RequestBody Object json){
        try {
            JSONObject jsonObject = JSONObject.fromObject(json);
            jsonObject.remove("password");//防止修改用户信息的时候也将密码修改了
            User oldUser = userDao.findByUserCode(JsonUtil.getString("userCode",jsonObject));
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
            user.setPassword("");//不返回密码
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
            logDao.save(new Log(BasicFilter.user_id,Log.DELETE,"删除用户："+userCode+"成功",new Date()));
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
                user.setPassword("");//不返回密码
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

    @RequestMapping(value = "getByPage", method = RequestMethod.GET)
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
                user.setPassword("");//不返回密码信息
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

    /**
     * 登陆系统成功  返回用户信息（除密码）+token（用户名+时间戳）
     * @param obj
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "登陆系统", notes = "登陆系统")
    public String login(@RequestParam Object obj){
        try {
            String userCode = JsonUtil.getString("userCode",obj);
            String password = JsonUtil.getString("password",obj);
            User oldUser = userDao.findByUserCode(userCode);
            if (oldUser==null)
                return JsonUtil.returnStr(JsonUtil.FAIL,"用户不存在");
            if (oldUser.getState()==3)
                return JsonUtil.returnStr(JsonUtil.FAIL,"账号已经停用请联系管理员");
            if (oldUser.getPassword().equals(MD5Util.getMd5(userCode,password))){
                //记录日志
                logDao.save(new Log(BasicFilter.user_id,Log.LOGIN,"用户："+userCode+"登陆系统成功",new Date()));
                oldUser.setPassword("");
                JSONObject json = JSONObject.fromObject(oldUser);
                String token = MD5Util.getMd5(userCode,String.valueOf(System.currentTimeMillis()));
                json.put("token",token);
                //使用redis缓存以<key,value>形式存储 userCode--token
                redisTemplate.opsForValue().set(userCode, token, 30, TimeUnit.MINUTES);//十分钟有效
                return json.toString();//登陆系统成功并返回token和用户信息
            }else {
                return JsonUtil.returnStr(JsonUtil.FAIL,"密码错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"登陆系统失败");
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ApiOperation(value = "注销系统", notes = "注销系统")
    public String logout(@RequestParam String userCode){
        try {
            redisTemplate.delete(userCode);
            return JsonUtil.returnStr(JsonUtil.FAIL,"注销成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"注销失败");
        }
    }

    @RequestMapping(value = "editPassword", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public String editPassword(@RequestBody Object obj){
        try {
            String userCode = JsonUtil.getString("userCode",obj);
            String password = JsonUtil.getString("password",obj);
            String newPassword =JsonUtil.getString("newPassword",obj);

            User user = userDao.findByUserCode(userCode);
            if (user.getPassword().equals(MD5Util.getMd5(userCode,password))){
                user.setPassword(MD5Util.getMd5(userCode,newPassword));
                user.setUpdateTime(new Date());
                userDao.save(user);
                //日志
                logDao.save(new Log(BasicFilter.user_id,Log.UPDATE,"修改密码",new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改密码成功");
            }else {
                return JsonUtil.returnStr(JsonUtil.FAIL,"旧密码不正确");
            }

        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"修改密码失败");
        }
    }

    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public String resetPassword(@RequestBody Object obj){
        try {
            String userCode = JsonUtil.getString("userCode",obj);
            String password = JsonUtil.getString("password",obj);
            String verifyCode = JsonUtil.getString("verifyCode",obj);
            String oldVerifyCode = redisTemplate.opsForValue().get(userCode).toString();

            if (verifyCode.equals(oldVerifyCode)){
                User user = userDao.findByUserCode(userCode);
                user.setPassword(MD5Util.getMd5(userCode,password));
                user.setUpdateTime(new Date());
                userDao.save(user);
                //日志
                logDao.save(new Log(userCode,Log.UPDATE,"重置密码",new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS,"重置密码成功");
            }else {
                return JsonUtil.returnStr(JsonUtil.FAIL,"验证码错误");
            }

        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"重置密码失败");
        }
    }
}
