package com.example.mymobliesafe;

import java.util.List;

import com.example.domain.ContactBean;
import com.example.engine.ReadContactsEngine;

public class CalllogsActivity extends BaseFriendsCallSmsActivity {

	@Override
	public List<ContactBean> getdatas() {
		return ReadContactsEngine.calllogs(getApplicationContext());
	}

}
