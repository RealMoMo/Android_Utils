package momo.com.week12_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
    protected LayoutInflater mInflater;

    protected Context mContext;

    protected List<T> mDatas;

    protected final int mItemLayoutId;

    protected int countSum = -1;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    /**
     * 替换元素并刷新
     *
     * @param mDatas
     */
    public void refresh(List<T> mDatas)
    {
        this.mDatas = mDatas;
        this.notifyDataSetChanged();
    }

    /**
     * 删除元素并更新
     *
     * @param position
     */
    public void deleteList(int position)
    {
        this.mDatas.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * 定义itemCount
     */
    public CommonAdapter setCount(int i)
    {
        countSum = i;
        this.notifyDataSetChanged();
        return this;
    }

    @Override
    public int getCount()
    {
        if (countSum == -1)
        {
            return mDatas.size();
        }
        else
        {
            return countSum;
        }
    }

    @Override
    public T getItem(int position)
    {
        if (countSum == -1)
        {
            return mDatas.get(position);
        }
        else
        {
            return mDatas.get(countSum % mDatas.size());
        }

    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, position, getItem(position));
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder helper, int position, T item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent)
    {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}
