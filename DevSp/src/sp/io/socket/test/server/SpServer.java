package sp.io.socket.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SpServer implements Runnable {
	private volatile boolean isRun = true;
    private int port;
    private ServerSocket serverSocket;
    private BlockingQueue<Integer> queue;
    private Socket sock = null;
    
    
    public SpServer(int port, BlockingQueue<Integer> queue) {
    	this.port = port;
    	this.queue = queue;
    }
    
	@Override
	public void run() {
		openServerSocket();
		String buf = null;
		while(isRun) {
			try (
					Socket sock = this.serverSocket.accept();
					BufferedReader in = new BufferedReader(
	                        new InputStreamReader(sock.getInputStream()));
					) {
				this.sock = sock;
				debug("accept: "+sock.getLocalPort());
				while((buf = in.readLine()) != null) {
					queue.add(Integer.parseInt(buf));
				}
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
        	this.sock.close();
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
