public class MyClient {
  
  private void doReq() {
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
