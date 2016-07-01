package com.whosmyqueen.easypublicproject.adapter;

import android.content.Context;

import com.whosmyqueen.easypublicproject.adapter.common.CommonListViewAdapter;
import com.whosmyqueen.easypublicproject.adapter.common.ViewHolder;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by 郑志辉 on 2016/6/30.
 */
public class UserListViewAdapter extends CommonListViewAdapter<JSONObject> {

    public UserListViewAdapter(Context mContext, List<JSONObject> mData, int layoutRes) {
        super(mContext, mData, layoutRes);
    }

    @Override
    public void convertData(ViewHolder viewHolder, JSONObject jsonObject) {

    }
}
