package edu.xyl;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * @Author: cxx
 * @Date: 2018/6/20 15:20
 */
public class ServerTest {
    private static int port = 8888;
    private static Socket accept;
    private static ServerSocket socket;
    private static BufferedWriter bw;
    public static void main(String[] args) throws Exception {
        socket = new ServerSocket(port);
        System.out.println("服务器开启，等待连接....");
        int num = 1;
        while (true){
            accept = socket.accept();
            System.out.println(num++);
            InputStreamReader r = new InputStreamReader(accept.getInputStream());
            System.out.println("浏览器请求成功!");
            BufferedReader br = new BufferedReader(r);
            String readLine = br.readLine();
            System.out.println("---------------------");
            //打印请求消息
            String filePath="log";
            int i=0;
            while(readLine != null && !readLine.equals("")){
                if (i==0){
                    String[] split = readLine.split(" ");
                    if (split[1].endsWith("html")) {
                        filePath += split[1];
                    }
                    writeHtml(filePath);
                }
                i++;
                readLine=br.readLine();
            }
            System.out.println("----------------------");
            //发送响应请求
            System.out.println(filePath);

         //   accept.shutdownOutput();
        }
    }

    public static void writeHtml(String filePath) throws Exception{
        //if (!"log/index.html".equals(filePath)){
            filePath="/Users/dylan/Desktop/index.html";
        //}
        FileInputStream fis = new FileInputStream(filePath);
        int len=0;
        byte[] b = new byte[1024];
        StringBuilder sb = new StringBuilder();
        //拼装http响应的数据格式
        sb.append("http/1.1 200 ok").append("\n\n");
        while ((len=fis.read(b))!=-1){
            sb.append(new String(b,0,len));
        }
        bw = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }
}
