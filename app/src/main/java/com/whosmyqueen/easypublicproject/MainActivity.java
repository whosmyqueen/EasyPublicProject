package com.whosmyqueen.easypublicproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.whosmyqueen.easypublicproject.adapter.ViewPagerFragmentAdapter;
import com.whosmyqueen.easypublicproject.fragment.FindFragment;
import com.whosmyqueen.easypublicproject.fragment.UserFragment;
import com.whosmyqueen.easypublicproject.listener.BaseUIListener;
import com.whosmyqueen.easypublicproject.util.QQUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private List<LinearLayout> mTabBtnList;
    private List<Fragment> mFragmentList;
    private FindFragment mFindFragment;
    private UserFragment mUserFragment;
    private FragmentManager fm;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private BaseUIListener mLoginListener;

    public FindFragment getFindFragment() {
        return mFindFragment;
    }

    @ViewInject(R.id.vp_container_main)
    private ViewPager vp_container;
    @ViewInject(R.id.ll_btn_find_main)
    private LinearLayout btn_find;
    @ViewInject(R.id.ll_btn_friend_main)
    private LinearLayout btn_friend;
    @ViewInject(R.id.ll_btn_msg_main)
    private LinearLayout btn_msg;
    @ViewInject(R.id.ll_btn_user_main)
    private LinearLayout btn_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
        initListener();
    }

    private void initListener() {
        mLoginListener = new BaseUIListener(this) {
            @Override
            protected void doComplete(JSONObject values) {
                super.doComplete(values);
                initOpenidAndToken(values);
                Log.i("logininfo", values.toString());
                QQUtil.dismissQQLoginDialog();
                Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
            }
        };
        for (LinearLayout item : mTabBtnList) {
            item.setOnClickListener(new BottomTabsOnClickListener(mTabBtnList.indexOf(item)));
        }
        vp_container.setOnPageChangeListener(this);
    }

    class BottomTabsOnClickListener implements View.OnClickListener {
        int mPosition;

        public BottomTabsOnClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {

            vp_container.setCurrentItem(mPosition);
        }
    }

    private void initDate() {
        mTabBtnList = new ArrayList<>();
        mTabBtnList.add(btn_find);
        mTabBtnList.add(btn_friend);
        mTabBtnList.add(btn_msg);
        mTabBtnList.add(btn_user);
        fm = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        mFindFragment = new FindFragment();
        mUserFragment = new UserFragment();
        mFragmentList.add(mFindFragment);
        mFragmentList.add(mUserFragment);
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(fm);
        mViewPagerFragmentAdapter.setList(mFragmentList);
        vp_container.setAdapter(mViewPagerFragmentAdapter);
    }

    private void initTabsState() {
        for (int i = 0; i < mTabBtnList.size(); i++) {
            LinearLayout one = mTabBtnList.get(i);
            for (int j = 0; j < one.getChildCount(); j++) {
                View item = one.getChildAt(j);
                if (item instanceof CheckBox) {
                    ((CheckBox) item).setChecked(false);
                } else if (item instanceof TextView) {
                    ((TextView) item).setTextColor(Color.BLACK);
                }

            }
        }
    }

    private void changeSelectTab(int position) {
        initTabsState();
        LinearLayout view = mTabBtnList.get(position);
        for (int i = 0; i < view.getChildCount(); i++) {
            View item = view.getChildAt(i);
            if (item instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) item;
                checkBox.setChecked(true);
            } else if (item instanceof TextView) {
                ((TextView) item).setTextColor(getResources().getColorStateList(R.color.colorUserNameListView));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mLoginListener);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeSelectTab(position);
        checkIsLogin(position);
    }

    private void checkIsLogin(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
            case 2:
            case 3:
                QQUtil.checkIsQQLoginYet(this);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                BaseActivity.TENCENT.setAccessToken(token, expires);
                BaseActivity.TENCENT.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }
}
