package com.example.mymobliesafe;

import com.example.service.LostFindService;
import com.example.utils.MyConstants;
import com.example.utils.ServiceUtils;
import com.example.utils.SpTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupAcitivity {

	private CheckBox mCbProtect;

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);
		mCbProtect = (CheckBox) findViewById(R.id.cb_setup4_isprotected);
	}
	
	@Override
	public void initData() {
		//初始化复选框的值 看服务是否开启
		//如果服务开启，打钩，否则不打钩
		if (ServiceUtils.isServicesRunning(getApplicationContext(), "com.example.service.LostFindService")) {
			mCbProtect.setChecked(true);
			mCbProtect.setText("防盗保护已开启");
		}else {
			mCbProtect.setChecked(false);
			mCbProtect.setText("防盗保护未开启");
		}
		super.initData();
	}

	@Override
	public void initEvent() {
		mCbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mCbProtect.setText("防盗保护已开启");
					Intent service = new Intent(Setup4Activity.this,LostFindService.class);
					//启动防盗保护的服务
					startService(service);
				}else {
					mCbProtect.setText("防盗保护未开启");
					Intent service = new Intent(Setup4Activity.this,LostFindService.class);
					//启动防盗保护的服务
					stopService(service);
				}
			}
		});
		super.initEvent();
	}

	@Override
	public void nextActivity() {
		SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, true);
		startActivity(LostFindActivity.class);
		finish();
	}

	@Override
	public void prevActivity() {
		startActivity(Setup3Activity.class);
	}
}
