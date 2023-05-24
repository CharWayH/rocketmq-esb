package com.charwayh.controller;

import com.charwayh.constant.MessageConstant;
import com.charwayh.entity.MessageResult;
import com.charwayh.entity.Result;
import com.charwayh.service.PubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.controller
 * @date:2023/5/23
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private PubService pubService;

    @RequestMapping("/send")
    public Result sendMsg(@RequestBody Map map) {
        MessageResult messageResult = pubService.sendMsg(map);
        return new Result(true, MessageConstant.SEND_SUCCESS.toString(),messageResult);
    }
}
