package com.test;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author wuhao
 * @desc ...
 * @date 2021-09-17 14:39:38
 */
public class BioTest {
    @Test
    public void test1() {
        //1.初始化缓冲区数组
        ByteBuffer bf = ByteBuffer.allocate(1024);
        System.out.println("==========allocate============");
        System.out.println(bf.position());
        System.out.println(bf.limit());
        System.out.println(bf.capacity());

        //2.put插入数据
        bf.put("asasas".getBytes());
        System.out.println("==========put============");
        System.out.println(bf.position());
        System.out.println(bf.limit());
        System.out.println(bf.capacity());

        //3.改为读状态
        bf.flip();
        System.out.println("==========flip============");
        System.out.println(bf.position());
        System.out.println(bf.limit());
        System.out.println(bf.capacity());

        //4.获取缓冲区数据
        final byte[] bytes = new byte[bf.limit()];
        bf.get(bytes);
        System.out.println("==========get============");
        System.out.println(new String(bytes,0,bytes.length));
        System.out.println(bf.position());
        System.out.println(bf.limit());
        System.out.println(bf.capacity());

        //5.重置读状态
        bf.rewind();
        System.out.println("==========rewind============");
        System.out.println(bf.position());
        System.out.println(bf.limit());
        System.out.println(bf.capacity());

        //6.清除数据标识
        bf.clear();
        System.out.println("==========clear============");
        System.out.println(bf.position());
        System.out.println(bf.limit());
        System.out.println(bf.capacity());
    }
}
