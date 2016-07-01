package com.whosmyqueen.easypublicproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whosmyqueen.easypublicproject.adapter.QuestionDetailsListViewAdapter;
import com.whosmyqueen.easypublicproject.constant.NetConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_question_details)
public class QuestionDetailsActivity extends BaseActivity implements View.OnClickListener {
    private int articleInfoId = 0;
    private int pageIndex = 1;
    private static final int PAGE_SIZE = 20;
    private View headView;
    private JSONObject jsonObject = null;
    private QuestionDetailsListViewAdapter mQuestionDetailsListViewAdapter;
    private List<JSONObject> mData;
    private ImageOptions mImageOptions;

    @ViewInject(R.id.lv_container_question_details)
    private ListView lv_container;
    @ViewInject(R.id.srl_refresh_question_details)
    private SwipeRefreshLayout srl_refresh;
    @ViewInject(R.id.iv_back_question_details)
    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
    }

    private void initData() {
        mImageOptions = new ImageOptions.Builder().setFadeIn(true).setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.me)
                .build();
        headView = LayoutInflater.from(this).inflate(R.layout.head_question_details, null);
        Intent intent = getIntent();
        String item = intent.getStringExtra("item");


        try {
            if (item != null) {
                jsonObject = new JSONObject(item);
                articleInfoId = jsonObject.getInt("ArticleInfoId");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateListViewData();
    }

    private void updateListViewData() {
        RequestParams params = new RequestParams(NetConstant.ROOT_URL + "findAnswerInfoByPageInfo.spring");
        params.addBodyParameter("articleInfoId", articleInfoId + "");
        params.addBodyParameter("pageSize", PAGE_SIZE + "");
        params.addBodyParameter("pageIndex", pageIndex + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            String result = null;
            boolean isError;

            @Override
            public void onSuccess(String s) {
                result = s;
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                isError = true;
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                if (!isError && result != null) {
                    try {
                        parseJsonData(result);
                        initHeadViewData();
                        initListView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "无评论", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initHeadViewData() throws JSONException {
        TextView sum = (TextView) headView.findViewById(R.id.tv_answer_sum_head_question_details);
        ImageView icon = (ImageView) headView.findViewById(R.id.iv_icon_head_question_details);
        TextView name = (TextView) headView.findViewById(R.id.tv_name_head_question_details);
        RelativeLayout contentContainer = (RelativeLayout) headView.findViewById(R.id
                .rl_container_head_question_details);
        TextView content = (TextView) headView.findViewById(R.id.tv_content_head_question_details);
        TextView time = (TextView) headView.findViewById(R.id.tv_time_head_qustion_details);
        x.image().bind(icon, NetConstant.USER_HEAD_URL + jsonObject.getString("HeadImageName"));
        name.setText(jsonObject.getString("NickName"));
        content.setText(jsonObject.getString("Content"));
        //        Log.i("publicdate", new SimpleDateFormat("yyyy-MM-dd").format(jsonObject.get("PublicDate")));
        time.setText(jsonObject.getString("PublicDate"));
        sum.setText(mData.size() + " 个回答");
    }

    private void initListView() {

        lv_container.addHeaderView(headView);
        if (mQuestionDetailsListViewAdapter == null && pageIndex == 1) {
            mQuestionDetailsListViewAdapter = new QuestionDetailsListViewAdapter(this, mData, R.layout
                    .item_list_view_question_details);
            mQuestionDetailsListViewAdapter.setData(mData);
            lv_container.setAdapter(mQuestionDetailsListViewAdapter);

        } else {
            mQuestionDetailsListViewAdapter.setData(mData);
            mQuestionDetailsListViewAdapter.notifyDataSetChanged();
        }
    }

    private void parseJsonData(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getInt("code") == 200) {
            mData = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                mData.add(item);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_question_details:
                finish();
                break;
        }
    }
}
