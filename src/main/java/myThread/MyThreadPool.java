package myThread;

import java.util.concurrent.*;

/**
 * @author 张璐洋1
 */
public class MyThreadPool{
    ThreadPoolExecutor threadPoolExecutor;
   public ThreadPoolExecutor createThreadPool(){
       threadPoolExecutor = new ThreadPoolExecutor(
               6, 8, 60L, TimeUnit.SECONDS,
               new ArrayBlockingQueue<>(6), Executors.defaultThreadFactory(),
               new ThreadPoolExecutor.AbortPolicy());
       return threadPoolExecutor;
   }
   public void execute(Runnable task){
       threadPoolExecutor.execute(task);
   }

    public <T> Future<T> submit(Callable<T> task){
        return threadPoolExecutor.submit(task);
    }

}
