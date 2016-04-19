package jacz.database;

import jacz.util.concurrency.ThreadUtil;
import jacz.util.concurrency.task_executor.ThreadExecutor;

/**
 * Created by Alberto on 05/02/2016.
 */
public class TestParallel {

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        DatabaseMediator.dropAndCreate("store.db", "a");
        System.out.println(System.currentTimeMillis());
        DatabaseMediator.dropAndCreate("store2.db", "a");
        System.out.println(System.currentTimeMillis());

        new Movie("store.db");
        new Movie("store.db");
        new Movie("store.db");
        new Movie("store2.db");
        new Movie("store2.db");
        new Movie("store2.db");
        System.out.println(System.currentTimeMillis());


//        ParallelTaskExecutor.executeTask(new ParallelTask() {
//            @Override
//            public void performTask() {
//                DatabaseMediator.dropAndCreate("store.db", "a");
//                new Movie("store.db");
//                new Movie("store.db");
//                new Movie("store.db");
//                Base.close();
//            }
//        });
//        ParallelTaskExecutor.executeTask(new ParallelTask() {
//            @Override
//            public void performTask() {
//                DatabaseMediator.dropAndCreate("store2.db", "a");
//                new Movie("store2.db");
//                new Movie("store2.db");
//                new Movie("store2.db");
//            }
//        });


        ThreadUtil.safeSleep(1000);
        System.out.println("Start test...");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        ThreadExecutor.registerClient("this");

        ThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    Movie.getMovies("store.db");
                }
            }
        });

        ThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    Movie.getMovies("store.db");
                }
            }
        });

        ThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    Movie.getMovies("store2.db");
                }
            }
        });

        ThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    Movie.getMovies("store2.db");
                }
            }
        });

        ThreadExecutor.shutdownClient("this");

        ThreadUtil.safeSleep(4000);

    }
}
