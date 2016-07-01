package com.whosmyqueen.easypublicproject.adapter.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 郑志辉 on 2016/6/19.
 */
public abstract class CommonListViewAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mData;
    private int layoutRes;

    public void setData(List<T> data) {
        mData = data;
    }

    public CommonListViewAdapter(Context mContext, List<T> mData, int layoutRes) {
        this.mContext = mContext;
        this.mData = mData;
        this.layoutRes = layoutRes;
    }

    @Override
    public int getCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1:
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder(mContext, position, parent, layoutRes);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.setPosition(position);
        }
        //2:
        T t = mData.get(position);
        //3:
        convertData(viewHolder, t);
        //4:
        return viewHolder.getCurrentView();
    }

    public abstract void convertData(ViewHolder viewHolder, T t);
}
