package com.shop.logic.controller;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.shop.logic.dao.LogDao;
import com.shop.logic.dao.OrderDao;
import com.shop.logic.domain.Order;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单对外服务类
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/9
 */

@RestController
@RequestMapping("/order")
public class OrderControl {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private LogDao logDao;

    /**
     *
     * @param obj
     * @return
     */
  /*  @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    @ApiOperation(value = "新增订单信息", notes = "新增订单信息")
    public String addOrder(@RequestBody Object obj){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
