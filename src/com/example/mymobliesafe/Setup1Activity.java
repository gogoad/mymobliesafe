package com.example.mymobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends BaseSetupAcitivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void nextActivity() {
		
		startActivity(Setup2Activity.class);
	}

	@Override
	public void prevActivity() {
	
	}
	
}
