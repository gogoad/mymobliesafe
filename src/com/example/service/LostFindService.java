package com.example.service;

import android.R;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.telephony.SmsMessage;

public class LostFindService extends Service {

	private SmsReceiver receiver;
	private boolean isplay;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//实现短信拦截功能
			Bundle extras = intent.getExtras();
			Object datas[] = (Object[]) extras.get("pdus");
			for (Object object : datas) {
				SmsMessage sm = SmsMessage.createFromPdu((byte[])object);
				String body = sm.getMessageBody();
				if (body.equals("#*gps*#")) {
					//耗时的定位,把定位的功能放到服务中执行
					Intent gps = new Intent(LostFindService.this, LocationService.class);
					startService(gps);
					
					abortBroadcast();
				}else if (body.equals("#*wipedata*#")) {
					//远程清除数据
					//获取设备管理器
					DevicePolicyManager manager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
					manager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					abortBroadcast();
				}else if (body.equals("#*lockscreen*#")) {
					//远程锁屏
					DevicePolicyManager manager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
					manager.resetPassword("123", 0);
					manager.lockNow();
					abortBroadcast();
				}else if (body.equals("#*music*#")) {
					//播放报警音乐
					abortBroadcast();
					//只播放一次
					if (isplay) {
						return;
					}
					MediaPlayer create = MediaPlayer.create(getApplicationContext(), com.example.mymobliesafe.R.raw.ylzs);
					//设置左右声道声音为最大值
					create.setVolume(1, 1);
					//开始播放
					create.start();
					create.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							//音乐播放完毕触发此方法
							isplay = false;
						}
					});
					isplay = true;
				}
			}
		}
		
	}
	
	@Override
	public void onCreate() {
		receiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);//级别一样，清单文件，谁先注册谁先执行，如果级别一样，代码比清单要高
		//注册短信监听
		registerReceiver(receiver, filter);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
