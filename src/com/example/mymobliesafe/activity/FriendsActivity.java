package com.example.mymobliesafe.activity;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.example.domain.BlackBean;
import com.example.domain.ContactBean;
import com.example.engine.ReadContactsEngine;
import com.example.utils.MyConstants;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends BaseFriendsCallSmsActivity {

	@Override
	public List<ContactBean> getdatas() {
		// TODO Auto-generated method stub
		return ReadContactsEngine.readContacts(getApplicationContext());
	}

}
