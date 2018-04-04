package com.shop.logic.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * json转化类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/25
 */

public class JsonUtil {
    public static final int FAIL = 500;    //操作失败
    public static final int SUCCESS = 200; //操作成功

    /**
     * JSON格式返回值字符串
     *
     * @param status 返回状态
     * @param msg    返回信息
     * @return 字符串
     */
    public static String returnStr(int status, String msg) {
        try {
            JSONObject json = new JSONObject();
            json.put("status", status);
            json.put("msg", msg);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将对象转换成JSONObject
     *
     * @param obj 对象
     * @return jsonObject类型的字符串
     * @throws Exception
     */
    public static String fromObject(Object obj) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(obj);
        return jsonObject.toString();
    }

    /**
     * 将对象转换成JSONArray
     *
     * @param obj 对象
     * @return JSONArray类型的字符串
     * @throws Exception
     */
    public static String fromArray(Object obj) {
        JSONArray jsonArray = JSONArray.fromObject(obj);
        return jsonArray.toString();
    }

    /**
     * 获取JSON对象中的值
     *
     * @param key 字段
     * @param obj 对象
     * @return 字符串值
     * @throws Exception
     */
    public static String getString(String key, Object obj) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if (jsonObject.containsKey(key)) {
            return jsonObject.getString(key);
        } else {
            return null;
        }
    }

    /**
     * 将JSONArray 转换成 list
     *
     * @param object
     * @return list集合
     */
    public static List getList(Object object) {
        JSONArray jsonArray = JSONArray.fromObject(object);
        List<Object> list = new ArrayList<>();
        for (Object obj : jsonArray) {
            list.add(obj);
        }
        return list;
    }

    /**
     * 获取json字符串中参数修改对象中对应的参数
     * @param obj
     * @param jsonStr
     * @return 如果都能正确进行赋值就返回赋值后的对象，否则就返回null
     */
    public static Object jsonToBean(Object obj,String jsonStr){
        try {
            JSONObject json = JSONObject.fromObject(jsonStr);
            if (obj!=null){
                //根据反射遍历类的所有方法参数类型

                Map<String,String> map = new ConcurrentHashMap<>();//保存set方法的参数类型
                Method []methods = obj.getClass().getMethods();
                for (Method method:methods){
                    Class []types = method.getParameterTypes();
                    for (Class type:types){
                        System.out.println("方法名称： "+method.getName()+"     参数类型"+type.getName());
                        map.put(method.getName(),type.getName());
                    }
                }

                //遍历传入的json
                Iterator<String> it = json.keys();
                while (it.hasNext()){
                    String key = (String) it.next();
                    //例如象的setter方法是setUserCode 所以"set"+key 中的字符串变量key对应的字符串的开头要大写才能拼接后变为setUserCode
                    String paramName = "set"+key.substring(0,1).toUpperCase()+key.substring(1);//将第一个字符变为大写，再拼接之后的字符
                    String type = map.get(paramName);
                    Method method = null;
                    if ("int".equals(type)){
                         method= obj.getClass().getMethod(paramName,int.class);
                        method.invoke(obj,json.get(key));
                    }
                    if ("java.lang.String".equals(type)){
                        method= obj.getClass().getMethod(paramName,String.class);
                        method.invoke(obj,json.get(key));
                    }
                    if ("java.util.Date".equals(type)){
                        method= obj.getClass().getMethod(paramName,Date.class);
                        method.invoke(obj,DateUtil.parse(DateUtil.FORMAT2,json.get(key).toString()));
                    }
                }
            }
            return obj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
