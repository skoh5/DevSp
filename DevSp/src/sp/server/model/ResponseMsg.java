package jetty.server;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ResponseMsg {
	public static final String RST_OK = "Ok";

	
	@SerializedName("Result")
	private String result = RST_OK;
	
	
	@SerializedName("Message")
	private String msg = null;
	
	public ResponseMsg() {
		
	}
	public ResponseMsg(String result) {
		this.result = result;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "ResponseMsg [result=" + result + ", msg=" + msg + "]";
	}
	
	public String toJsonString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		//for debug
		ResponseMsg resMsg = new ResponseMsg(ResponseMsg.OK);
		resMsg.setMsg("Aha");
		
		System.out.println(resMsg.toJsonString());
		Gson gson = new Gson();
		Map map = gson.fromJson(resMsg.toJsonString(), Map.class);
		System.out.println(map);
	}
}
