package com.whosmyqueen.easypublicproject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.whosmyqueen.easypublicproject.R;
import com.whosmyqueen.easypublicproject.adapter.ViewPagerFragmentAdapter;
import com.whosmyqueen.easypublicproject.util.QQUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_find)
public class FindFragment extends BaseFragment implements View.OnClickListener {
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private FragmentManager fm;
    private List<Fragment> mList;
    private FindQuestionFragment mFindQuestionFragment;
    private FindWateringFragment mFindWateringFragment;
    private JSONObject userInfo;

    @ViewInject(R.id.tl_tabs_main)
    private TabLayout tl_tab;
    @ViewInject(R.id.vp_container_find)
    private ViewPager vp_container;
    @ViewInject(R.id.iv_add_question_find)
    private ImageView iv_add;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initListener();
    }

    private void initListener() {
        iv_add.setOnClickListener(this);
    }

    private void initDate() {
        mList = new ArrayList<>();
        mFindQuestionFragment = new FindQuestionFragment();
        mFindWateringFragment = new FindWateringFragment();
        mList.add(mFindQuestionFragment);
        mList.add(mFindWateringFragment);
        fm = getActivity().getSupportFragmentManager();
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(fm);
        mViewPagerFragmentAdapter.setList(mList);
        vp_container.setAdapter(mViewPagerFragmentAdapter);
        tl_tab.setSelectedTabIndicatorColor(Color.BLACK);
        tl_tab.setupWithViewPager(vp_container);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_question_find:
                userInfo = QQUtil.checkIsQQLoginYet(getActivity());
                if (userInfo != null) {

                }
                break;
        }
    }


}
