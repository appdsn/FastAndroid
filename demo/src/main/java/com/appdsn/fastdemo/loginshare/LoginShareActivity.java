package com.appdsn.fastdemo.loginshare;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.fastdemo.R;
import com.appdsn.library.MobShareSDK;
import com.appdsn.library.listener.OnMobLoginListener;
import com.appdsn.library.listener.OnMobShareListener;
import com.appdsn.library.model.MobLoginInfo;
import com.appdsn.library.model.PlatformType;
import com.appdsn.library.model.ShareParams;
import com.appdsn.library.model.ShareType;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/10 10:49
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class LoginShareActivity extends BaseAppActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login_share;
    }

    @Override
    protected void initVariable(Intent intent) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {
        findViewById(R.id.btnWXLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobShareSDK.login(LoginShareActivity.this, PlatformType.WEIXIN, mOnLoginListener);
            }
        });

        findViewById(R.id.btnQQLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobShareSDK.login(LoginShareActivity.this, PlatformType.QQ, mOnLoginListener);
            }
        });

        findViewById(R.id.btnWXShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareParams params = ShareParams.create(LoginShareActivity.this, ShareType.WEB_MIX)
                        .setTitle("微信分享标题")
                        .setDesc("描述")
                        .setUrl("http://www.baidu.com")
                        .setImage(R.drawable.icon_camera);
                MobShareSDK.shareDirect(PlatformType.WEIXIN, params, mOnShareListener);
            }
        });

        findViewById(R.id.btnQQShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareParams params = ShareParams.create(LoginShareActivity.this, ShareType.WEB_MIX)
                        .setTitle("QQ分享标题").setDesc("描述").setUrl("http://www.baidu.com").setImage(R.drawable.icon_camera);
                MobShareSDK.shareDirect(PlatformType.QQ, params, mOnShareListener);
            }
        });

        findViewById(R.id.btnOpenShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareParams params = ShareParams.create(LoginShareActivity.this, ShareType.WEB_MIX)
                        .setTitle("分享图文标题")
                        .setDesc("描述")
                        .setUrl("http://www.baidu.com")
                        .setImage(R.drawable.icon_camera);

                /*设置支持的分享平台*/
                PlatformType[] platformList = new PlatformType[]{PlatformType.QQ, PlatformType.QZONE, PlatformType.WEIXIN_CIRCLE, PlatformType.WEIXIN};
                MobShareSDK.openShareBoard(platformList, params, mOnShareListener);
            }
        });
    }

    private OnMobLoginListener mOnLoginListener = new OnMobLoginListener() {

        @Override
        public void onStart(PlatformType platform) {
            Log.i("123", "onStart" + platform.name());
        }

        @Override
        public void onComplete(PlatformType platform, int action, MobLoginInfo loginInfo) {
            Log.i("123", "onComplete" + platform.name() + loginInfo.toString());
        }

        @Override
        public void onError(PlatformType platform, int action, Throwable t) {
            Log.i("123", "onError" + platform.name() + t.toString());
        }

        @Override
        public void onCancel(PlatformType platform, int action) {
            Log.i("123", "onCancel" + platform.name());
        }
    };

    private OnMobShareListener mOnShareListener = new OnMobShareListener() {

        @Override
        public void onStart(PlatformType platform) {
            Log.i("123", "onStart" + platform.name());
        }

        @Override
        public void onResult(PlatformType platform) {
            Log.i("123", "onResult" + platform.name());
        }

        @Override
        public void onError(PlatformType platform, Throwable t) {
            Log.i("123", "onError" + platform.name() + t.toString());
        }

        @Override
        public void onCancel(PlatformType platform) {
            Log.i("123", "onCancel" + platform.name());
        }
    };

    @Override
    protected void loadData() {

    }
}
