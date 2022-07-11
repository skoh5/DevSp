package jetty.server;

import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class Main {
	
	public static final String CMD_CREATE = "CREATE";
	public static final String CMD_SHUTDOWN = "SHUTDOWN";
	
	private Server server = null;
	private boolean isRun = true;
	
	public void run() {
//		qm.startTimeoutHandler();
		server = createServer(8080);		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected void shutdownServer() {
		try {
			Thread t = new ShutdownHandler(this);
			t.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void stopServer() {
		try {
			System.out.println("Shutdown server start");
			server.stop();	
			System.out.println("Shutdown server end");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setIsRun(boolean isRun) {
		this.isRun = isRun;
	}
	public boolean getIsRun() {
		return this.isRun;
	}
	
	private Server createServer(int port) {
		Server server = new Server(port);
		
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		
		CreateServlet createServlet = new CreateServlet(this);
		ServletHolder createHolder = new ServletHolder(createServlet);
		context.addServlet(createHolder, createServletPath(CMD_CREATE));			
		
		ShutdownServlet shutdownServlet = new ShutdownServlet(this);
		ServletHolder shutdownHolder = new ServletHolder(shutdownServlet);
		context.addServlet(shutdownHolder, createServletPath(CMD_SHUTDOWN));
		
		server.setHandler(context);
			
		return server;
	}
	
	private String createServletPath(String cmd) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("/").append(cmd).append("/*");
		return strBuf.toString();
	}
	
	private static class ShutdownHandler extends Thread {		
		private Main m = null;
		
		public ShutdownHandler(Main main) {
			super();
			this.m = main;
		}
		
		@Override
		public void run() {
			System.out.println("Shutdown Thread start!!");
			while(m.getIsRun()) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
				}
			}
			m.stopServer();
			System.out.println("Shutdown Thread end!!");
		}
	}
	/* 참고 링크
	 * 
	 * https://www.eclipse.org/jetty/documentation/jetty-9/index.html#jetty-helloworld
	 * https://stackoverflow.com/questions/39421686/jetty-pass-object-from-main-method-to-servlet
	 */
}
