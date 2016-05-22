package com.example.mymobliesafe.activity;

import com.example.mymobliesafe.R;
import com.example.utils.EncryptTools;
import com.example.utils.MyConstants;
import com.example.utils.SpTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupAcitivity {

	private EditText mSafenumber;

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
		mSafenumber = (EditText) findViewById(R.id.et_setup3_safenumber);
	}

	@Override
	public void initData() {
		String safenumber = SpTools.getString(getApplicationContext(), MyConstants.SAFENUMBER, "");
		mSafenumber.setText(safenumber);
		super.initData();
	}
	
	@Override
	public void initEvent() {
		
		super.initEvent();
	}

	public void selectSafeNumber(View v) {
		Intent intent = new Intent(Setup3Activity.this, FriendsActivity.class);
		startActivityForResult(intent, 1);//启动显示好友界面
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			//用户选择数据来关闭联系人界面,而不是直接点击返回按钮
			//取数据
			String number = data.getStringExtra(MyConstants.SAFENUMBER);
			mSafenumber.setText(number);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void next(View v) {
		String number = mSafenumber.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(getApplicationContext(), "请先选择或输入安全号码", Toast.LENGTH_LONG).show();
			//不调用父类的方法进行页面切换
			return;
		}else{
			//对安全号码加密
			//number = EncryptTools.encrypt(MyConstants.MUSIC, number);
			//保存安全号码
			SpTools.putString(getApplicationContext(), MyConstants.SAFENUMBER, number);
		}
		super.next(v);
	}

	@Override
	public void nextActivity() {
		startActivity(Setup4Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup2Activity.class);
	}
}
