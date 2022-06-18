import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ServerLink {
    private final Socket socket;//和代理服务器的套接字连接
    public BufferedReader bufferedReader;//输入流
    private BufferedWriter bufferedWriter;//输出流
    private final String host;//主机名
    private final String fileUrl;//文件url
    private final String filename;//保存的文件名

    public ServerLink(String host,int port) throws IOException {
        //提示用户输入要获取的url
        System.out.println("Please input the url of http request, no need to add http://");
        Scanner scan = new Scanner(System.in);
        String url;
        url = scan.next();
        //解析URL,得到请求的主机名和请求的文件url
        String[] split = url.split("/", 2);
        this.filename = url.replace("/", "#");
        this.host = split[0];
        this.fileUrl = "/" + split[1];
        //建立和代理服务器的连接
        socket = new Socket(host, port);
        //使用缓存区存放收到的消息
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void Get() throws IOException {
        //准备写数据缓存区，发送一条常规GET请求
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        bufferedWriter.write("GET " + this.fileUrl + " HTTP/1.1\r\n" + "Host: " + this.host + "\r\n\r\n");
        bufferedWriter.flush();
    }

    public void close() throws IOException {//关闭连接
        socket.close();
        bufferedWriter.close();
        bufferedReader.close();
    }

    public void saveFile() throws IOException {
        String info;
        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
        //展示消息头
        do  {
            info = bufferedReader.readLine();
            System.out.println(info);
        } while (!info.isEmpty());//空行时说明消息头结束
        //写入获取的文件
        while ((info = bufferedReader.readLine()) != null) {
            fileWriter.write(info + "\r\n");
        }
        fileWriter.close();
        System.out.println("File is saved in "+this.filename);
    }
}