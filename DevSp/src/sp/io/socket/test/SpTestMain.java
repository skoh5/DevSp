package sp.io.socket.test;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import sp.io.socket.test.server.TestServer;

public class SpTestMain {
	private volatile boolean isRun = true; 
	private static final int clientPort = 5000;
	private Socket sock = null;
	//https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html
	private BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(100);
	private ExecutorService threadPool =
	        Executors.newFixedThreadPool(3);
	private PrintWriter pw = null;
	
	private void init() {
		listenServer();
		openClient();
	}
	
	public void run() {
		init();
		Integer result = 0;
		while(isRun) {
			try {
				result = queue.poll(2L, TimeUnit.SECONDS);
				if(result != null) {
					pw.println(String.valueOf(result));
					debug("send: "+result);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}
	
	private void debug(String str) {
		System.out.println("[Main] "+str);
	}
	
	private void listenServer() {
		this.threadPool.execute(new TestServer(5001, queue));
		this.threadPool.execute(new TestServer(5002, queue));
		this.threadPool.execute(new TestServer(5003, queue));
	}
	
	private void openClient() {
		try {
			sock = new Socket("localhost", clientPort);
			pw = new PrintWriter(sock.getOutputStream(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void stop() {
		this.isRun = false;
	}
	
	public static void main(String[] args) {
		new SpTestMain().run();
	}
}
