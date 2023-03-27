package myThread;

import DataInfo.UwbReceiveInfo;
import dataHandling.DataParsing;

import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author 张璐洋1
 */
public class ServerReaderRunnable implements Runnable {
    private Socket socket;
    public static ConcurrentLinkedQueue<UwbReceiveInfo> uwbReceiveInfoData = new ConcurrentLinkedQueue<>();
    public ServerReaderRunnable(Socket socket){
    this.socket = socket;

    }
    @Override
    public void run() {
        DataParsing dataParsing = new DataParsing();
        try {
            InputStream is = socket.getInputStream();
            int readByte;
            UwbReceiveInfo uwbReceiveInfo = null;
            while ((readByte = is.read()) != -1) {

                uwbReceiveInfo = dataParsing.parsingData(readByte);
                if (uwbReceiveInfo != null) {
                    uwbReceiveInfoData.offer(uwbReceiveInfo);
                    uwbReceiveInfo = null;
//                    System.out.println((socket.getRemoteSocketAddress()).toString() +"的" +  uwbReceiveInfo);
                }
            }
        } catch (Exception e) {
            System.out.println(socket.getRemoteSocketAddress() + "下线啦");
        }

    }
}
