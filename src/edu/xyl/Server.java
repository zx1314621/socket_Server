package edu.xyl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final static String root = "/Users/dylan/Desktop/html";
    private final static int port = 8613;
    private final static int thread_nums = 100;
    private final static String language = "UTF-8";
    private static int num = 1;
    //private static BufferedWriter bw;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务器开启， 请求连接...");
        //ExecutorService executorService = Executors.newFixedThreadPool(thread_num);

        while (true) {


            Socket socket = serverSocket.accept();

            ExecutorService executorService = Executors.newFixedThreadPool(thread_nums);
          Runnable runnable = () -> {
              try {

                  BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                  System.out.println("connect success...");

                  String str = br.readLine();
                  int i = 0;
                  String path = root;
                  while (str != null && !str.equals("")) {
                      if (i++ == 0) {
                          //System.out.println(str);
                          String[] strs = str.split(" ");

                          if (strs[0].equals("GET")) {
                              if (strs[1].endsWith("html"))
                                  path = path + strs[1];
                          } else {
                              path = path +  "403.html";
                          }

                          writeHtml(path, socket);
                      }
                      str = br.readLine();
                  }

              }catch (Exception e){
                  e.printStackTrace();
              }
            };

            executorService.submit(runnable);
        }
    }

    public static void writeHtml(String filePath, Socket socket) throws IOException{
        if (!"/Users/dylan/Desktop/html/index.html".equals(filePath) && !"/Users/dylan/Desktop/html/403.html".equals(filePath)) {
            filePath = "/Users/dylan/Desktop/html/404.html";
        }

        FileInputStream fis = new FileInputStream(filePath);
        int len = 0;
        byte[] b = new byte[1024];
        StringBuilder sb = new StringBuilder();
        sb.append("http/1.1 200 ok").append("\n\n");
        while ((len=fis.read(b))!=-1){
            sb.append(new String(b,0,len));
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }
}
