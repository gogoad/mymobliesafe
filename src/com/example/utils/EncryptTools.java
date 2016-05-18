package com.example.utils;

public class EncryptTools {
	
	public static String encrypt(int seed,String str){
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] ^= seed;
		}
		return new String(bytes);
	}
	
	public static String decryption(int seed,String str){
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] ^= seed;
		}
		return new String(bytes);
	}
}
