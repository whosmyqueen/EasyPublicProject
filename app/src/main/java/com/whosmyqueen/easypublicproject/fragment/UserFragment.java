package com.whosmyqueen.easypublicproject.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whosmyqueen.easypublicproject.R;
import com.whosmyqueen.easypublicproject.adapter.UserListViewAdapter;
import com.whosmyqueen.easypublicproject.util.QQUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_user)
public class UserFragment extends BaseFragment implements View.OnClickListener {
    private JSONObject userInfo;
    private UserListViewAdapter mUserListViewAdapter;

    @ViewInject(R.id.lv_container_user)
    private ListView lv_container;
    @ViewInject(R.id.ll_user_container_user)
    private LinearLayout btn_user_container;
    @ViewInject(R.id.srl_refresh_user)
    private SwipeRefreshLayout srl_refresh;
    @ViewInject(R.id.iv_icon_user)
    private ImageView iv_icon;
    @ViewInject(R.id.tv_name_user)
    private TextView tv_name;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        btn_user_container.setOnClickListener(this);
    }

    public void initData() throws JSONException {
        userInfo = QQUtil.checkIsQQLoginYet(getActivity());
        if (userInfo != null) {
            initUserHeadView();
        }
    }

    private void initUserHeadView() throws JSONException {
        tv_name.setText(userInfo.getString("NickName"));

    }

    private void setUserIcon() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_container_user:
                userInfo = QQUtil.checkIsQQLoginYet(getActivity());
                if (userInfo != null) {
                    try {
                        initUserHeadView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
