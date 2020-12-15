package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;

import net.sf.json.JSONObject;


/*
 * Json ������
 * ����GUI���򴫹�����json�ַ���, ����json�����ݵ���Client����ķ���
 * ���Ұ�ִ�еĽ��������һ���µ�json��, ���ظ�������
 */
public class JsonParser {

	private Client client;
	public JsonParser(Client client) {
		this.client = client;
	}
	
	public JSONObject Parse(JSONObject jo) {
		JSONObject response = null;
		String type = jo.getString("type");//��ȡ���������
		//��¼
		if("login".equals(type)) {
			response = login(jo);
		//����
		}else if("cd".equals(type)) {
			response = cd(jo);			
		}else if("md".equals(type)) {
			
		}else if("download".equals(type)) {
			response = download(jo);
		}else if("upload".equals(type)) {
			
		}else if("delete".equals(type)) {
			
		}else if("rename".equals(type)) {
			
		}else {
			//GUI�������޷�ʶ�������
		}
		return response;//����ִ�еĽ��
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
			response.put("status", 0);//0��ʾʧ��
			return response;
		}
		response.put("status", 1);//1��ʾ�ɹ�
		return response;
	}
	
	
	private JSONObject cd(JSONObject jo) {
		JSONObject response = new JSONObject();
		FTPFile[] ftpFiles = null;
		String ftpPath = jo.getString("ftpPath");
		try {
			ftpFiles = client.cd(ftpPath);//ת������Ŀ¼��, ����ȡĿ¼�е�����
		} catch (IOException e) {
			response.put("status", 0);//ʧ��
			return response;
		}
		response.put("status", 1);//�ɹ�
		//��Ŀ¼�е����ݼ��뵽response��
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
