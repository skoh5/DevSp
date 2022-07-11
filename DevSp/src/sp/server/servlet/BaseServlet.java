package jetty.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public abstract class BaseServlet extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("rawtypes")
	protected Map getBody(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		try (BufferedReader br = request.getReader()) {
			String buf = "";
			while( (buf = br.readLine()) != null) {
				sb.append(buf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		return gson.fromJson(sb.toString(), Map.class);
	}
	
	
	protected abstract RequestMsg getRequestMsg(HttpServletRequest request);
}
