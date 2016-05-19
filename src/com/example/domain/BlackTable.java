package com.example.domain;

public interface BlackTable {

	String PHONE = "phone";//黑名单号码
	String MODE = "mode";//黑名单拦截模式
	String BLACKTABLE = "blacknumber";//黑名单表名
	
	int SMS = 1 << 0;
	int TEL = 1 << 1;
	int ALL = SMS | TEL;
}
