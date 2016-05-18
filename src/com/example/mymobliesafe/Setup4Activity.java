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
		if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.example.service.LostFindService")) {
			
			mCbProtect.setChecked(true);
			mCbProtect.setText("防盗保护已开启");
		}else {
			mCbProtect.setChecked(false);
			mCbProtect.setText("防盗保护已关闭");
		}
		super.initData();
	}

	@Override
	public void initEvent() {
		mCbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					System.out.println("check true");
					mCbProtect.setText("防盗保护已开启");
					Intent intent = new Intent(Setup4Activity.this, LostFindService.class);
					startService(intent);
				}else {
					System.out.println("check false");
					mCbProtect.setText("防盗保护已关闭");
					Intent intent = new Intent(Setup4Activity.this, LostFindService.class);
					stopService(intent);
				}
			}
		});
		super.initEvent();
	}

	@Override
	public void nextActivity() {
		SpTools.putBoolean(getApplicationContext(), MyConstants.ISSETUP, true);
		startActivity(LostFindActivity.class);
		finish();
	}

	@Override
	public void prevActivity() {
		startActivity(Setup3Activity.class);
	}
}
