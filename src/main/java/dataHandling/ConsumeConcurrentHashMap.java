package dataHandling;

import DataInfo.Point;
import DataInfo.UwbData;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 张璐洋1
 */
public class ConsumeConcurrentHashMap implements Runnable {

    public static Lock lock = new ReentrantLock();
    @Override
    public void run() {
        while (true){
            try {
                if(DataSolving.concurrentHashMap.size() != 0){
                    for (Map.Entry<String, UwbData> entry : DataSolving.concurrentHashMap.entrySet()) {
                        lock.lock();
                        try {
                           String k = entry.getKey();
                           UwbData v = entry.getValue();
                           if (v.getCount() == 4) {
                               Point point = FindTest.coordinateProcessing(v.getDistance());
                               String tagId = k.substring(0, 8);
//                               Float[] distance = DataSolving.concurrentHashMap.get(k).getDistance();
//                               Arrays.fill(distance, null);
                               v.setCount(0);
                               System.out.println(tagId + ":" + point);

                           }
                            if (DataSolving.linkedBlockingQueue.size() > 80){
                                int count = 0;
                                while (count < 40){
                                    DataSolving.concurrentHashMap.remove(DataSolving.linkedBlockingQueue.poll());
                                    System.out.println("清理无法结算的tagid");
                                    System.out.println("concurrentHashMap的大小为= " + DataSolving.concurrentHashMap.size());
                                    count ++;
                                }

                            }

                        }catch (Exception e){
                           e.printStackTrace();
                       }finally {
                            lock.unlock();
                       }

                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
