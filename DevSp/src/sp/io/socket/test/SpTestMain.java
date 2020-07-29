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
                result = this.queue.poll(2L, TimeUnit.SECONDS);
                if(result != null) {
                    this.pw.println(String.valueOf(result));
                    debug("send: "+result);
                }
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
        this.threadPool.execute(new TestServer(5001, this.queue));
        this.threadPool.execute(new TestServer(5002, this.queue));
        this.threadPool.execute(new TestServer(5003, this.queue));
        debug("listen servers");
    }

    private void openClient() {
        boolean isConnected = false;
        while(isConnected == false) {
            try {
                this.sock = new Socket("localhost", clientPort);
                this.pw = new PrintWriter(this.sock.getOutputStream(), true);
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

    private void shutdown() {
        debug("Call shutdown");
        this.isRun = false;
    }

    public static void main(String[] args) {
        new SpTestMain().run();
    }
}
