package sp.io.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * https://www.baeldung.com/a-guide-to-java-sockets
 */
public class ServerSocketMultiMain {
    private boolean bRun = true;

    public void run(int port) {
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                ) {
            while(this.bRun) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("connected: "+clientSocket);
                new ClientHandler(clientSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(this.clientSocket.getInputStream()));
                    ) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    out.println(inputLine);
                }
            } catch (Exception e) {

            } finally {
                try {
                    System.out.println("closed: "+this.clientSocket);
                    this.clientSocket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        int port = 55555;
        ServerSocketMultiMain m = new ServerSocketMultiMain();
        m.run(port);
    }
}
