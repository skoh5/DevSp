package sp.io.socket.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sp.io.socket.test.vo.TwoNum;

public class SpMain {
	private static final int _DATA_CNT = 13;
	private static final int _DATA_MAX = 100;
	
	private volatile boolean isRun = true; 
	private static final int serverPort = 5000;
	private List<TwoNum> listAll = new ArrayList<>();
	
	private void init() {
		Random r = new Random(System.currentTimeMillis());
		while(listAll.size() < _DATA_CNT) {
			listAll.add(new TwoNum(r.nextInt(_DATA_MAX), r.nextInt(_DATA_MAX)));
		}
	}
	
	public void run() {
		List<TwoNum> listQueue = new ArrayList<>();
		while(isRun) {
			
		}
	}
	
	private void stop() {
		this.isRun = false;
	}
	
	public static void main(String[] args) {
		new SpMain().run();
	}
}
