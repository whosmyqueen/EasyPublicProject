package com.whosmyqueen.easypublicproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.whosmyqueen.easypublicproject.BaseActivity;
import com.whosmyqueen.easypublicproject.R;
import com.whosmyqueen.easypublicproject.constant.NetConstant;
import com.whosmyqueen.easypublicproject.constant.UserInfoConstant;
import com.whosmyqueen.easypublicproject.listener.BaseUIListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 郑志辉 on 2016/6/30.
 */
public class QQUtil {
    private static AlertDialog loginDialog = null;
    private static Context CONTENT;
    private static Map USER_INFO;
    private static String REGISTER_MOD = "qqRegister.spring";


    public static JSONObject checkIsQQLoginYet(Activity activity) {
        String userInfoTemp = UserInfoConstant.SP.getString("UserInfo", null);
        JSONObject userInfo = null;
        if (userInfoTemp != null) {
            //用户登录成功
            try {
                userInfo = new JSONObject(userInfoTemp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            QQUtil.showQQLoginDialog(activity);
        }
        return userInfo;
    }

    public static void qqLogin(final Activity activity) {
        Tencent tencent = BaseActivity.TENCENT;
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", new BaseUIListener(activity) {
                @Override
                protected void doComplete(JSONObject values) {
                    super.doComplete(values);
                    USER_INFO = new HashMap();
                    initOpenidAndToken(values);
                    try {
                        String openid = values.getString("openid");
                        USER_INFO.put("OpenId", openid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateUserInfo(activity);
                    QQUtil.dismissQQLoginDialog();
                }
            });
        } else {
            tencent.logout(activity.getApplicationContext());
        }
    }

    public static void dismissQQLoginDialog() {
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }

    public static void showQQLoginDialog(final Activity activity) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_qq_login_alert:
                        //qq快捷登录
                        //                loginDialog.dismiss();
                        QQUtil.qqLogin(activity);
                        break;
                    case R.id.tv_phone_login_alert:
                        //手机登录
                        break;
                    case R.id.tv_phone_register_alert:
                        //手机注册
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.login_alert_message, null);
        view.findViewById(R.id.btn_qq_login_alert).setOnClickListener(listener);
        view.findViewById(R.id.tv_phone_login_alert).setOnClickListener(listener);
        view.findViewById(R.id.tv_phone_register_alert).setOnClickListener(listener);
        builder.setView(view);
        loginDialog = builder.create();
        loginDialog.show();
    }

    public static void updateUserInfo(final Context context) {
        if (BaseActivity.TENCENT != null && BaseActivity.TENCENT.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject jsonUser = (JSONObject) response;
                    try {
                        String nickName = jsonUser.getString("nickname");
                        USER_INFO.put("NickName", nickName);
                        RequestParams params = new RequestParams(NetConstant.ROOT_URL + REGISTER_MOD);
                        params.addBodyParameter("openId", USER_INFO.get("OpenId").toString());
                        params.addBodyParameter("nickName", USER_INFO.get("NickName").toString());
                        x.http().get(params, new Callback.CommonCallback<String>() {
                            String result;
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
                                    SharedPreferences.Editor editor = UserInfoConstant.SP.edit();
                                    if (!result.equals("0")) {
                                        USER_INFO.put("UserInfoId", Integer.parseInt(result));
                                        JSONObject userInfo = new JSONObject(USER_INFO);
                                        editor.putString("UserInfo", userInfo.toString());
                                        USER_INFO = null;
                                        Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        editor.remove("UserInfo");
                                    }
                                    editor.commit();

                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //                    Util.showResultDialog(CONTENT, response.toString(), "");
                }

                @Override
                public void onCancel() {

                }
            };
            BaseActivity.USERINFO = new UserInfo(CONTENT, BaseActivity.TENCENT.getQQToken());
            BaseActivity.USERINFO.getUserInfo(listener);

        } else {
            BaseActivity.TENCENT.logout(context);
        }
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
