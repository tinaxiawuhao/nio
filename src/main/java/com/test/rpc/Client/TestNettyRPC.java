package com.test.rpc.Client;

import com.test.rpc.ClientStub.NettyRPCProxy;
import com.test.rpc.Service.HelloNetty;
import com.test.rpc.Service.HelloRPC;

//服务调用方
public class TestNettyRPC {
    public static void main(String[] args) {
        //第 1 次远程调用
        HelloNetty helloNetty = (HelloNetty) NettyRPCProxy.create(HelloNetty.class);
        System.out.println(helloNetty.hello());
        //第 2 次远程调用
        HelloRPC helloRPC = (HelloRPC) NettyRPCProxy.create(HelloRPC.class);
        System.out.println(helloRPC.hello("RPC"));
    }
}