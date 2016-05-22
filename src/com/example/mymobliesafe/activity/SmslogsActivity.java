package com.example.mymobliesafe.activity;

import java.util.List;

import com.example.domain.ContactBean;
import com.example.engine.ReadContactsEngine;

public class SmslogsActivity extends BaseFriendsCallSmsActivity {

	@Override
	public List<ContactBean> getdatas() {
		// TODO Auto-generated method stub
		return ReadContactsEngine.readSmslogs(getApplicationContext());
	}

}
