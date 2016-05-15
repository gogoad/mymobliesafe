package com.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	public static String md5(String str){
		StringBuilder mess = new StringBuilder();
		try {
		
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = str.getBytes();
			byte[] digest = md.digest(bytes);
			
			for (byte b : digest){
				
				int d = b & 0xff;
				String hexString = Integer.toHexString(d);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				mess.append(hexString);
				
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mess + "";
	}
}
