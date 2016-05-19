package com.example.view;

import com.example.mymobliesafe.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SettingCenterItemView extends LinearLayout {

	

	private TextView tv_title;
	private TextView tv_content;
	private CheckBox cb_check;
	private String[] contents;
	private View item;

	public SettingCenterItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initEvent();
		String content = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mymobliesafe", "content");
		
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mymobliesafe", "title");
		
		tv_title.setText(title);
		
		contents = content.split("-");
		//初始化复选框的内容
		tv_content.setTextColor(Color.RED);
		tv_content.setText(contents[1]);
		
		
	}

	//给跟布局设置点击事件
	public void setItemClickListener(OnClickListener listener) {
		//通过自定义组合控制，把事件传递给子组件
		item.setOnClickListener(listener);
	}

	public void setChecked(boolean isChecked){
		cb_check.setChecked(isChecked);
	}
	
	
	public boolean isChecked(){
		return cb_check.isChecked();
	}

	
	private void initEvent() {

		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cb_check.setChecked(!cb_check.isChecked());
			}
		});
		
	
		cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	
				if (isChecked) {
				
					tv_content.setTextColor(Color.GREEN);
					tv_content.setText(contents[0]);
				} else {
					
					tv_content.setTextColor(Color.RED);
					tv_content.setText(contents[1]);
				}
			}
		});
	}

	private void initView(){
		item = View.inflate(getContext(), R.layout.item_settingcenter_view, null);
	
		tv_title = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_title);
		
		tv_content = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_content);
		
		cb_check = (CheckBox) item.findViewById(R.id.cb_settingcenter_autoupdate_checked);
		
		addView(item);
	}
	
	public SettingCenterItemView(Context context) {
		super(context);
		initView();
	}

}
