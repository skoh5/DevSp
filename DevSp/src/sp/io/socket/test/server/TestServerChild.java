package sp.io.socket.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class TestServerChild implements Runnable {

	private volatile boolean isRun = true;
	private Socket sock;
	private BlockingQueue<Integer> queue;
	
	public TestServerChild(Socket sock, BlockingQueue<Integer> queue) {
		this.sock = sock;
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try (
				BufferedReader in = new BufferedReader(
                        new InputStreamReader(sock.getInputStream()));
				) {
			String buf = null;
			String[] arr = null;
			int result = 0;
			while(isRun) {
				buf = in.readLine();
				if(buf == null) {
					Thread.sleep(2000L);
					continue;
				}
				debug("read:"+buf);
				arr = buf.split(",");
				result = Integer.parseInt(arr[0])+Integer.parseInt(arr[1]);
				debug("calc: "+arr[0]+"+"+arr[1]+"="+result);
				queue.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				debug("closed");
				sock.close();
			} catch (IOException e) {
			}
		}
	}
	
	private void debug(String str) {
		System.out.println("[Client-"+sock.getLocalPort()+"] "+str);
	}
	
	public synchronized void close() {
		this.isRun = false;
		try {
			if(sock != null) {
				sock.close();
			}
		} catch (IOException e) {
		}
	}
}
