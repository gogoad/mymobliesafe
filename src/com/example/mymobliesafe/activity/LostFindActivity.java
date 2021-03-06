package com.example.mymobliesafe.activity;

import com.example.mymobliesafe.R;
import com.example.utils.MyConstants;
import com.example.utils.SpTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
/**
 * 手机防盗主要是通过用户更换SIM卡时程序自动给我们的安全号码发送短
 *	信、发送位置信息等功能获取手机当前的位置、 SIM卡等信息。同时我们也可
 *	以通过其他手机给我们的手机发送短信指令，根据不同的指令安全卫士会做一
 *	些锁屏、删除数据、恢复出厂设置等操作以保护我们手机数据的安全。
 * @author jxa
 *
 */
public class LostFindActivity extends Activity {
	private TextView mSafePhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//如果第一次访问该界面，要先进入设置向导界面
		
		// 进入过设置向导界面，直接显示本界面
		if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)) {
			
			initView();
			initData();
		}else {
			
			//进入设置向导页面
			Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		
	}

	private void initData() {
		String safeNumber = SpTools.getString(getApplicationContext(), MyConstants.SAFENUMBER, "");
		mSafePhone.setText(safeNumber);
	}

	private void initView() {
		setContentView(R.layout.activity_lostfind);
		mSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
		
	}
	
	public void reEnter(View v) {
		Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
		startActivity(intent);
	}
}
