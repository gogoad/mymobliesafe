package com.example.mymobliesafe;

import com.example.utils.MyConstants;
import com.example.utils.SpTools;
import com.example.view.SettingCenterItemView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingCenterActivity extends Activity {

	private SettingCenterItemView sciv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		sciv.setItemClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sciv.setChecked(!sciv.isChecked());
				//保存复选框的状态
				SpTools.putBoolean(getApplicationContext(), MyConstants.AUTOUPDATE, sciv.isChecked());
			}
		});
	}

	private void initData() {
		sciv.setChecked(SpTools.getBoolean(getApplicationContext(),MyConstants.AUTOUPDATE, false));
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
		sciv = (SettingCenterItemView) findViewById(R.id.sciv_setting_center_autoupdate);
	}
}
