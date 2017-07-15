package com.momo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import android.text.TextUtils;

/** 
 * @author  RealMo
 * @date 创建时间：2017-5-16 上午10:43:44 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 * 走马灯的TextView  
 */
public class MarqueeTextView extends TextView{

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}


	public MarqueeTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MarqueeTextView(Context context) {
		this(context, null);
	}
	
	

	private void initView(Context context) {        
        this.setSingleLine(true);
		this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		this.setMarqueeRepeatLimit(-1);
        
	}
	
	@Override
	public boolean isFocused() {
		//realmo must be return true ,have marquee effection.
		return true;
	}
	


	

}
