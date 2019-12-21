package com.test.rpc.ServiceSub;

import lombok.Data;

import java.io.Serializable;

//封装类信息
@Data
public class ClassInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String className; //类名
    private String methodName; //方法名
    private Class<?>[] types; //参数类型
    private Object[] objects; //参数列表

}