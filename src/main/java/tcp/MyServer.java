package tcp;

import dataHandling.DataSolving;
import myThread.ServerReaderRunnable;
import myThread.MyThreadPool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

public class MyServer {
    public static ThreadPoolExecutor threadPool = new MyThreadPool().createThreadPool();

    public static void createServerConn(Integer port) throws IOException {
        try {

            ServerSocket serverSocket = new ServerSocket(port);
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("服务器启动，等待连接:\n本机ip为:" + hostAddress + "\n本机监听端口为:" + port);

            while (true) {
                Socket socket = serverSocket.accept();
                String ip = socket.getInetAddress().getHostAddress();
                System.out.println(socket.getRemoteSocketAddress()+ "上线啦！");
                threadPool.execute(new ServerReaderRunnable(socket));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}