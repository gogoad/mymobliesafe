package com.example.view;

import com.example.mymobliesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
//自定义组合控件
public class SettingItemView extends LinearLayout{

	private View view;
	private CheckBox mChecked;
	private TextView mContent;
	private TextView mTitle;

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		
	}

	private void initView() {
		view = View.inflate(getContext(), R.layout.item_settingcenter_view, null);
		mTitle = (TextView) view.findViewById(R.id.tv_settingcenter_autoupdate_title);
		mContent = (TextView) view.findViewById(R.id.tv_settingcenter_autoupdate_content);
		mChecked = (CheckBox) view.findViewById(R.id.cb_settingcenter_autoupdate_checked);
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

}
