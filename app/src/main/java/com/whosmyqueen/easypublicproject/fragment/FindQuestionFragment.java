package com.whosmyqueen.easypublicproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whosmyqueen.easypublicproject.QuestionDetailsActivity;
import com.whosmyqueen.easypublicproject.R;
import com.whosmyqueen.easypublicproject.adapter.FindQuestionListViewAdapter;
import com.whosmyqueen.easypublicproject.constant.NetConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_find_question)
public class FindQuestionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView
        .OnScrollListener, AdapterView.OnItemClickListener {
    private int pageIndex = 1;
    private int totalPage = 0;
    private static int PAGE_SIZE = 20;
    private static int AREAL_ID = 1;
    private static String MOD = "getArticleByPageInfo.spring";
    private List<JSONObject> mListData;
    private FindQuestionListViewAdapter mListViewAdapter;

    @ViewInject(R.id.srl_refresh_find_question)
    private SwipeRefreshLayout srl_refresh;
    @ViewInject(R.id.lv_container_find_question)
    private ListView lv_container;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }

    private void initListener() {
        srl_refresh.setOnRefreshListener(this);
        lv_container.setOnScrollListener(this);
        lv_container.setOnItemClickListener(this);
    }

    private TextView addHeaderView;

    private void initData() {
        addHeaderView = new TextView(getContext());
        addHeaderView.setText("12213");
        addHeaderView.setHeight(50);
        addHeaderView.setWidth(100);
        isLoad = false;
        updateListView();
    }

    private void updateListView() {
        RequestParams params = new RequestParams(NetConstant.ROOT_URL + this.MOD);
        params.addBodyParameter("pageIndex", pageIndex + "");
        params.addBodyParameter("pageSize", PAGE_SIZE + "");
        params.addBodyParameter("arealId", AREAL_ID + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            String result = "";
            boolean isError;

            @Override
            public void onSuccess(String s) {
                this.result = s;
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
                    parseJsonData(result);
                    initListView();
                } else {
                    Toast.makeText(getContext(), "网络错误！", Toast.LENGTH_LONG).show();
                }
                srl_refresh.setRefreshing(false);

            }
        });
    }

    private void initListView() {
        if (mListViewAdapter == null && pageIndex == 1) {
            mListViewAdapter = new FindQuestionListViewAdapter(getContext(), mListData, R.layout
                    .item_list_view_find_question);
            mListViewAdapter.setData(mListData);
            lv_container.setAdapter(mListViewAdapter);

        } else {
            mListViewAdapter.setData(mListData);
            mListViewAdapter.notifyDataSetChanged();
        }
        if (lv_container.getHeaderViewsCount() == 0) {
            lv_container.addHeaderView(addHeaderView);
        }


    }

    private void parseJsonData(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                totalPage = jsonObject.getInt("totalPage");
                if (!isLoad) {
                    mListData = new ArrayList<>();
                }
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    mListData.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        updateListView();
    }

    private boolean isLoad;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && isLoad) {
            if (pageIndex != totalPage) {
                pageIndex++;
                updateListView();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == firstVisibleItem + visibleItemCount && mListData != null) {
            isLoad = true;
        } else {
            isLoad = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject item = mListData.get(position - 1);
        Intent intent = new Intent(getActivity(), QuestionDetailsActivity.class);
        intent.putExtra("item", item.toString());
        startActivityForResult(intent, 1);
    }
}
