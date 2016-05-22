package com.example.mymobliesafe.activity;

import com.example.mymobliesafe.R;
import com.example.service.ComingPhoneService;
import com.example.service.TelSmsBlackService;
import com.example.utils.MyConstants;
import com.example.utils.ServiceUtils;
import com.example.utils.SpTools;
import com.example.view.SettingCenterItemView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingCenterActivity extends Activity {

	private SettingCenterItemView mScivUpdate;
	private SettingCenterItemView mScivBlack;
	private SettingCenterItemView sciv_phoneLocationService;
	private RelativeLayout rl_style_root;
	private TextView tv_locationStyle_content;
	private CheckBox iv_changeStyle2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		initData();
		initEvent();
	}

	private String[] styleNames = new String[] { "卫士蓝", "金属灰", "苹果绿", "活力橙",
			"半透明" };
	private AlertDialog dialog;
	private void showStyleDialog() {
		// 对话框让用户选择样式
		AlertDialog.Builder ab = new AlertDialog.Builder(
				SettingCenterActivity.this);
		ab.setTitle("选择归属地样式");
		ab.setSingleChoiceItems(styleNames, Integer.parseInt(SpTools.getString(
				getApplicationContext(), MyConstants.STYLEBGINDEX, "0")),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// which 点击的位置
						// 保存sp中 字符串的方式保存归属地样式
						SpTools.putString(getApplicationContext(),
								MyConstants.STYLEBGINDEX, which + "");
						tv_locationStyle_content.setText(styleNames[which]);
						dialog.dismiss();// 关闭对话框
					}
				});

		dialog = ab.create();
		dialog.show();
	}

	private void initEvent() {
		mScivUpdate.setItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mScivUpdate.setChecked(!mScivUpdate.isChecked());
				// 保存复选框的状态
				SpTools.putBoolean(getApplicationContext(),
						MyConstants.AUTOUPDATE, mScivUpdate.isChecked());
			}
		});
		
		//箭头的点击事件
		iv_changeStyle2.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View v) {
				//改变复选框的状态
				iv_changeStyle2.setChecked(!iv_changeStyle2.isChecked());
				showStyleDialog();
				
			}			
		});
		
		// 来电显示归属地服务启动或关闭
				sciv_phoneLocationService.setItemClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// 判断黑名单拦截服务是否运行
						if (ServiceUtils.isServiceRunning(getApplicationContext(),
								"com.example.service.ComingPhoneService")) {
							// 服务在运行,关闭服务
							Intent comingPhoneService = new Intent(
									SettingCenterActivity.this,
									ComingPhoneService.class);
							stopService(comingPhoneService);
							// 设置复选框的状态
							sciv_phoneLocationService.setChecked(false);
						} else {
							// 服务停止,打开服务
							Intent comingPhoneService = new Intent(
									SettingCenterActivity.this,
									ComingPhoneService.class);
							startService(comingPhoneService);
							// 设置复选框的状态
							sciv_phoneLocationService.setChecked(true);
						}
					}
				});
		
		// 黑名单服务启动或关闭
		mScivBlack.setItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ServiceUtils.isServiceRunning(getApplicationContext(),
						"com.example.service.TelSmsBlackService")) {
					// 服务正在运行,关闭服务
					Intent intent = new Intent(SettingCenterActivity.this,
							TelSmsBlackService.class);
					stopService(intent);
					mScivBlack.setChecked(false);
				} else {
					Intent intent = new Intent(SettingCenterActivity.this,
							TelSmsBlackService.class);
					startService(intent);
					mScivBlack.setChecked(true);
				}
			}
		});
		//
	}

	private void initData() {
		mScivUpdate.setChecked(SpTools.getBoolean(getApplicationContext(),
				MyConstants.AUTOUPDATE, false));

		// 判断黑名单服务，来设置复选框的初始值
		mScivBlack.setChecked(ServiceUtils.isServiceRunning(
				getApplicationContext(),
				"com.example.service.TelSmsBlackService"));

		// 判断来电归属地服务，来设置复选框的初始值
		sciv_phoneLocationService.setChecked(ServiceUtils.isServiceRunning(
				getApplicationContext(),
				"com.example.service.ComingPhoneService"));

	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
		mScivUpdate = (SettingCenterItemView) findViewById(R.id.sciv_setting_center_autoupdate);
		mScivBlack = (SettingCenterItemView) findViewById(R.id.sciv_black);

		sciv_phoneLocationService = (SettingCenterItemView) findViewById(R.id.sciv_setting_center_phonelocationservice);

		rl_style_root = (RelativeLayout) findViewById(R.id.rl_settingcenter_locationsytle_root);

		tv_locationStyle_content = (TextView) findViewById(R.id.tv_settingcenter_locationsytle_content);

		iv_changeStyle2 = (CheckBox) findViewById(R.id.iv_settingcenter_locationsytle_select);
	}
}
