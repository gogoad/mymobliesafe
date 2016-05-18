package com.example.mymobliesafe;

import android.app.Activity;
import android.os.Bundle;

public class SettingCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
	}
}
