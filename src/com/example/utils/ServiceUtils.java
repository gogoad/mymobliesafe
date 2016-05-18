package com.example.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	public static boolean isServicesRunning(Context context, String serviceName) {
		boolean isRunning = false;
		//获取运行中的服务ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(50);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			if (runningServiceInfo.service.getClassName().equals(serviceName)) {
				isRunning = true;
				break;
			}
		}
		return isRunning;	
	}
}
