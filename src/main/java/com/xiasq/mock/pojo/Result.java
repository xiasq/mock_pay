package com.xiasq.mock.pojo;

import lombok.Data;

/**
 * @author xiasq
 * @version Result, v0.1 2018/8/27 14:40
 */
@Data
public class Result {
    private int status;
    private String msg;
    private Object data;

    public static Result success(String msg){
        return result(200,msg);
    }

    public static Result success(String msg,Object data){
        return result(200,msg,data);
    }

    public static Result result(int status,String msg){
        return result(status,msg,null);
    }

    public static Result result(int status,String msg,Object data){
        Result result = new Result();
        result.setStatus(status);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
