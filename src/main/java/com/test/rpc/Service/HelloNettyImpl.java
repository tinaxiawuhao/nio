package com.test.rpc.Service;

public class HelloNettyImpl implements HelloNetty {
    @Override
    public String hello() {
        return "hello,netty";
    }
}