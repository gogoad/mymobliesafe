package com.example.mymobliesafe;

import java.util.List;

import com.example.domain.ContactBean;
import com.example.engine.ReadContactsEngine;
import com.example.mymobliesafe.activity.BaseFriendsCallSmsActivity;

public class SmslogsActivity extends BaseFriendsCallSmsActivity {

	@Override
	public List<ContactBean> getdatas() {
		// TODO Auto-generated method stub
		return ReadContactsEngine.readSmslogs(getApplicationContext());
	}

}
