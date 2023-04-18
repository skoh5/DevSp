import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class MyServer {
	
	public static final String ATTR_NAME_PROXY = "proxy";
	private Proxy proxy;

	//https://creamilk88.tistory.com/87
	public MyServer(Proxy proxy) {
		this.proxy = proxy;
	}
	
	public void run() {
		Server server = new Server(proxy.getPort());
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.setAttribute(ATTR_NAME_PROXY, proxy);
		
		//https://stackoverflow.com/questions/30733910/whats-the-difference-between-a-servlethandler-and-a-servletcontexthandler-in-je
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
