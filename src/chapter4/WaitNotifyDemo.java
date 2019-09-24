package chapter4;

public class WaitNotifyDemo {
    private static String LOCK = "Lock";
    public static void main(String[] args) {
        Thread wait = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    System.out.println("等待线程开始运行");
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("等待线程被唤醒开始运行");
                }
            }
        }, "等待线程");
        Thread notify = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    System.out.println("通知线程开始运行");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("通知线程休息2s结束");
                    LOCK.notifyAll();
                }
            }
        }, "通知线程");
        wait.start();
        notify.start();
    }
}

