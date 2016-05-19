package com.example.mymobliesafe.test;

import java.util.List;

import com.example.dao.BlackNumberDao;
import com.example.domain.BlackBean;
import com.example.domain.BlackTable;

import android.test.AndroidTestCase;

public class MyTest extends AndroidTestCase {
	public void testAddBlackNumber(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		for (int i = 0; i < 200;i++){
			dao.add("1234567" + i, BlackTable.SMS);
		}
	}
	
	public void testFindAllBlackDatas(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		//获取所有黑名单数据
		List<BlackBean> datas = dao.getAllDatas();
		System.out.println(datas);
	}
}
