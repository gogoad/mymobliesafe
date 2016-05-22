package com.example.service;

import com.example.engine.PhoneLocationEngine;
import com.example.mymobliesafe.R;
import com.example.utils.MyConstants;
import com.example.utils.SpTools;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

public class ComingPhoneService extends Service {

	private TelephonyManager tm;
	private PhoneStateListener listener;
	private WindowManager.LayoutParams params;
	private WindowManager wm;
	private View view;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		initToastParams();

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		listener = new PhoneStateListener() {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {

				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					
					closeLocationToast();
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:// ͨ��״̬
					
					closeLocationToast();
					break;
				case TelephonyManager.CALL_STATE_RINGING:// ����״̬
					
					showLocationToast(incomingNumber);
					break;

				default:
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}

		};

		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	private void initToastParams() {
		// TODO Auto-generated method stub
		// XXX This should be changed to use a Dialog, with a Theme.Toast
		// defined that sets up the layout params appropriately.

		// ��˾�ĳ�ʼ������
		params = new WindowManager.LayoutParams();
		
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		
		params.x = (int) Float.parseFloat(SpTools.getString(getApplicationContext(), MyConstants.TOASTX, "0"));
		params.y = (int) Float.parseFloat(SpTools.getString(getApplicationContext(), MyConstants.TOASTY, "0"));
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		params.setTitle("Toast");
	}


	protected void closeLocationToast() {
		// TODO Auto-generated method stub
	
		if (view != null) {
			wm.removeView(view);
			view = null;
		}
	}

	int bgStyles[] = new int[]{R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green,R.drawable.call_locate_orange,R.drawable.call_locate_white};

	protected void showLocationToast(String incomingNumber) {
		view = View.inflate(getApplicationContext(), R.layout.sys_toast, null);
		int index = Integer.parseInt(SpTools.getString(getApplicationContext(), MyConstants.STYLEBGINDEX, "0"));
		view.setBackgroundResource(bgStyles[index]);
		TextView tv_location = (TextView) view
				.findViewById(R.id.tv_toast_location);
		tv_location.setText(PhoneLocationEngine.locationQuery(incomingNumber,
				getApplicationContext()));

		view.setOnTouchListener(new OnTouchListener() {

			private float startX;
			private float startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println(event.getX() + ":" + event.getRawX());
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getRawX();
					startY = event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					float moveX = event.getRawX();
					float moveY = event.getRawY();
					
					//dx x�����λ�ñ仯ֵ dy y�����λ�ñ仯ֵ
					float dx = moveX - startX;
					float dy = moveY - startY;
				
					params.x += dx;
					params.y += dy;
				
					startX = moveX;
					startY = moveY;
					
				
					wm.updateViewLayout(view, params);
					break;
				case MotionEvent.ACTION_UP:
				
					if (params.x < 0) {
						params.x = 0;
					} else if (params.x + view.getWidth() > wm.getDefaultDisplay().getWidth()) {
						params.x =  wm.getDefaultDisplay().getWidth() - view.getWidth();
					}
					
					if (params.y < 0) {
						params.y = 0;
					} else if (params.y + view.getHeight() > wm.getDefaultDisplay().getHeight()) {
						params.y = wm.getDefaultDisplay().getHeight() - view.getHeight();
					}
					SpTools.putString(getApplicationContext(), MyConstants.TOASTX, params.x + "");
					SpTools.putString(getApplicationContext(), MyConstants.TOASTY, params.y + "");
					
				default:
					break;
				}
				return false;
			}
		});
		wm.addView(view, params);
	}

	@Override
	public void onDestroy() {
		
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}

}
