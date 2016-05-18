package com.example.service;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsMessage;

public class LostFindService extends Service {

	private smsReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class smsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//实现短信拦截功能 获取到自己想要的信息
			Bundle extras = intent.getExtras();
			Object datas[] = (Object[]) extras.get("pdus");
			for (Object data:datas){
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) data);
				System.out.println(sm.getMessageBody() + ":" + sm.getOriginatingAddress());
			}	
		}
	}
	
	@Override
	public void onCreate() {
		//短信广播接收者
		receiver = new smsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);//级别一样，清单文件，谁先注册谁先执行，如果级别一样，代码比清单要高
		//注册短信监听
		registerReceiver(receiver, filter);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		//取消短信监听
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
