package com.shop.logic.controller;

import com.shop.basic.BasicFilter;
import com.shop.logic.dao.AddrDao;
import com.shop.logic.dao.LogDao;
import com.shop.logic.dao.UserAddrDao;
import com.shop.logic.domain.Address;
import com.shop.logic.domain.Log;
import com.shop.logic.domain.UserAddr;
import com.shop.logic.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author:Dreram
 * @Description: 用户地址对外暴露类
 * @Date:created in :0:11 2018/4/7
 * @Modified By:
 */
@RestController
@RequestMapping("/address")
public class AddrControl {
    @Autowired
    private AddrDao addrDao;
    @Autowired
    private UserAddrDao userAddrDao;
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "addAddrrss", method = RequestMethod.POST)
    @ApiOperation(value = "新增地址信息", notes = "新增地址信息")
    @Transactional//开启事务
    public String addAddress(@RequestBody Address address){
        try {
            address.setId(String.valueOf(System.currentTimeMillis()));//使用系统当前时间毫秒值作为地址id
            address.setCreateTime(new Date());
            address.setUpdateTime(new Date());
            addrDao.save(address);
            //同时保存用户地址中间表的关联关系
            UserAddr userAddr = new UserAddr();
            userAddr.setUserCode(BasicFilter.user_id);
            userAddr.setAddrId(address.getId());
            userAddr.setCreateTime(new Date());
            userAddr.setUpdateTime(new Date());
            userAddrDao.save(userAddr);

            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.ADD,"新增地址信息",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"新增地址信息成功");
        }catch (Exception e){
            e.printStackTrace();
            //因为sping 默认trycatch是不进行事务回滚的，可以在手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtil.returnStr(JsonUtil.FAIL,"新增地址信息失败");
        }
    }

    /**
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "editAddrrss", method = RequestMethod.POST)
    @ApiOperation(value = "修改地址信息", notes = "修改地址信息")
    public String editAddress(@RequestParam Object json){
        try {
            JSONObject jsonObject = JSONObject.fromObject(json);
            Address address = addrDao.findById(JsonUtil.getString("id",jsonObject));
            address = (Address) JsonUtil.jsonToBean(address,jsonObject);
            address.setUpdateTime(new Date());
            addrDao.save(address);
            //日志
            logDao.save(new Log(BasicFilter.user_id,Log.UPDATE,"修改用户地址成功",new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"修改用户地址成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL,"修改地址信息失败");
        }
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除地址信息", notes = "删除地址信息")
    @Transactional
    public String deleteById(@RequestParam String id){
        try {
            userAddrDao.deleteByAddrId(id);//删除中间表关系
            addrDao.deleteById(id);//删除地址表的地址信息
            return JsonUtil.returnStr(JsonUtil.SUCCESS,"删除地址信息成功");
        }catch (Exception e){
            e.printStackTrace();
            //因为sping 默认trycatch是不进行事务回滚的，可以在手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtil.returnStr(JsonUtil.FAIL,"删除地址信息失败");
        }
    }

    /**
     * 设置默认收货地址
     * @param addrId
     * @return
     */
    @RequestMapping(value = "defaultAddr", method = RequestMethod.POST)
    @ApiOperation(value = "设置为默认收货地址", notes = "设置为默认收货地址")
    @Transactional
    public String defaultAddr(@RequestParam String addrId){
        try {
            //查看该用户的默认收货地址是哪个，不再将其设为默认
            UserAddr userAddr = userAddrDao.findByUserCodeAndState(BasicFilter.user_id,1);
            if (userAddr!=null){
                userAddr.setState(0);
                userAddr.setUpdateTime(new Date());
                userAddrDao.save(userAddr);
            }
            UserAddr newUserAddr = userAddrDao.findByAddrId(addrId);
            newUserAddr.setState(1);
            userAddrDao.save(newUserAddr);
            //日志

            return JsonUtil.returnStr(JsonUtil.FAIL,"设置成功");
        }catch (Exception e){
            e.printStackTrace();
            //因为sping 默认trycatch是不进行事务回滚的，可以在手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtil.returnStr(JsonUtil.FAIL,"设置默认收货地址失败");
        }
    }
}
