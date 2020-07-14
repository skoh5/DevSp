package sp.io.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * https://www.baeldung.com/a-guide-to-java-sockets
 */
public class SocketMain {

    //바이너리 데이터 처리
    //http://tutorials.jenkov.com/java-io/datainputstream.html
    //http://tutorials.jenkov.com/java-io/dataoutputstream.html
    public void run(String ip, int port) {
        try (
                Socket socket = new Socket(ip, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ){
            out.println("hello server");
            out.flush();
            String resp = in.readLine();
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String ip = "localhost";
        int port = 55555;
        SocketMain m = new SocketMain();
        m.run(ip, port);
    }
}
