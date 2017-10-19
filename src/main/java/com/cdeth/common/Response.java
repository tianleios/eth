package com.cdeth.common;

import org.apache.ibatis.annotations.ResultMap;

import java.io.Serializable;

/**
 * Created by tianlei on 2017/8/3.
 */
public class Response<T> implements Serializable {

    private int code;
    private String msg;
    private T data;
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public Response(int code, String msg, T data){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    static public <T> Response success(T data){

      return new Response<T>(0,"success",data);

    }

    static public <T>  Response<T> failure(int code, String errorMsg){

        return new Response<T>(code,errorMsg,null);
    }

//    private Response(int code,String msg) {
//
//        this.code = code;
//        this.msg = msg;
//    }

}
