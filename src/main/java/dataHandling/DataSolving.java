package dataHandling;
import DataInfo.UwbData;
import DataInfo.UwbReceiveInfo;
import myThread.ServerReaderRunnable;
import tools.BinaryTools;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author 张璐洋1
 */
public class DataSolving implements Runnable {
    public static ConcurrentHashMap<String, UwbData> concurrentHashMap = new ConcurrentHashMap<>();
    public static  LinkedBlockingQueue<String>  linkedBlockingQueue=  new LinkedBlockingQueue(100);
    public static String tagIdBatchSn;
    @Override
    public void run() {

         while (true){
             try {
                 if(!ServerReaderRunnable.uwbReceiveInfoData.isEmpty()){
                     UwbReceiveInfo queueHeadData = ServerReaderRunnable.uwbReceiveInfoData.poll();
                     String tagId = BinaryTools.binaryToHexString(queueHeadData.getTagId());
                     String batchSn = Integer.toHexString(queueHeadData.getBatchSn());
                     tagIdBatchSn = (tagId +batchSn).replaceAll(" ","");
                     Integer anchorId = queueHeadData.getAnchorId()[3];
//                System.out.println("anchorId = " + anchorId);
                     String distanceStr = BinaryTools.binaryToHexString(queueHeadData.getDistance()).replaceAll(" ","");
                     BigInteger bigInteger = new BigInteger(distanceStr, 16);
                     Float distance = Float.intBitsToFloat(bigInteger.intValue());
//                System.out.println(anchorId +"的tagIdBatchSn = " + tagIdBatchSn + "的距离为 = " + distance) ;
                     if(!linkedBlockingQueue.contains(tagIdBatchSn)){
                         linkedBlockingQueue.offer(tagIdBatchSn);
                     }
                     int index = anchorId - 251;
                     if(index < 0){
                         System.out.println("index 是一个错误值");
                         continue;
                     }
                     ConsumeConcurrentHashMap.lock.lock();
                    try {
                        if(concurrentHashMap.containsKey(tagIdBatchSn)){

//                    if(concurrentHashMap.get(tagIdBatchSn).getDistance()[index] != null){
//                        for (Float aFloat : concurrentHashMap.get(tagIdBatchSn).getDistance()) {
//                            aFloat = 0.0f;
//                        }
//                        count = 1;
//                    }
//                    if(count > 4){
//                        concurrentHashMap.get(tagIdBatchSn).setCount(1);
//                    }

                            Float[] distance1 = concurrentHashMap.get(tagIdBatchSn).getDistance();
                            if(distance1[index] != null){
                                Arrays.fill(concurrentHashMap.get(tagIdBatchSn).getDistance(), null);
                                concurrentHashMap.get(tagIdBatchSn).setCount(0);
                            }
//                         if(concurrentHashMap.get(tagIdBatchSn).getCount() > 4){
//                             Arrays.fill(concurrentHashMap.get(tagIdBatchSn).getDistance(), null);
//                             concurrentHashMap.get(tagIdBatchSn).setCount(0);
//
//                         }
                            distance1[index] = distance;
                            concurrentHashMap.get(tagIdBatchSn).setDistance(distance1);
                            Integer count = concurrentHashMap.get(tagIdBatchSn).getCount();
                            count += 1;
                            concurrentHashMap.get(tagIdBatchSn).setCount(count);

                        }else {
                            UwbData uwbData = new UwbData();
                            Float[] distance1 = new Float[4];
                            distance1[index] = distance;
                            uwbData.setDistance(distance1);
                            uwbData.setCount(1);
                            concurrentHashMap.put(tagIdBatchSn,uwbData);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        ConsumeConcurrentHashMap.lock.unlock();
                    }

                 }

             }catch (Exception e){
                 e.printStackTrace();
             }

         }
     }

    }
