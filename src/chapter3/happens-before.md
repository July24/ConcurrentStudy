#happens-before

A happens-before B
则表示操作B能看到操作A的执行结果

JMM的happens-before规则为了解决编译器优化对并发编程安全性的理解，所以通过happens-before规则定义一些不能进行重排序优化的情况

1. 程序顺序规则
>一个线程中的每个操作，happens-before之后的操作

2. 监视器锁规则
>无论在单线程还是在多线程里，对于**同一个锁**，一个线程对这个锁解锁以后，另一个线程获取到这个锁后，能看到前一个线程的操作结果

3. volatile变量规则
>对volatile变量的读操作一定能看到最后一次的写操作结果

4. 线程启动规则
>在主线程A执行的过程中启动线程B，那么线程A在启动B之前的操作对线程B可见

5. 线程终止规则
>在主线程A执行ThreadB.join()并成功返回，那么线程B终止之前的操作对线程A可见

6. 传递规则
> A happens-before B, B happens-before C 则 A happens-before C

##JMM的设计
1. 程序员对内存模型的使用。希望内存模型易于理解，易于编程，希望强内存模型。
2. 编译器、处理器对内存模型的实现。希望束缚越少越好，希望弱内存模型

happens-before规则是以上两点的平衡结果

as-if-serial保证单线程内程序执行的结果不被改变，happens-before关系保证正确的同步多线程程序执行结果不被改变  