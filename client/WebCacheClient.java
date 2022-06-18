import java.util.Scanner;

public class WebCacheClient {
    public static void main(String[] args){
        try {
            //交互获得代理服务器所在的地址/主机名
            System.out.println("Please input the address or host name where the cache server on");
            Scanner scan = new Scanner(System.in);
            String host = scan.next();
            int port = 0;
            //获得正确的端口
            while (port>65536||port<1024){
                try {
                    System.out.println("Please input the port where the server run");
                    port = scan.nextShort();
                    if (port<1024) {
                        System.out.println("Port can not be a Well Known Port");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //设置退出符号
            char quit = ' ';
            while (true) {
                //和代理服务器建立连接
                ServerLink link = new ServerLink(host, port);
                //发起一个Get请求
                link.Get();
                //保存文件
                link.saveFile();
                //关闭连接
                link.close();
                //询问用户是否要推出
                System.out.println("q for quit, others to go on");
                quit = scan.next().charAt(0);
                if (quit=='q'){
                    return;
                }
            }
            //继续或退出
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}