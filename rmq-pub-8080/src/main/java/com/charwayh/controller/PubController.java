package com.charwayh.controller;


import com.charwayh.annotation.MsgCheck;
import com.charwayh.entity.Result;
import com.charwayh.service.PubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.controller
 * @date:2023/5/23
 */
@RestController
@RequestMapping("/pub")
public class PubController {
    @Autowired
    private PubService pubService;


    @RequestMapping("/send")
    @MsgCheck
    public Result sendMsg(@RequestBody Map map) {
        return pubService.sendMsg(map);
    }
}
