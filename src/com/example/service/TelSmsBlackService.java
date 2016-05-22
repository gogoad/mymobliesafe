package com.example.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.dao.BlackNumberDao;
import com.example.domain.BlackTable;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class TelSmsBlackService extends Service {
	private BlackNumberDao dao;
	private SmsReceiver receiver;
	private TelephonyManager tm;
	private PhoneStateListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] datas = (Object[]) intent.getExtras().get("pdus");
			for (Object sms : datas) {
				// 获取短信的数据
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) sms);

				// 取短信的发件人号码
				String address = sm.getOriginatingAddress();

				// 判断是否存在黑名单中
				int mode = dao.getMode(address);

				// 短信拦截
				if ((mode & BlackTable.SMS) != 0) {
					// 具有短信拦截
					// if (mode == 短信 || mode == 全部)
					// 拦截此短信
					abortBroadcast();// 终止广播传递
				}
			}
		}

	}

	@Override
	public void onCreate() {

		// 初始化黑名单的业务类
		dao = new BlackNumberDao(getApplicationContext());

		receiver = new SmsReceiver();
		// 短信广播意图
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		// 设置拦截模式为最高
		filter.setPriority(Integer.MAX_VALUE);
		// 注册短信广播
		registerReceiver(receiver, filter);
		// 注册电话监听
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state,
					final String incomingNumber) {
				// TODO Auto-generated method stub
				// state 电话的状态 incomingNumber 打进来的号码
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:// 空闲状态 挂断状态
					System.out.println("CALL_STATE_IDLE");
					break;

				case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
					System.out.println("CALL_STATE_RINGING");
					// 判断incomingNumber是否是黑名单号码
					// 获取电话的模式
					int mode = dao.getMode(incomingNumber);
					if ((mode & BlackTable.TEL) != 0) {
						System.out.println("挂断电话");
						// 挂断电话之前先注册内容观察者
						getContentResolver().registerContentObserver(
								Uri.parse("content://call_log/calls"), true,
								new ContentObserver(new Handler()) {
									public void onChange(boolean selfChange) {
										// 电话日志变化 触发此方法调用
										deleteCalllog(incomingNumber);// 删除电话日志
										// 取消内容观察者注册
										getContentResolver()
												.unregisterContentObserver(this);
										super.onChange(selfChange);
									}
								});
						endCall();
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:// 通话状态

					break;
				default:
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}
		};
		super.onCreate();
	}

	protected void endCall() {
		// tm.endCall(); 1.5版本后，把该方法阉割掉了
		// 想用该功能，实现方法
		// ServiceManager.getService();
		// 反射调用

		try {
			// 1.class
			Class clazz = Class.forName("android.os.ServiceManager");
			// 2. method
			Method method = clazz.getDeclaredMethod("getService", String.class);
			// 3.obj 不需要 静态方法
			// 4. 调用
			Binder binder = (Binder) method.invoke(null,
					Context.TELEPHONY_SERVICE);
			// aidl
			ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
			iTelephony.endCall();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void deleteCalllog(String incomingNumber) {
		// TODO Auto-generated method stub
		// 只能内容提供者来删除电话日志
		Uri uri = Uri.parse("content://call_log/calls");
		// 删除日志
		getContentResolver().delete(uri, "number=?",
				new String[] { incomingNumber });
	};

	@Override
	public void onDestroy() {

		// 取消电话监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		// 取消短信监听
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
