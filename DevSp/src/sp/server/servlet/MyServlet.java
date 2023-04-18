import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;

public class MyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		doReq(req, res);			
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		doReq(req, res);
	}

	private void doReq(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Proxy proxy = (Proxy)getServletContext().getAttribute(MyServer.ATTR_NAME_PROXY);
		if(proxy == null) {
			res.setStatus(500);
			res.getWriter().print("Proxy info not found.");
			return;
		}
		
		HttpClient httpClient = new HttpClient();
		httpClient.setFollowRedirects(false);		
		ContentResponse proxyRes = null; 
		try {
			httpClient.start();
			Request proxyReq = makeRequest(httpClient, proxy, req);
			proxyRes = proxyReq.send();
			httpClient.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		res.setStatus(proxyRes.getStatus());
		res.setContentType(proxyRes.getMediaType());		
		res.getWriter().print(proxyRes.getContentAsString());		
	}

	private Request makeRequest(HttpClient client, Proxy proxy, HttpServletRequest req) {
		//String path = req.getPathInfo();
		String path = req.getServletPath();		
		String url = proxy.getUrl(path);
		
		System.out.println(req.getServletPath()+":"+req.getContextPath()+":"+req.getRequestURI()+":"+req.getPathInfo());
		if(req.getQueryString() != null) {
			path += "?" + req.getQueryString();
		}
		Request proxyReq = client.newRequest(url)						
				.method(req.getMethod())
				.path(path);
		return proxyReq;
	}
	
}
