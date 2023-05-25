import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class MyServer2 {
	//https://creamilk88.tistory.com/87
	public MyServer() {
	}
	
	public void run() {
		//Server server = new Server(proxy.getPort());
		Server server = new Server();
	    ServerConnector http = new ServerConnector(server);
	    http.setHost("127.0.0.1");
	    http.setPort(proxy.getPort());
	    server.addConnector(http);
	    
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.setAttribute(ATTR_NAME_PROXY, proxy);
		context.setAttribute(ATTR_NAME_HISTORY, mapHistory);
		
		//https://stackoverflow.com/questions/30733910/whats-the-difference-between-a-servlethandler-and-a-servletcontexthandler-in-je
		//context.addServlet(TraceServlet.class, "/trace");
		context.addServlet(MyServlet.class, "/");
		server.setHandler(context);
				
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
