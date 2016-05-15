package com.example.mymobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends BaseSetupAcitivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}


	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);
	}

	@Override
	public void nextActivity() {
		startActivity(Setup3Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(LostFindActivity.class);
	}
}
