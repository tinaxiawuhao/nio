package com.test.bio;

import com.test.nio.NioApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NioApplication.class)
public class BioApplicationTests {

    /**
     * 1. 在电脑D盘下创建一个文件为HelloWorld.txt文件，
     * 判断他是文件还是目录，再创建一个目录IOTest,
     * 之后将HelloWorld.txt移动到IOTest目录下去；
     * 之后遍历IOTest这个目录下的文件
     *
     * 程序分析：
     * 1、文件创建使用File的createNewFile()方法
     * 2、判断是文件用isFile(),判断是目录用isDirectory
     * 3、创建目录用：mkdirs()方法
     * 4、移动文件用：renameTo
     * 5、遍历目录用：list（）方法获得存放文件的数组，foreach遍历的方法把文件打印出来
     * */
    @Test
    public void test1(){
        //在电脑D盘下创建一个文件为HelloWorld.txt文件
        File file=new File("D://HelloWorld.txt");

        //创建文件，返回一个布尔值
        boolean isCreate;
        try {
            isCreate = file.createNewFile();
            if(isCreate){
                System.out.println("创建文件成功！");
            }else{
                System.out.println("创建文件失败,文件已存在！");
            }
        }catch (IOException e){
            System.out.println("创建文件失败");
        }

        // 判断他是文件还是目录，
        if(file.isFile()){
            System.out.println("这是一个文件");
        }else{
            System.out.println("这是一个目录");
        }

        //再创建一个目录IOTest
        File file2=new File("D:/IOTest");
        file2.mkdirs();

        //移动文件用：renameTo（File file）里面需要传入一个file类型的对象
        File file3 = new File(file2.getPath()+"/"+file.getName());
        //File file3 = new File("D:/IOTest/文件名"); //这里的代码加上
        //file.renameTo(file3)会创建一个文件
        if(file.renameTo(file3)) {
            System.out.println("移动成功");
        }else {
            System.out.println("移动失败");
        }

        //遍历IOTest目录下的文件
        //list() 返回一个字符串数组，命名由此抽象路径名表示的目录中的文件和目录。
        String[] list = file2.list();
        for(String str:list) {
            System.out.println(str);
        }
    }

    @Test
    public void test2(){
        //比如输入D盘下自己建的测试文件夹
        List<File> list = FileUtils.getAllFiles("D:/IOTest");
        //输出所有的文件和文件夹的名字
        for(File file:list) {
            System.out.println(file);

        }

    }

    private static class FileUtils {
        //获取文件夹下所有的文件
        public static List<File> getAllFiles(String dir){
            //创建一个集合存放遍历到的File
            List< File > files=new ArrayList();

            File file=new File(dir);
            //文件夹必须存在并且要是文件夹
            if (file.exists()&&file.isDirectory()) {
                //重点！这里要本身一直遍历
                longErgodic(file,files);//把遍历得到的东西存放在files里面
            }
            return files;
        }

        //重点理解，这是一个递归方法，会不断来回调用本身，但是所有获得的数据都会存放在集合files里面
        private static void longErgodic(File file, List files) {

            //.listFiles()方法的使用
            //把文件夹的所有文件（包括文件和文件名）都放在一个文件类的数组里面
            File[] fillArr=file.listFiles();

            //如果文件夹有内容,遍历里面的所有文件（包括文件夹和文件），都添加到集合里面
            for (File file2 : fillArr) {

                //如果只是想要里面的文件或者文件夹或者某些固定格式的文件可以判断下再添加
                files.add(file2);

                //添加到集合后，在来判断是否是文件夹，再遍历里面的所有文件
                //方法的递归
                if(file2.isDirectory()) {
                    longErgodic(file2, files);
                }

            }
        }
    }

    /**
     * 递归实现列出当前工程下所有.java文件
     * 还是要题目2的遍历文件的工具类来获取所有的文件，再过滤.java文件就可以了
     * 当前目录的地址：输入.就可以获取
     * */
    @Test
    public void test3(){
        //比如输入D盘下自己建的测试文件夹
        List<File> list = FileUtils.getAllFiles(".");
        //输出所有的文件和文件夹的名字
        for(File file:list) {
            if (file.toString().endsWith(".java")) {
                System.out.println(file.getName());
            }

        }
    }

