package com.test;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author wuhao
 * @desc 本地io:
 * 　　 FileInputStreanm/FileOutputStream
 * 　　RandomAccessFile
 * 网络io:
 * 　　Socket
 * 　　ServerSocket
 * 　　DatagramSocket
 * @date 2021-09-17 15:22:26
 */
public class ChannelTest {

    //利用通道完成文件的复制,非直接缓冲区
    @Test
    @SneakyThrows
    public void test(){
        FileInputStream fis = new FileInputStream("D:\\1.jpg");
        FileOutputStream fos = new FileOutputStream("D:\\2.jpg");
        //获取通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        //分配指定大小缓存区
        ByteBuffer buff = ByteBuffer.allocate(1024);// position 0 ,limit 1024
        //将通道的数据存入缓存区
        while(inChannel.read(buff)!=-1){// position 1024 ,limit 1024 ,相当于put
            //切换读模式
            buff.flip();//position 0 ,limit 1024
            //将缓存去的数据写入通道
            outChannel.write(buff);//position 1024 ,limit 1024,相当于get
            //清空缓冲区
            buff.clear();//position 0 ,limit 1024
        }
        outChannel.close();
        inChannel.close();
        fis.close();
        fos.close();
    }

    //利用通道完成文件的复制,直接缓冲区,利用物理内存映射文件
    //会出现文件复制完了，但程序还没结束，原因是JVM资源还在用，当垃圾回收机制回收之后程序就会结束,不稳定
    @Test
    @SneakyThrows
    public void test1(){
        FileChannel inChannel = FileChannel.open(Paths.get("D:\\1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:\\4.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //内存映射文件
        MappedByteBuffer inMapBuff = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());//==allocateDirect
        MappedByteBuffer outMapBuff = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        byte[] by = new byte[inMapBuff.limit()];
        inMapBuff.get(by);
        outMapBuff.put(by);
        outChannel.close();
        inChannel.close();
    }

    /**
     * 从源信道读取字节到这个通道的文件中。如果源通道的剩余空间小于 count 个字节，则所传输的字节数要小于请求的字节数。这种方法可能比从源通道读取并写入此通道的简单循环更有效率。
     * @param SRC 源通道
     * @param position 调动开始的文件内的位置，必须是非负的
     * @param count 要传输的最大字节数，必须是非负
     * @return 传输文件的大小（单位字节），可能为零，
     * public abstract long transferFrom(ReadableByteChannel src, long position, long count)　throws IOException;
     */
    //复制图片，利用直接缓存区
    @Test
    @SneakyThrows
    public void test2(){
        FileChannel inChannel = FileChannel.open(Paths.get("D:\\1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:\\2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        outChannel.transferFrom(inChannel, 0, inChannel.size());
        inChannel.close();
        outChannel.close();
    }

    /**
     * 将字节从这个通道的文件传输到给定的可写字节通道。
     * @param position 调动开始的文件内的位置，必须是非负的
     * @param count 要传输的最大字节数，必须是非负
     * @param target 目标通道
     * @return 传输文件的大小（单位字节），可能为零，
     * public abstract long transferTo(long position, long count, WritableByteChannel target) throws IOException;
     */
    //复制图片，利用直接缓存区
    @Test
    @SneakyThrows
    public void test3(){
        FileChannel inChannel = FileChannel.open(Paths.get("D:\\1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:\\3.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inChannel.close();
        outChannel.close();
    }

    // 分散读取聚集写入实现文件复制

    @Test
    @SneakyThrows
    public void test4(){
        RandomAccessFile randomAccessFile = null;
        RandomAccessFile randomAccessFile1 = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            randomAccessFile = new RandomAccessFile(new File("d:\\old.txt"), "rw");
            randomAccessFile1 = new RandomAccessFile(new File("d:\\new.txt"), "rw");
            inChannel = randomAccessFile.getChannel();
            outChannel = randomAccessFile1.getChannel();
            // 分散为三个bytebuffer读取,capcity要设置的足够大，不然如果文件太大，会导致复制的内容不完整
            ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
            ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
            ByteBuffer byteBuffer3 = ByteBuffer.allocate(10240);
            ByteBuffer[] bbs = new ByteBuffer[]{byteBuffer1,byteBuffer2,byteBuffer3};

            inChannel.read(bbs);// 分散读取

            // 切换为写入模式
            for (int i = 0; i < bbs.length; i++) {
                bbs[i].flip();
            }

            outChannel.write(bbs);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
