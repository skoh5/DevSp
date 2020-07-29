package sp.io.socket.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import sp.io.socket.test.server.SpServer;
import sp.io.socket.test.vo.TwoNum;

public class SpMain {
	private static final int _DATA_CNT = 13;
	private static final int _DATA_MAX = 100;
	private static final int _CLIENT_MAX = 3;
	private static final int _REFILL_MAX = 3;
	private static final int _SEND_MAX = 2;
	
	private volatile boolean isRun = true; 
	private static final int serverPort = 5000;
	private List<TwoNum> listAll = new ArrayList<>();
	private SpServer server = null;
	private BlockingQueue<Integer> queueServerMsg = new ArrayBlockingQueue<>(100);
	private List<Socket> listClient = new ArrayList<>();
	private int sendIdx = 0;
	
	
	private void init() {
		Random r = new Random(System.currentTimeMillis());
		while(listAll.size() < _DATA_CNT) {
			listAll.add(new TwoNum(r.nextInt(_DATA_MAX), r.nextInt(_DATA_MAX)));
		}
		server = new SpServer(serverPort, queueServerMsg);
		new Thread(server).start();
		connectClient();
	}
	
	public void run() {
		init();
		List<TwoNum> listQueue = new ArrayList<>();
		refillQueue(listQueue);
		Collections.sort(listQueue);
		debugList(listQueue);
		int sendCnt = this.sendMsg(listQueue);
		Integer result = null;
		while(isRun) {
			try {
				result = queueServerMsg.poll(2,TimeUnit.SECONDS);
				if(result == null) {
					continue;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			debug("result: "+result);
			sendCnt--;
			if(sendCnt > 0) {
				continue;
			}
			refillQueue(listQueue);
			if(listQueue.size() == 0) {
				isRun = false;
				break;
			}
			Collections.sort(listQueue);
			debugList(listQueue);
			sendCnt = this.sendMsg(listQueue);
		}
		stop();
	}
    
	private void debug(String str) {
        System.out.println("[Main] "+str);
    }
	
	private void debugList(List<TwoNum> listQueue) {
		System.out.println(listQueue);
	}
	
	private void refillQueue(List<TwoNum> listQueue) {
		Iterator<TwoNum> iter = listAll.iterator();
		int i = 0;
		while(iter.hasNext()) {
			listQueue.add(iter.next());
			iter.remove();
			i++;
			if(i >= _REFILL_MAX) {
				break;
			}
		}
	}
    
	private int sendMsg(List<TwoNum> listQueue) {
		int sendCnt = 0;
		Iterator<TwoNum> iter = listQueue.iterator();
		while(iter.hasNext()) {
			this.sendMsg(iter.next().toPacketString());
			iter.remove();
			sendCnt++;
			if(sendCnt >= _SEND_MAX) {
				break;
			}
		}
		return sendCnt;
	}
	
	private void sendMsg(String msg) {
		Socket sock = listClient.get(sendIdx);
		try {	
			PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
			pw.println(msg);
			pw.flush();
			debug("send["+sock.getPort()+"] " +msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendIdx++;
		if(sendIdx >= _CLIENT_MAX) {
			sendIdx = 0;
		}
	}
	
	private void connectClient() {
		Socket sock = null;
		for(int i=5001;i<=5003;i++) {
			try {
				sock = new Socket("localhost", i);
				listClient.add(sock);
				debug("connect to "+i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void stop() {
		this.isRun = false;
		closeAll();
	}
	
	private void closeAll() {
		server.stop();
		debug("server stop");
		for(Socket sock: listClient) {
			try {
				debug("client close: "+sock.getPort());
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new SpMain().run();
	}
}
