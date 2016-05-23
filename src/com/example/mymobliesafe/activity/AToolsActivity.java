package com.example.mymobliesafe.activity;

import com.example.engine.SmsEngine;
import com.example.engine.SmsEngine.SmsCallBack;
import com.example.mymobliesafe.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class AToolsActivity extends Activity {

	private ProgressDialog dialog;
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}
	
	public void smsBackups(View v) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SmsEngine.smsBaikeJson(AToolsActivity.this, new SmsCallBack() {
					
					@Override
					public void show() {
						// TODO Auto-generated method stub
						dialog.show();
						mProgressBar.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void setProgress(int progress) {
						// TODO Auto-generated method stub
						dialog.setProgress(progress);
						mProgressBar.setProgress(progress);
					}
					
					@Override
					public void setMax(int max) {
						// TODO Auto-generated method stub
						dialog.setMax(max);
						mProgressBar.setMax(max);
					}
					
					@Override
					public void end() {
						// TODO Auto-generated method stub
						dialog.dismiss();
						mProgressBar.setVisibility(View.GONE);
					}
				});	
				
				super.run();
			}
		}.start();
	
	}

	public void phoneQuery(View v) {
		Intent query = new Intent(AToolsActivity.this, PhoneLocationActivity.class);
		startActivity(query);
	}
	
	private void initView() {
		setContentView(R.layout.activity_atools);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_smsbeike_progress);
	}
}
