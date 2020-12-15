package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;

import net.sf.json.JSONObject;


/*
 * Json 解释器
 * 接受GUI程序传过来的json字符串, 按照json的内容调用Client对象的方法
 * 并且把执行的结果保存在一个新的json中, 返回给调用者
 */
public class JsonParser {

	private Client client;
	public JsonParser(Client client) {
		this.client = client;
	}
	
	public JSONObject Parse(JSONObject jo) {
		JSONObject response = null;
		String type = jo.getString("type");//获取请求的类型
		//登录
		if("login".equals(type)) {
			response = login(jo);
		//下载
		}else if("cd".equals(type)) {
			response = cd(jo);			
		}else if("md".equals(type)) {
			
		}else if("download".equals(type)) {
			response = download(jo);
		}else if("upload".equals(type)) {
			
		}else if("delete".equals(type)) {
			
		}else if("rename".equals(type)) {
			
		}else {
			//GUI发来了无法识别的请求
		}
		return response;//返回执行的结果
	}
	
	private JSONObject login(JSONObject jo) {
		JSONObject response = new JSONObject();
		String ip = jo.getString("ip");
		int port = jo.getInt("port");
		String username = jo.getString("username");
		String password = jo.getString("password");
		try {
			client.login(ip, port, username, password);
		} catch (IOException e) {
			response.put("status", 0);//0表示失败
			return response;
		}
		response.put("status", 1);//1表示成功
		return response;
	}
	
	
	private JSONObject cd(JSONObject jo) {
		JSONObject response = new JSONObject();
		FTPFile[] ftpFiles = null;
		String ftpPath = jo.getString("ftpPath");
		try {
			ftpFiles = client.cd(ftpPath);//转到给定目录下, 并获取目录中的内容
		} catch (IOException e) {
			response.put("status", 0);//失败
			return response;
		}
		response.put("status", 1);//成功
		//把目录中的内容加入到response中
		List<String> folders = new ArrayList<String>();
		List<String> files = new ArrayList<String>();
		for(FTPFile f:ftpFiles) {
			if(f.isDirectory()) {
				folders.add(f.getName());
			}else if(f.isFile()) {
				files.add(f.getName());
			}
		}
		response.put("folders",folders);
		response.put("files",files);
		return response;
	}
	
	private JSONObject download(JSONObject jo) {
		JSONObject response = new JSONObject();
		String ftpPath = jo.getString("ftpPath");
		String localPath = jo.getString("localPath");
		try {
			client.download(ftpPath, localPath);
		} catch (IOException e) {
			response.put("status", 0);
			return response;
		}
		response.put("status", 1);
		return response;
	}
}
