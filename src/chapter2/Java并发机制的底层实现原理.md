# Java并发机制的底层实现原理
Java代码会编译成字节码，字节码会被类加载器加载到JVM里，JVM将字节码转换为汇编指令在CPU上执行所以Java中的并发机制依赖于JVM的实现与CPU的指令

## volatile的应用
synchronized和violate在并发编程中有着重要作用，violate是轻量级的synchronized，它保证了多处理器开发时的共享变量的可见性。当一个线程修改一个共享变量时，另外一个线程可以读到这个修改的值，violate比synchronized使用和执行成本更低，它不会引起线程上下文的切换和调度

### volatile的定义与实现原理
volatile定义：
>Java编程语言允许线程访问共享变量，为了确保共享变量能被准确和一致的更新，线程应该确保通过排他锁单独获得这个变量。

CPU与内存通信模型：
>为了提高处理速度，处理器不直接与内存进行通信，而是将需要的内存数据读取到CPU内部缓存（L1、L2或者其他）中后在内部缓存中进行操作，但不知道何时会将内部缓存写回主内存

缓存行：
>缓存中k可以分配的最小存储单元

实现原理：
>如果变量被violate修饰，JVM将字节码转化为汇编指令时,会多出一条Lock前缀的指令,此指令的作用有两点
>>1. 将当前处理器的缓存行数据写回到系统主内存
>>2. 这个写回操作会导致其他处理器中缓存了该内存地址的数据无效

### volatile的使用优化
并发包中队列集合类LinkedTransferQueue，它在使用volatile变量时，用一种追加字节的方式来优化队列出队和入队的性能
```java
// 队列中的头部节点
private transient final PaddedAtomicReference<QNode> head;
// 队列中的尾部节点 
private transient final PaddedAtomicReference<QNode> tail;

static final class PaddedAtomicReference <T> extends AtomicReference <T> {
// 使用很多4个字节的引用追加到64个字节
    Object p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, pa, pb, pc, pd, pe;
    PaddedAtomicReference(T r) {
        super(r);
    }
}
public class AtomicReference <V> implements java.io.Serializable {
    private volatile V value;
    // 省略其他代码
｝

```

原理：
>每个对象引用占4个字节，PaddedAtomicReference追加了15个对象，使该对象的占用64个字节。对于Intel酷睿7、Pentium M等处理器的L1、L2缓存的高速缓存行是64个字节宽，如果队列的头尾结点不足64个字节的话，会读取到同一个缓存行中，当一个处理器试图修改头/尾结点时，会锁定该缓存行，在缓存一致性的机制下，会导致其他处理器不能访问尾结点，队列的入队出队会频繁访问头尾结点，所以严重影响效率，将头尾结点追加到64字节，避免加载到同一个缓存行内，使头尾结点修改时不会相互锁定

下列不适用将volatile变量追加到64字节的情况：
1. 缓存行非64字节宽的处理器
>P6系列、奔腾处理器缓存32个字节宽
2. 共享变量不会被频繁地写
