package com.realmo.view;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;


/**
 * @author RealMo
 * @date 创建时间：2019-4-13 下午8:20:33
 * @version 1.0
 * @parameter
 * @since
 * @return Preference与 Switch点击事件分离的SwitchPreference
 * 
 */
public class SeparationSwitchPreference extends SwitchPreference {



	private Context mContext;
	private Switch mSwitch;

	public interface SwitchChangeListener {
		void switchOnChange(Switch view, boolean isChecked);
	}

	private SwitchChangeListener mSwitchChangeListener;

	public SeparationSwitchPreference(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;

	}

	public SeparationSwitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

	}

	public SeparationSwitchPreference(Context context) {
		super(context);
		mContext = context;

	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
		if(Build.VERSION.SDK_INT >= 26){
			int switchId = mContext.getResources().getIdentifier(
				"android:id/switch_widget", null, null);
		mSwitch = (Switch) view.findViewById(switchId);	
		}else{
			int switchId = mContext.getResources().getIdentifier(
				"android:id/switchWidget", null, null);
		mSwitch = (Switch) view.findViewById(switchId);	
		}

		mSwitch = (Switch) view.findViewById(switchId);	
		mSwitch.setOnCheckedChangeListener(null);
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (mSwitchChangeListener == null) {
					// 默认的储存操作
					// 触发回调preference的onPreferenceChange方法
					callChangeListener(isChecked);
				} else {
					// 自定义回调的操作
					mSwitchChangeListener.switchOnChange(mSwitch, isChecked);
				}
				// 触发回调preference的onPreferenceChange方法
				// callChangeListener(isChecked);
			}
		});

		mSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

	}

	// 重写 onClick 不实现
	@Override
	protected void onClick() {
	}

	public void setOnSwitchChangeCallBack(
			SwitchChangeListener switchChangeListener) {
		mSwitchChangeListener = switchChangeListener;
	}

	public void setSwitchEnable(boolean enabled) {
		mSwitch.setEnabled(enabled);
		if (!enabled) {
			mSwitch.setChecked(false);
		}
	}

	public void setSwitchChecked(boolean isChecked) {
		mSwitch.setChecked(false);

	}

}
