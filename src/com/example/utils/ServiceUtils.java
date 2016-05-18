package com.example.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
<<<<<<< a85ebdb4ce72494761159c4ab03551e2f8835d3c

	public static boolean isServicesRunning(Context context, String serviceName) {
		boolean isRunning = false;
		//获取运行中的服务ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(50);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			if (runningServiceInfo.service.getClassName().equals(serviceName)) {
				isRunning = true;
=======
	/**
	 * @param context
	 * @param serviceName
	 *       service完整的名字 包名+类名
	 * @return
	 *     该servcie是否在运行
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		boolean isRunning = false;
		//判断运行中的服务状态，ActivityManager
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		//获取android手机中运行的所有服务
		List<RunningServiceInfo> services = am.getRunningServices(50);
		
		for (RunningServiceInfo runningServiceInfo : services) {
			//判断服务的名字是否包含我们指定的服务名
			if (runningServiceInfo.service.getClassName().equals(serviceName)) {
				//该服务在运行中 
				isRunning = true;
				//已经找到，退出循环
>>>>>>> 第四次提交 手机防盗功能完成
				break;
			}
		}
		return isRunning;	
	}
}
