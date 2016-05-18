package com.example.service;

import java.util.List;

import com.example.utils.EncryptTools;
import com.example.utils.MyConstants;
import com.example.utils.SpTools;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
/**
 * 定位的服务管理器,来获取定位的信息
 */
public class LocationService extends Service {

	private LocationManager lm;
	private LocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		//获取位置管理器
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new LocationListener() {
			
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * 位置发生变化时触发此方法
			 */
			@Override
			public void onLocationChanged(Location location) {
				// 获取位置变化的结果
				float accuracy = location.getAccuracy();// 精确度,以米为单位
				double altitude = location.getAltitude();// 获取海拔高度
				double longitude = location.getLongitude();// 获取经度
				double latitude = location.getLatitude();// 获取纬度
				float speed = location.getSpeed();// 速度
				
				//定位信息
				StringBuilder tv_mess = new StringBuilder();
				tv_mess.append("accuracy:" + accuracy + "\n");
				tv_mess.append("altitude:" + altitude + "\n");
				tv_mess.append("longitude:" + longitude + "\n");
				tv_mess.append("latitude:" + latitude + "\n");
				tv_mess.append("speed:" + speed + "\n");
				//获取安全号码
				String mSafeNumber = SpTools.getString(getApplicationContext(), MyConstants.SAFENUMBER,"");
				mSafeNumber = EncryptTools.decryption(MyConstants.MUSIC, mSafeNumber);
				//发送短信给安全号码
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(mSafeNumber, null, tv_mess+"", null, null);
				
				//关闭GPS
				stopSelf();
			}
		};
		//获取所有的定位方式
		List<String> providers = lm.getAllProviders();
		for (String string : providers) {
			System.out.println(string + "》》定位方式");
		}
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);//产生费用 例如4G 3G网络
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		//动态获取手机的最佳定位方式
		String bestProvider = lm.getBestProvider(criteria, true);
		//注册回调监听
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		lm.removeUpdates(listener);
		lm = null;
		super.onDestroy();
	}
}
