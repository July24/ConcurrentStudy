package test;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ThreadOne implements Callable<Integer> {
    public static void main(String[] args) {
        Callable<Integer> callable = new ThreadOne();
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
    }

    @Override
    public Integer call() throws Exception {
        //do something
        return null;
    }
}
