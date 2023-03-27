import com.sun.org.apache.bcel.internal.generic.NEW;
import dataHandling.ConsumeConcurrentHashMap;
import dataHandling.DataSolving;
import myThread.MyThreadPool;
import tcp.MyServer;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 张璐洋1
 */
public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        DataSolving dataSolving =  new DataSolving();
        MyServer.threadPool.execute(dataSolving);
        MyServer.threadPool.execute(new ConsumeConcurrentHashMap());
        MyServer.createServerConn(31000);
//        String str = "402A4CC2";
//        BigInteger bigInteger = new BigInteger(str, 16);
//        System.out.println(bigInteger);
//        float v = Float.intBitsToFloat(bigInteger.intValue());
//        System.out.println(v);
    }
}
