package sp.io.socket.test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestServer implements Runnable {
	private volatile boolean isStopped = true;
	private int port;
	private ServerSocket serverSocket;
	private BlockingQueue<Integer> queue;
	private ExecutorService threadPool =
	        Executors.newFixedThreadPool(10);
	
	public TestServer(int port, BlockingQueue<Integer> queue) {
		this.port = port;
		this.queue = queue;
	}

	@Override
	public void run() {
		this.openServerSocket();
		while(isStopped() == false) {
			try {
				Socket clientSocket = serverSocket.accept();
				debug("accept: "+clientSocket.getLocalPort());
				this.threadPool.execute(
		                new TestServerChild(clientSocket, queue));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		this.threadPool.shutdown();
		debug("close");
	}
	
	private void debug(String str) {
		System.out.println("[Server] "+str);
	}
	
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port: "+port, e);
        }
    }
}
