package com.example.receiver;

import com.example.utils.MyConstants;
import com.example.utils.SpTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		/*
		 * 从sp中获取是否开启手机保护信息 如果没有开启则直接返回
		 */
		boolean isprotectting = SpTools.getBoolean(context,
				MyConstants.PROTECTTING, false);
		if (!isprotectting) {
			return;
		}
		/*
		 * 从sp中获取已经绑定的sim卡序列号 如果没有绑定sim卡则返回
		 */
		tm = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String sim = SpTools.getString(context, MyConstants.SIM, "");
		if (TextUtils.isEmpty(sim)) {
			Toast.makeText(context, "sim卡未绑定", Toast.LENGTH_LONG);
			return;
		} else {
			/*
			 * 通过TelephonyManager对象获取当前sim卡的序列号
			 * 如果当前的sim卡和sp中的sim卡序列号一致则可以断定SIM卡没有更换
			 * 如果两次的SIM卡序列号不一致，则断定SIM已经更换，这时可以根据用户的设置给安 全号码发送短信，通知SIM卡已经更换
			 */
<<<<<<< a85ebdb4ce72494761159c4ab03551e2f8835d3c
			String number = tm.getSimSerialNumber();
=======
			String number = tm.getSimSerialNumber()+"111";
>>>>>>> 第四次提交 手机防盗功能完成
			if (sim.equals(number)) {
				Toast.makeText(context, "sim卡未更换", Toast.LENGTH_LONG);
			} else {
				// 发送短信给安全号码
				Toast.makeText(context, "sim卡已经更换", Toast.LENGTH_LONG);
				SmsManager.getDefault().sendTextMessage(
						SpTools.getString(context, MyConstants.SAFENUMBER, ""),
<<<<<<< a85ebdb4ce72494761159c4ab03551e2f8835d3c
						null, "sim卡改变，请确认操作是否是本人", null, null);
=======
						null, "我是小偷", null, null);
>>>>>>> 第四次提交 手机防盗功能完成
			}
		}
	}

}