    /**
     * 从磁盘读取一个文件到内存中，再打印到控制台
     *
     * 程序设计：
     * 1、读取文件用到FileinputSteam
     * 2、把读取的内容不断加入到StringBuffer，
     * 3、再把StringBuffer打印出来就可以
     * */
    @Test
    public void test4(){
        // 读取D:\notePad\aa.txt里面的内容
        //java7可以对实现了Closable接口的类使用try catch resource
        //不用将close写在finally{}里面了，不用书写
        File file=new File("D:/IOTest/frfdfgf/sdfsfs/asas.txt");
        // 创建读取流即输入流
        //调用字节流,设置读取文件的编码格式

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"))) {
            StringBuffer sb = new StringBuffer();
            // 把读取的数据添加到StringBuffer里面
            //格式固定,对象名len可以自拟
            String len;
            while ((len=br.readLine()) != null) {
                //逐行输出
                System.out.println("....." + len);

                sb.append(len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 在程序中写一个"HelloJavaWorld你好世界"输出到操作系统文件Hello.txt文件中
     *
     * 程序分析：文件写入，要用到输出流FileOutputStream
     * */
    @Test
    public void test5(){
        File file=new File("D:/IOTest/HelloWorld.txt");
        try(FileOutputStream fos = new FileOutputStream(file)) {
            // 创建输出流
            //把String类型的字符串转化为byte数组的数据保存在输出流中
            fos.write("HelloJavaWorld你好世界".getBytes());
            fos.flush();//刷新输出流
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 拷贝一张图片，从一个目录到另外一个目录下(PS:是拷贝是不是移动)
     *
     * 程序设计思路：
     *  这题不能使用renameTo，
     *  解题步骤：
     * 1、在目的地址创建一个图片文件
     * 2、读取源地址文件的字节流
     * 3、把读取到的字节流写入到目的地址的文件里面
     * 4、刷新输出流，并关闭就可以了
     *
     * @throws Exception
     * */
    @Test
    public void test6(){
        // 本题示范把D盘下的mm.jpg复制到D盘java文件夹里面
        // 源文件地址
        File fileFrom = new File("D:/IOTest/1.gif");
        // 目的文件地址
        File fileTo = new File("D:/IOTest/frfdfgf/1.gif");
        try {
            if (!fileTo.createNewFile()) {
                System.out.println("创建文件失败！");
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileFrom));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTo)));

            String len;
            while ((len=bufferedReader.readLine()) != null) {
                //逐行输出
                bufferedWriter.write(len);
            }
            //bufferedWriter.flush();
            // 关闭输入流和输出流
            bufferedReader.close();
            bufferedWriter.close();
            System.out.println("文件复制成功！");
        }catch (IOException e){
            System.out.println("文件复制失败");
        }

    }

    /**
     * 统计一个文件asas.txt（见附件）中字母'A'和'a'出现的总次数
     *
     * 程序分析：
     * 读取文件用FileInputStream
     * 一次只读一个字节(一个字母就是一个字节)，当字节内容和A或a相等时，相应的数量加1
     * */
    @Test
    public void test7(){
        try {
            //添加文件路径
            File file = new File("D:/IOTest/frfdfgf/sdfsfs/asas.txt");
            //创建文件读取流
            FileInputStream fis = new FileInputStream(file);
            int numA = 0;//字母A的数量
            int numa = 0;//字母a的数量
            int len = 0;//每次读取的字节数量
            while ((len=fis.read())!= -1) {
                //统计字母a的数量
                if (new String((char)len+"").equals("a")) {
                    numa++;
                }
                //统计字母A的数量
                if (new String((char)len+"").equals("A")) {
                    numA++;
                }
            }
            //打印出文件内字母的数量
            System.out.println("a的数量是："+numa);
            System.out.println("A的数量是："+numA);
            System.out.println("a和A出现的总次数："+(numA+numa));
            fis.close();//关闭输入流
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 统计一个文件asas.txt（见附件）中各个字母出现次数：
     * A(8),B(16),C(10)...,a(12),b(10),c(3)....，括号内代表字符出现次数;
     *
     * 程序分析：
     * 1.这里没中文字符，依然可以只用字节流来读取文件
     * 2.不能保存相同的主键值，可以使用HashMap：key-value来实现
     * 3.先获得该key的value，如果存在key的话value的值加1
     * */
    @Test
    public void test8(){
        // 文件路径
        File file = new File("D:/IOTest/frfdfgf/sdfsfs/asas.txt");
        try {
            // 创建字符流
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), "GBK");
            // 创建集合HashMap类存放要保存的key-value
            HashMap map = new HashMap<>();

            // 读取文件
            int len = 0;// 每次读取的文件长度
            int count = 0;
            while ((len = is.read()) != -1) {
                // 每次获取到的字母
                char c = (char) len;
                //这里使用try catch是因为 map.get(c + "")，第一次get不到东西会出现空指针
                try {
                    // 通过每次的key值获取它的value值，
                    // 但是在它的key值没有时或报空指针错误，所以要try catch处理
                    // 当她有key值，就可以获取到相应的value值
                    count = (int) map.get(c + "");
                } catch (Exception e) {// 什么都不用输出
                }
                // 如果有它的key值对应的value值要加1
                map.put(c + "", count + 1);
            }
            is.close();

            // 读完后把结果打印出来
            //迭代器的使用
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                System.out.println(entry.getKey() + "{" + entry.getValue()+ "} \t");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test9(){
        /**
         * 使用随机文件流类RandomAccessFile将一个文本文件倒置读出。
         *
         * 程序分析：
         *  RandomAccessFile的seek方法能把读取文件的光标移动到具体的位置
         * 但是还是有地点值得注意的是一个字母或数字是占用一个字节的， 一个汉字是占用两个字节的
         * */

        // 要读取的文件的地址
        File file = new File("D:/IOTest/frfdfgf/sdfsfs/asas.txt");
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            long length = raf.length();
            StringBuffer sb = new StringBuffer();
            while (length > 0) {
                length--;
                raf.seek(length);
                int c = (char) raf.readByte();
                // 如果asc码<=255,>=0,则判断是个英文字符,添加到字符串中.
                if (c >= 0 && c <= 255) {
                    sb.append((char) c);
                } else {
                    // 如果不在asc码范围内,则判断是个汉字字符
                    // 汉字字符是占2个字节的,所以length再退一个字节
                    length--;
                    raf.seek(length);
                    byte[] cc = new byte[2];
                    // cc被复制为文件中连续的两个字节
                    raf.readFully(cc);
                    sb.append(new String(cc));
                }
            }
            System.out.println(sb);
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 输入两个文件夹名称，将A文件夹内容全部拷贝到B文件夹，要求使用多线程来操作。
     *
     * 程序分析：
     * 1.拷贝文件里面的东西，要分析的东西还是蛮多的，要先建文件夹再拷贝里面的东西，而且要一层层的来搞
     * 2.这里也需要文件遍历工具，直接调用第二题的工具类，不再重写
     * 3.多线程的使用，可以直接在方法里面直接新建线程
     * 4.对整个文件夹进行复制 文件夹分隔符可以用\\或/，其他的都是不对的
     * 所有其中还对输入的分割符进行了替换
     * 这题看起来比较长，分开看其实也不长
     */
    @Test
    public void test10(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("要复制的文件夹：");
        String fromDir = scanner.next();// 接收输入 D:/IOTest/frfdfgf
        System.out.println("要复制到哪里去：");
        String toDir = scanner.next();// 接收输入 D:/IOTest/deede

        // 把输入的地址转化为File类型
        File fromFile = new File(fromDir);
        File toFile = new File(toDir);

        //新建线程
        new Thread(){
            //里面做实际操作
            public void run() {
                // 判断如果要复制的是文件，直接复制就可以了
                if (fromFile.isFile()) {
                    System.out.println("复制单个文件");
                    copy(fromFile, toFile);
                } else {
                    // 要复制文件夹
                    // 要防止一种无法进行的复制：比如说，要把复制的文件复制到自己的子文件夹里面
                    // 举个例子：把D:/java/jsp文件夹复制到D:/java/jsp/js文件夹里面，
                    // 这会导致子文件不断增加的同时，而父文件也要不断增加，的一个死循环
                    // 如果反过来，就没事，相当于只是简单的覆盖而已
                    // 具体实现就是看：目的地地址包含复制的文件夹地址，就不允许操作
                    if (toDir.replace("/", "\\").toLowerCase()
                            .startsWith(fromDir.replace("/", "\\").toLowerCase())) {
                        return;
                    }

                    // 复制文件（包括文件和文件夹）操作

                    // 先获取所有的文件（包括文件和文件夹）
                    List<File> list = FileUtils.getAllFiles(fromDir);

                    // 创建一个线程池，加快复制的速度
                    ExecutorService threadPool = Executors.newFixedThreadPool(20);

                    // 需要对每一个文件的路径进行处理
                    for (File file : list) {
                        // 复制文件名
                        String name = file.getAbsolutePath();
                        // 把原来的文件路径换成新的文件路径
                        String toName = name.replace(fromFile.getParent(), toDir + "/");
                        System.out.println(name + "变成了" + toName);
                        // 如果是文件夹，直接创建
                        if (file.isDirectory()) {
                            new File(toName).mkdirs();
                        } else {
                            // 如果是文件,在线程里面复制
                            threadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    File copyFile = new File(toName);
                                    // 先要有父文件夹
                                    copyFile.getParentFile().mkdirs();

                                    // 开始复制文件
                                    copy(file, copyFile);
                                }
                            });
                        }
                    }

                }
                scanner.close();
            };

        }.start();//开始线程
    }

    //复制文件的操作
    public static void copy(File fromFile, File toFile) {
        // 定义一个输入流
        FileInputStream fis = null;
        // 定义一个输出流
        FileOutputStream fos = null;

        try {
            // 把复制地址的File，封装后赋值给输入流对象
            fis = new FileInputStream(fromFile);
            // 把目的地的File，封装后复制给输出流的对象
            fos = new FileOutputStream(toFile);
            // 创建一个容量，
            byte[] buf = new byte[1024];
            // 每次读取/写入的字节长度
            int len = 0;
            // 边读边写
            while ((len = fis.read(buf)) != -1) {// 判断是否还能读到数据
                // 把输入放到输出流里面
                fos.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输入流和输出流
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 查看D盘中所有的文件和文件夹名称，并且使用名称升序降序，
     * 文件夹在前和文件夹在后，文件大小排序等。
     *
     * 程序分析:
     * 1.查找文件和文件夹，需要例题二里遍历文件的工具类（这里直接调用，不在重写）
     * 2.排序需要用到list集合里面的Collections工具类类的sort方法
     * 3.这里有三重排序：首先是要按是否是文件夹来排序，然后按名称来排序，最后按大小来排序
     *   其实这里还是会有问题的，按照某一个排序还没事，但是三个都排序就不一定有效！
     实际应用中也是只按一种排序就可以了的
     * */
    @Test
    public void test11(){
        List<File> list =FileUtils.getAllFiles("D:/IOTest");

        //按文件夹先显示的顺序：
        list.sort((o1, o2) -> (o2.isDirectory() ? 1 : -1) - (o1.isDirectory() ? 1 : -1));

        //按文件名称显示的顺序：
        list.sort((o1, o2) -> (o1.getName()).compareTo(o2.getName()));

        //按文件名称显示的顺序：
        list.sort((o1, o2) -> (int) (o1.length() - o2.length()));

        //遍历集合的文件
        for (File file : list) {
            //打印排序后的文件或文件夹
            System.out.println(file.getName());
        }
    }


}
