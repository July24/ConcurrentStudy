# Java中的锁

## Lock接口
    锁用来控制多个线程访问共享资源，JDK5之前只能通过synchronized来实现，之后新加了Lock接口，提供了与synchronized类似的同步功能，只是在使用时需要显式的获取和释放锁，虽然它缺少了隐式释放锁的便捷性，但确拥有了锁获取与释放的可操作性，可中断的获取锁以及超时获取锁等多种synchronized不具备的功能

Lock接口提供的不同于synchronized的特性

| 特性 | 描述 |
| :-----: | :----: |
| 非阻塞地获得锁 | 线程尝试获得锁，如果无法获得会立即返回 |
| 能被中断的获取锁 | 与synchronized不同，获取锁的线程可以响应中断，可以抛出InterruptedException，并释放锁 |
| 超时获取锁 | 阻塞状态，阻塞于锁 |

Lock的API

| 方法 | 描述 |
| :-----: | :----: |
| void lock() | 获取锁，调用该方法当前线程将会获取锁，当锁获得后，从该方法返回 |
| void lockInterruptibly() throws InterruptedException | 可中断的获取锁，和lock方法的不同之处 |
| boolean tryLock() | 尝试非阻塞的获取锁,调用方法后立即返回，成功返回true，失败返回false |
| boolean tryLock(long time, TimeUnit unit) throws InterruptedException | 超时获取锁，三种情况下返回， 超时时间内获得锁、超时时间内被中断、超时时间结束，返回false|
| void unlock() | 释放锁 |
| Condition newCondition() | 获取等待通知组件，该组件和当前的锁绑定，当前线程只有获得了锁，才能调用该 |

## 队列同步器
