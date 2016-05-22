package com.example.mymobliesafe.activity;

import com.example.mymobliesafe.R;
import com.example.utils.MyConstants;
import com.example.utils.SpTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 设置向导二页面的作用主要是用来绑定SIM卡的。手机每次重启时会去检
 *	查存储在手机本地的SIM卡序列号是否与当前SIM卡序列号一致，若不一致则SI
 *	M卡被更换。
 */
public class Setup2Activity extends BaseSetupAcitivity {
	
	private Button mBtBind;
	private ImageView mIvIsbind;

	
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup2);
		//获取绑定sim卡按钮
		mBtBind = (Button) findViewById(R.id.bt_setup2_bindsim);
		//获取绑定sim卡图标
		mIvIsbind = (ImageView) findViewById(R.id.iv_setup2_isbind);
		//获取sim卡序列号信息
		String mSim = SpTools.getString(getApplicationContext(), MyConstants.SIM, "");
		if (TextUtils.isEmpty(mSim)) {
			//未绑定
			mIvIsbind.setImageResource(R.drawable.unlock);
		}else {
			//已绑定
			mIvIsbind.setImageResource(R.drawable.lock);
		}
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {
		/**
		 * 绑定SIM卡的原理很简单，首先通过getSystemService(TELEPHONY_SERVI
		 *	CE)，获取TelephoneManager对象，然后通过该对象的getSimSerialNumber方法
		 *	获取SIM卡的序列号。获取SIM卡序列号后我们将该序列号保存在SharedPrefere
		 *	nces中即可
		 */
		mBtBind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//绑定和解绑sim卡
				String mSim = SpTools.getString(getApplicationContext(), MyConstants.SIM, "");
				if (TextUtils.isEmpty(mSim)) {
					//绑定sim卡信息 
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					//获取sim卡序列号
					String serialNumber = tm.getSimSerialNumber();
					SpTools.putString(getApplicationContext(), MyConstants.SIM, serialNumber);
					mIvIsbind.setImageResource(R.drawable.lock);
				}else {
					SpTools.putString(getApplicationContext(), MyConstants.SIM, "");
					mIvIsbind.setImageResource(R.drawable.unlock);
				}
			}
		});
		// TODO Auto-generated method stub
		super.initEvent();
	}
	
	@Override
	public void next(View v) {
		String mSim = SpTools.getString(getApplicationContext(), MyConstants.SIM, "");
		if (TextUtils.isEmpty(mSim)) {
			Toast.makeText(getApplicationContext(), "请先绑定sim卡", Toast.LENGTH_LONG).show();
			//不调用父类的方法进行页面切换
			return;
		}
		super.next(v);
	}
	
	@Override
	public void nextActivity() {
		startActivity(Setup3Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup1Activity.class);
	}

}
