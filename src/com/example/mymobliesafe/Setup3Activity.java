package com.example.mymobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup3Activity extends BaseSetupAcitivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
	}

	@Override
	public void nextActivity() {
		startActivity(Setup4Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup2Activity.class);
	}
}
