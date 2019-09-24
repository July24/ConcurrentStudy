# volatile、锁、final域的内存语义

##volatile的内存语义

### volatile的特征
    对volatile变量的单个读/写，等同于使用同一个锁对这些个读/写操作做了同步。

```java
class VolatileFeatureExample {
    volatile long v = 0L;
    public void set(long l) {
        v = l;    
    }
    public void getAndIncrement() {
        v++;
    }
    public long get() {
        return v;
    }
}
```

等同于如下代码
```java
class VolatileFeatureExample {
    volatile long v = 0L;
    public synchronized void set(long l) {
        v = l;    
    }
    public void getAndIncrement() {
        long temp = get();
        temp += 1L;
        set(temp);
    }
    public synchronized long get() {
        return v;
    }
}
```
如例子所示，一个volatile变量的单个读/写操作与一个普通变量的读/写操作使用同一个锁来同步的执行效果相同。

锁的happens-before规则保证获取锁和释放锁之间的可见性。对一个volatilebi变量的读，总能看到任意线程对这个变量最后的写

volatile变量有下列特征：
1. 可见性。对于volatile变量的读总能读取到最新值
2. 原子性。对于单个volatile的读/写具有原子性

从内存语义来说，volatile的写-读与释放-获得锁有相同的内存效果

### volatile写-读的内存语义
    写语义：当写一个volatile变量时，JMM会把该线程对应的本地内存中的值刷新回主内存
    
    读语义：当读一个volatile变量时，JMM会把该线程对应的本地内存置为无效，该线程将从内存中读取共享变量
    
JMM通过插入内存屏障保证volatile写操作之前的操作不能重排序到volatile写操作之后，保证volatile读之后的操作不会重排序volatile读之前。

## 锁的内存语义
    当线程释放锁的时候，JMM会把该线程对应的本地内存中的共享变量刷新到主内存中
    当线程获得锁时，JMM会把该线程对应的本地内存置为无效，从而使得被监视器保护的临界区代码必须从主内存中获得共享变量
    
## final域重排序规则
1. 在构造函数内对一个final域的写入，与随后把这个被构造对象的引用赋值给一个引用变量，这两个操作不能重排序
2. 初次读一个包含final域的对象的引用，与随后读这个final域不能重排序