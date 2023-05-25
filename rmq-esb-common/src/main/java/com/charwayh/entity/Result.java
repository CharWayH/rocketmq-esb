package com.charwayh.entity;

import lombok.Data;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.entity
 * @date:2023/5/23
 */
@Data
public class Result {
    private boolean flag;
    private String msg;
    private Object data;

    public Result(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public Result(boolean flag, String msg, Object data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }
}
