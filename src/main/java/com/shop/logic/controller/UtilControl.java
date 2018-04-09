package com.shop.logic.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.shop.basic.BasicFilter;
import com.shop.logic.dao.LogDao;
import com.shop.logic.dao.UserDao;
import com.shop.logic.domain.Log;
import com.shop.logic.util.JsonUtil;
import com.shop.logic.util.SendMessage;
import com.shop.logic.util.UploadFileUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author:Dreram
 * @Description: 工具类对外接口控制类
 * @Date:created in :14:59 2018/4/6
 * @Modified By:
 */
@RestController
@RequestMapping("/util")
public class UtilControl {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LogDao logDao;
    @Autowired
    private UserDao userDao;

    //只用于测试  真正发布的时候不对外暴露接口
    @RequestMapping(value = "sendOrderMessage", method = RequestMethod.GET)
    @ApiOperation(value = "发送订单通知短信", notes = "发送订单通知短信")
    public String sendOrderMessage(String phone, String content) {
        try {
            String toPhone = "18300072030";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "陈冠有是个sb");
            jsonObject.put("time", "20170825");
            jsonObject.put("phone", "13826143050");
            //发送订单通知信息
            SendSmsResponse response = SendMessage.sendSms(phone, "归兰海科技", "SMS_89665019", jsonObject.toString());
            System.out.println("短信接口返回的数据");
            System.out.println("Code=" + response.getCode());
            System.out.println("Message=" + response.getMessage());
            System.out.println("RequestId=" + response.getRequestId());
            System.out.println("BizId=" + response.getBizId());
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "发送短信成功");

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "发送短信失败");
        }
    }

    /**
     * 使用电话号码作为用户ID，发送验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "sendVerifyCode", method = RequestMethod.GET)
    @ApiOperation(value = "发送验证码短信", notes = "发送验证码短信")
    public String sendVerifyCode(@RequestParam String phone) {
        try {
            //发送注册验证码
            String verifyCode = String.valueOf((int) (Math.random() * 9 + 1) * 1000);//生成四位随机0到9 的验证码
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", verifyCode);
            SendSmsResponse response = SendMessage.sendSms(phone, "归兰海科技", "SMS_90110038", jsonObject.toString());

            //使用redis缓存以<key,value>形式存储 userId--verifyCode (使用电话号码作为用户ID)
            redisTemplate.opsForValue().set(phone, verifyCode, 10, TimeUnit.MINUTES);//十分钟有效
            System.out.println("短信接口返回的数据");
            System.out.println("Code=" + response.getCode());
            System.out.println("Message=" + response.getMessage());
            System.out.println("RequestId=" + response.getRequestId());
            System.out.println("BizId=" + response.getBizId());
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "发送验证码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "发送验证码失败");
        }
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String result = UploadFileUtil.singleFileUpload(file);
            int status = Integer.parseInt(JsonUtil.getString("status", result));
            if (status == 200) {
                // //记录日志
                String fileName = JsonUtil.getString("fileName", result);
                logDao.save(new Log(BasicFilter.user_id, Log.FILEUPlOAD, "文件" + fileName + "上传成功", new Date()));
                return result;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "上传文件失败");
        }
    }
}
