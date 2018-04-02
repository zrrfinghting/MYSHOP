package com.shop.logic.controller;

import com.shop.logic.util.JsonUtil;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户逻辑实现、对外暴露接口类
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/2
 */
@RestController
@RequestMapping("/user")
public class UserControl {

    @RequestMapping(value = "addUser", method = RequestMethod.GET)
    @ApiOperation(value = "新增用户", notes = "新增用户")
    public String getUserByCode(){
        try {
            return "用户信息成功";
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "获取用户信息失败");
        }
    }
}
