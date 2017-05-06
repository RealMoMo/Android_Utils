package com.realmo.utils;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ListView、GridView的多(单)布局 适配器
 *
 * 配置数据 -- 必须重写{@link #onBindHolder(ViewHolder, Object, int)}
 * 多布局实现 -- 必须重写{@link #getItemViewType(int)}方法
 *           -- 强制要求数据源中有一个type名称的属性，重写时返回该值
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> data;
	private int[] resId;
	
	public AbsBaseAdapter(Context context, int... resId){
		this.context = context;
		this.resId = resId;
		data = new ArrayList();
	}

	public AbsBaseAdapter(Context context, List<T> data, int... resId) {
		this.context = context;
		this.resId = resId;
		this.data = data;
	}

	public void setData(List<T> data){
		this.data = data;
		this.notifyDataSetChanged();
	}
	
	public void addData(List<T> data){
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	 public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView != null){
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(context).inflate(resId[getItemViewType(position)], null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		onBindHolder(holder, data.get(position), position);
		return convertView;
	}

	/**
	 * 整条Item避免setTag()，如必须，需设置id
     */
	public abstract void onBindHolder(ViewHolder holder, T d, int position);

	@Override
	public int getViewTypeCount() {
		return resId.length;
	}

	protected class ViewHolder{

		Map<Integer, View> cacheMap = new HashMap();
		View layoutView;

		public ViewHolder(View layoutView){
			this.layoutView = layoutView;
		}

		public View getView(int id){
			if(cacheMap.containsKey(id)){
				return cacheMap.get(id);
			} else {
				View view = layoutView.findViewById(id);
				cacheMap.put(id, view);
				return view;
			}
		}

		/**
		 * 给整条item设置tag
		 */
		public ViewHolder setItemTag(int id, Object tag) {
			layoutView.setTag(id, tag);
			return this;
		}

		public ViewHolder setLayoutMinHeight(int minHeight) {
			layoutView.setMinimumHeight(minHeight);
			return this;
		}

		/**
		 * 绑定TextView
		 */
		public ViewHolder bindTextView(int id, String text){
			TextView tv = (TextView) getView(id);
			tv.setText(text);
			return this;
		}

		/**
		 * 绑定TextView 含html标签
		 */
		public ViewHolder bindTextViewWithHtml(int id, String text){
			TextView tv = (TextView) getView(id);
			tv.setText(Html.fromHtml(text));
			return this;
		}

		/**
		 * 绑定ImageView
		 */
		public ViewHolder bindImageView(int id, int imgId){
			ImageView iv = (ImageView) getView(id);
			iv.setImageResource(imgId);
			return this;
		}

		/**
		 * 绑定SimpleDraweeView
		 */
		public ViewHolder bindSimpleDraweeView(int id, String url){
			return bindSimpleDraweeView(id, Uri.parse(url));
		}

		/**
		 * 绑定SimpleDraweeView
		 */
		public ViewHolder bindSimpleDraweeView(int id, Uri uri){
			SimpleDraweeView sdv = (SimpleDraweeView) getView(id);
			sdv.setImageURI(uri);
			return this;
		}

		/**
		 * 绑定SimpleDraweeView
		 */
		public ViewHolder bindSimpleDraweeView(int id, int imgId){
			SimpleDraweeView sdv = (SimpleDraweeView) getView(id);
			sdv.setImageURI(Uri.parse("res:///" + imgId));
			return this;
		}

		/**
		 * 设置进度条进度
		 */
		public ViewHolder bindProgressBar(int id, int num){
			ProgressBar progressBar = (ProgressBar) getView(id);
			progressBar.setProgress(num);
			return this;
		}
	}
}
