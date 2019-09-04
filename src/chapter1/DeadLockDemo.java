package chapter1;

/**
 * 死锁Demo。
 *      t1、t2线程相互等待对方的锁
 */
public class DeadLockDemo {
    private static String lockA = "A";
    private static String lockB = "B";

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockA) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lockB) {
                        System.out.println("执行t1");
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockB) {
                    synchronized (lockA) {
                        System.out.println("执行t2");
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}
