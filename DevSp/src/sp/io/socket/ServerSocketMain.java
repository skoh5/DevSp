package sp.io.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * https://www.baeldung.com/a-guide-to-java-sockets
 */
public class ServerSocketMain {
    public void run(int port) {
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
            String greeting = in.readLine();
            if ("hello server".equals(greeting)) {
                out.println("hello client");
                out.flush();
            } else {
                out.println("unrecognised greeting");
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 55555;
        ServerSocketMain m = new ServerSocketMain();
        m.run(port);
    }
}
