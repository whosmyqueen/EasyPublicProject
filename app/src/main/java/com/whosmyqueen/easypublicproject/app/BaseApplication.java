package com.whosmyqueen.easypublicproject.app;

import android.app.Application;

import com.tencent.tauth.Tencent;
import com.whosmyqueen.easypublicproject.BaseActivity;
import com.whosmyqueen.easypublicproject.constant.BaseConstant;
import com.whosmyqueen.easypublicproject.constant.UserInfoConstant;

import org.xutils.x;

/**
 * Created by 郑志辉 on 2016/6/19.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        BaseActivity.TENCENT = Tencent.createInstance(BaseConstant.APP_KEY, getApplicationContext());
        UserInfoConstant.SP = getSharedPreferences("USER", MODE_PRIVATE);
    }
}
