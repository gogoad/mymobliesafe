package com.example.utils;

/**
 * @author Administrator
 * ����json��ʽ�����ַ�
 */
public class JsonStrTools {
	/**
	 * @param json
	 *   json���ַ�
	 * @return
	 *   ��json�����ַ�����ת������ 
	 */
	public static String changeStr(String json){
		json = json.replaceAll(",", "��");
		json = json.replaceAll(":", "��");
		json = json.replaceAll("\\[", "��"); 
		json = json.replaceAll("\\]", "��"); 
		json = json.replaceAll("\\{", "<"); 
		json = json.replaceAll("\\}", ">"); 
		json = json.replaceAll("\"", "��"); 
		
		return json.toString();
	}
}
