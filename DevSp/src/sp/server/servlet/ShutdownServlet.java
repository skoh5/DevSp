package jetty.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ShutdownServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Main m;
	
	public ShutdownServlet(Main m) {
		super();
		this.m = m;
	}

	
	@Override
	protected void service(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
		RequestMsg reqMsg = getRequestMsg(request);		
		
		ResponseMsg repMsg = m.doOp(reqMsg);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
		response.getWriter().println(repMsg.toJsonString());
		m.setIsRun(false);
	}
	
	protected RequestMsg getRequestMsg(HttpServletRequest request) {
		RequestMsg reqMsg = new RequestMsg();
		reqMsg.setCmd(Main.CMD_SHUTDOWN);
		return reqMsg;
	}
}
