package com.whosmyqueen.easypublicproject.adapter;

import android.content.Context;

import com.whosmyqueen.easypublicproject.R;
import com.whosmyqueen.easypublicproject.adapter.common.CommonListViewAdapter;
import com.whosmyqueen.easypublicproject.adapter.common.ViewHolder;
import com.whosmyqueen.easypublicproject.constant.NetConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 郑志辉 on 2016/6/22.
 */
public class QuestionDetailsListViewAdapter extends CommonListViewAdapter<JSONObject> {

    public QuestionDetailsListViewAdapter(Context mContext, List<JSONObject> mData, int layoutRes) {
        super(mContext, mData, layoutRes);
    }

    @Override
    public void convertData(ViewHolder viewHolder, JSONObject jsonObject) {
        try {
            viewHolder.setImageView(R.id.iv_icon_item_question_details, NetConstant.USER_HEAD_URL + jsonObject
                    .getString("HeadImageName"));
            viewHolder.setTextView(R.id.tv_name_item_question_details, jsonObject.getString("NickName"));
            viewHolder.setTextView(R.id.tv_content_item_question_details, jsonObject.getString("Content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
