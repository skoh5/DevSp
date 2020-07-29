package sp.io.socket.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import sp.io.socket.test.server.TestServer;

public class SpTestMain {
    private volatile boolean isRun = true;
    private static final int clientPort = 5000;
    private Socket sock = null;

    //https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html
    private BlockingQueue<Integer> queueServerMsg = new ArrayBlockingQueue<>(100);
    private List<TestServer> listServer = new ArrayList<>();
    private PrintWriter pwClient = null;

    private void init() {
        listenServer();
        openClient();
    }

    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });
        init();
        Integer result = 0;
        while(this.isRun) {
            try {
                result = this.queueServerMsg.poll(2L, TimeUnit.SECONDS);
                if(result == null) {
                	continue;
                }
                this.pwClient.println(String.valueOf(result));
                debug("send: "+result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        debug("Shutdown done");
    }

    private void debug(String str) {
        System.out.println("[Main] "+str);
    }

    private void listenServer() {
    	TestServer server = null;    	
    	for(int i=5001;i<=5003;i++) {
    		server = new TestServer(i, this.queueServerMsg);
    		new Thread(server).start();    		
    		listServer.add(server);
    	}
        debug("listen servers");
    }
    
    private void stopServer() {
    	for(TestServer server: listServer) {
    		server.stop();
    	}
    }

    private void openClient() {
        boolean isConnected = false;
        while(isConnected == false) {
            try {
                this.sock = new Socket("localhost", clientPort);
                this.pwClient = new PrintWriter(this.sock.getOutputStream(), true);
            } catch (Exception e) {
                debug("Client connect fail: "+e.toString());
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e1) {
                }
                continue;
            }
            debug("Client connect success");
            isConnected = true;
        }
    }
    
    private void closeClient() {
    	 if(pwClient != null) {
         	pwClient.close();
         }
         if(sock != null) {
         	try {
 				sock.close();
 			} catch (IOException e) {
 			}
         }
    }

    private void shutdown() {
        debug("Call shutdown");
        stopServer();
        closeClient();
        this.isRun = false;
    }

    public static void main(String[] args) {
        new SpTestMain().run();
    }
}
