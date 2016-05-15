package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 自定义可以滚动的textView控件
 * @author jxa
 *
 */
public class FocusTextView extends TextView {

	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean isFocused() {
		return true;
	}
}
