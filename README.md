#文件IO
##缓冲区（Buffer） ： 
####实际上是一个容器， 是一个特殊的数组， 缓冲区对象内置了一些机制， 能够跟踪和记录缓冲区的状态变化情况。 Channel 提供从文件、 网络读取数据的渠道，但是读取或写入的数据都必须经由 Buffer.在 NIO 中， Buffer 是一个顶层父类， 它是一个抽象类， 常用的 Buffer 子类有：
```
 ByteBuffer， 存储字节数据到缓冲区
 ShortBuffer， 存储字符串数据到缓冲区
 CharBuffer， 存储字符数据到缓冲区
 IntBuffer， 存储整数数据到缓冲区
 LongBuffer， 存储长整型数据到缓冲区
 DoubleBuffer， 存储小数到缓冲区
 FloatBuffer， 存储小数到缓冲区
```
####对于 Java 中的基本数据类型， 都有一个 Buffer 类型与之相对应， 最常用的自然是ByteBuffer 类（二进制数据） ， 该类的主要方法如下所示：
```
 public abstract ByteBuffer put(byte[] b); 存储字节数据到缓冲区
 public abstract byte[] get(); 从缓冲区获得字节数据
 public final byte[] array(); 把缓冲区数据转换成字节数组
 public static ByteBuffer allocate(int capacity); 设置缓冲区的初始容量
 public static ByteBuffer wrap(byte[] array); 把一个现成的数组放到缓冲区中使用
 public final Buffer flip(); 翻转缓冲区， 重置位置到初始位置
```
##通道（Channel） ：
####类似于 BIO 中的 stream， 例如 FileInputStream 对象， 用来建立到目标（文件， 网络套接字， 硬件设备等） 的一个连接， 但是需要注意： BIO 中的 stream 是单向的， 例如 FileInputStream 对象只能进行读取数据的操作， 而 NIO 中的通道(Channel)是双向的，既可以用来进行读操作， 也可以用来进行写操作。 常用的 Channel 类有： FileChannel、DatagramChannel、 ServerSocketChannel 和 SocketChannel。 FileChannel 用于文件的数据读写，DatagramChannel 用于 UDP 的数据读写， ServerSocketChannel 和 SocketChannel 用于 TCP 的数据读写。这里我们先讲解 FileChannel 类， 该类主要用来对本地文件进行 IO 操作， 主要方法如下所示：
```
 public int read(ByteBuffer dst) ， 从通道读取数据并放到缓冲区中
 public int write(ByteBuffer src) ， 把缓冲区的数据写到通道中
 public long transferFrom(ReadableByteChannel src, long position, long count)， 从目标通道中复制数据到当前通道
 public long transferTo(long position, long count, WritableByteChannel target)， 把数据从当前通道复制给目标通
```
#网络 IO
##概述和核心 API ：
###前面在进行文件 IO 时用到的 FileChannel 并不支持非阻塞操作， 学习 NIO 主要就是进行网络 IO， Java NIO 中的网络通道是非阻塞 IO 的实现， 基于事件驱动， 非常适用于服务器需要维持大量连接， 但是数据交换量不大的情况， 例如一些即时通信的服务等等....在 Java 中编写 Socket 服务器， 通常有以下几种模式：

```
 一个客户端连接用一个线程， 优点： 程序编写简单； 缺点： 如果连接非常多， 分配的线程也会非常多， 服务器可能会因为资源耗尽而崩溃。
 把每一个客户端连接交给一个拥有固定数量线程的连接池， 优点： 程序编写相对简单，可以处理大量的连接。 确定： 线程的开销非常大， 连接如果非常多， 排队现象会比较严重。
 使用 Java 的 NIO， 用非阻塞的 IO 方式处理。 这种模式可以用一个线程， 处理大量的客户端连接。
```
##Selector(选择器)：
 ####能够检测多个注册的通道上是否有事件发生， 如果有事件发生， 便获取事件然后针对每个事件进行相应的处理。 这样就可以只用一个单线程去管理多个通道， 也就是管理多个连接。这样使得只有在连接真正有读写事件发生时，才会调用函数来进行读写，就大大地减少了系统开销， 并且不必为每个连接都创建一个线程， 不用去维护多个线程， 并且避免了多线程之间的上下文切换导致的开销。该类的常用方法如下所示：
```
 public static Selector open()， 得到一个选择器对象
 public int select(long timeout)， 监控所有注册的通道， 当其中有 IO 操作可以进行时， 将对应的 SelectionKey 加入到内部集合中并返回， 参数用来设置超时时间
 public Set<SelectionKey> selectedKeys()， 从内部集合中得到所有的 SelectionKey
```

##SelectionKey：
####代表了 Selector 和网络通道的注册关系,一共四种：
```
 int OP_ACCEPT： 有新的网络连接可以 accept， 值为 16
 int OP_CONNECT： 代表连接已经建立， 值为 8
 int OP_READ 和 int OP_WRITE： 代表了读、 写操作， 值为 1 和 4
该类的常用方法如下所示：
 public abstract Selector selector()， 得到与之关联的 Selector 对象
 public abstract SelectableChannel channel()， 得到与之关联的通道
 public final Object attachment()， 得到与之关联的共享数据
 public abstract SelectionKey interestOps(int ops)， 设置或改变监听事件
 public final boolean isAcceptable()， 是否可以 accept
 public final boolean isReadable()， 是否可以读
 public final boolean isWritable()， 是否可以写
```
##ServerSocketChannel：
####用来在服务器端监听新的客户端 Socket 连接， 常用方法如下所示：
 ```
 public static ServerSocketChannel open()， 得到一个 ServerSocketChannel 通道
 public final ServerSocketChannel bind(SocketAddress local)， 设置服务器端端口号
 public final SelectableChannel configureBlocking(boolean block)， 设置阻塞或非阻塞模式，取值 false 表示采用非阻塞模式
 public SocketChannel accept()， 接受一个连接， 返回代表这个连接的通道对象
 public final SelectionKey register(Selector sel, int ops)， 注册一个选择器并设置监听事件
```
##SocketChannel：
####网络 IO 通道， 具体负责进行读写操作。 NIO 总是把缓冲区的数据写入通道， 或者把通道里的数据读到缓冲区。 常用方法如下所示：
```
 public static SocketChannel open()， 得到一个 SocketChannel 通道
 public final SelectableChannel configureBlocking(boolean block)， 设置阻塞或非阻塞模式，取值 false 表示采用非阻塞模式
 public boolean connect(SocketAddress remote)， 连接服务器
 public boolean finishConnect()， 如果上面的方法连接失败， 接下来就要通过该方法完成连接操作
 public int write(ByteBuffer src)， 往通道里写数据
 public int read(ByteBuffer dst)， 从通道里读数据
 public final SelectionKey register(Selector sel, int ops, Object att)， 注册一个选择器并设置监听事件， 最后一个参数可以设置共享数据
 public final void close()， 关闭通道
```
