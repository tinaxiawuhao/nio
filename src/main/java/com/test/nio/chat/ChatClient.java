package com.test.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChatClient {
    private final String HOST = "127.0.0.1"; //服务器地址
    private int PORT = 9999; //服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public ChatClient() throws IOException {
        //得到选择器
        selector = Selector.open();
        //连接远程服务器
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //注册选择器并设置为 read
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到客户端 IP 地址和端口信息， 作为聊天用户名使用
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("---------------Client(" + userName + ") is ready---------------");
    }

    //向服务器端发送数据
    public void sendMsg(String msg) throws Exception {
        //如果控制台输入 bye 就关闭通道， 结束聊天
        if (msg.equalsIgnoreCase("bye")) {
            socketChannel.close();
            socketChannel = null;
            return;
        }
        msg = userName + "说: " + msg;
        try {
            //往通道中写数据
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从服务器端接收数据
    public void receiveMsg() {
        try {
            int readyChannels = selector.select();
            if (readyChannels > 0) { //有可用通道
                Set selectedKeys = selector.selectedKeys();
                Iterator keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey sk = (SelectionKey) keyIterator.next();
                    if (sk.isReadable()) {
                        //得到关联的通道
                        SocketChannel sc = (SocketChannel) sk.channel();
                        //得到一个缓冲区
                        ByteBuffer buff = ByteBuffer.allocate(1024);
                        //读取数据并存储到缓冲区
                        sc.read(buff);
                        //把缓冲区数据转换成字符串
                        String msg = new String(buff.array());
                        System.out.println(msg.trim());
                    }
                    keyIterator.remove(); //删除当前 SelectionKey， 防止重复处理
                }
            } else {
                System.out.println("人呢？ 都去哪儿了？ 没人聊天啊...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
