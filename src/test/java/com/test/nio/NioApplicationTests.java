package com.test.nio;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NioApplicationTests {

    @Test
    public void test1() throws Exception{
        String str="hello,nio,我是XXX";
        FileOutputStream fos=new FileOutputStream("basic.txt");
        FileChannel fc=fos.getChannel();
        ByteBuffer buffer= ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        buffer.flip();
        fc.write(buffer);
        fos.close();
    }

    @Test
    public void test2() throws Exception{
        File file=new File("basic.txt");
        FileInputStream fis=new FileInputStream(file);
        FileChannel fc=fis.getChannel();
        ByteBuffer buffer=ByteBuffer.allocate((int)file.length());
        fc.read(buffer);
        System.out.print(new String(buffer.array()));
        fis.close();
    }

    @Test
    public void test3() throws Exception{
        FileInputStream fis=new FileInputStream("C:\\Users\\zdx\\Desktop\\oracle.mov");
        FileOutputStream fos=new FileOutputStream("d:\\oracle.mov");
        FileChannel sourceCh = fis.getChannel();
        FileChannel destCh = fos.getChannel();
        destCh.transferFrom(sourceCh, 0, sourceCh.size());
        sourceCh.close();
        destCh.close();
    }

}
