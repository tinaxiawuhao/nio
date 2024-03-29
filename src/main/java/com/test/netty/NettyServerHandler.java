package com.test.netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

//自定义服务器端业务处理类
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override //读取数据事件
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("服务器线程:" + Thread.currentThread().getName());
        System.out.println("Server: " + ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发来的消息 : " + buf.toString(CharsetUtil.UTF_8));
    }

    @Override //数据读取完毕事件
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("就是没钱", CharsetUtil.UTF_8));
    }

    @Override //异常发生事件
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}