package com.example.mymobliesafe.activity;

import com.example.mymobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	public void phoneQuery(View v) {
		Intent query = new Intent(AToolsActivity.this, PhoneLocationActivity.class);
		startActivity(query);
	}
	
	private void initView() {
		setContentView(R.layout.activity_atools);
	}
}
