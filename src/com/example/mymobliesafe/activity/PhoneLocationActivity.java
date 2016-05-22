package com.example.mymobliesafe.activity;

import com.example.engine.PhoneLocationEngine;
import com.example.mymobliesafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneLocationActivity extends Activity {

	private EditText mEtnumber;
	private Button mQuery;
	private TextView mAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		initEvent();
	}

	private void initEvent() {
		// 给editText加文本变化事件
		mEtnumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// 文本改变
				locationQuery();

			}
		});
		mQuery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationQuery();
			}

		});

	}

	// 归属地查询事件封装
	protected void locationQuery() {
		String phone = mEtnumber.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			// 添加一个抖动事件
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.shake);
			mEtnumber.startAnimation(animation);
			// 震动的效果
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			// 震动的参数设置
			vibrator.vibrate(new long[] { 200, 300, 300, 200, 500, 100 }, 3);

			mAddress.setText("归属地：");
			return;

		}
		// 查询
		String location = PhoneLocationEngine.locationQuery(phone,
				getApplicationContext());
		mAddress.setText("归属地：" + location);

	}

	private void initView() {
		setContentView(R.layout.activity_phonelocation);
		mEtnumber = (EditText) findViewById(R.id.et_phonelocation_number);
		mQuery = (Button) findViewById(R.id.bt_phonelocation_query);
		mAddress = (TextView) findViewById(R.id.tv_phonelocation_address);
	}
}
