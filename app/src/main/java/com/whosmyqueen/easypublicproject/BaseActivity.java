package com.whosmyqueen.easypublicproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;

import org.xutils.x;

public class BaseActivity extends AppCompatActivity {
    public static Tencent TENCENT;
    public static UserInfo USERINFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }


}
