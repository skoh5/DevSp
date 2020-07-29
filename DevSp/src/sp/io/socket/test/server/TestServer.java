package sp.io.socket.test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TestServer implements Runnable {
    private volatile boolean isRun = true;
    private int port;
    private ServerSocket serverSocket;
    private BlockingQueue<Integer> queue;
    private List<TestServerChild> listChild = new ArrayList<>();

    public TestServer(int port, BlockingQueue<Integer> queue) {
        this.port = port;
        this.queue = queue;
    }

    @Override
    public void run() {    	
        openServerSocket();
        TestServerChild child = null;
        while(isRun) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                debug("accept: "+clientSocket.getLocalPort());
                child = new TestServerChild(clientSocket, this.queue);
                listChild.add(child);
                new Thread(child).start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        debug("closed");
    }

    private void debug(String str) {
        System.out.println("[Server] "+str);
    }

    public synchronized void stop(){	
        this.isRun = false;
        try {
            this.serverSocket.close();
            //TODO: client socket close
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            debug("listen server: "+port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port: "+this.port, e);
        }
    }
}
