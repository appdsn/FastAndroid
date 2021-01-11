package com.appdsn.library.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appdsn.library.listener.OnMobLoginListener;
import com.appdsn.library.listener.OnMobShareListener;
import com.appdsn.library.qq.QQLogin;
import com.appdsn.library.qq.QQShare;
import com.appdsn.library.sina.SinaLogin;
import com.appdsn.library.sina.SinaShare;
import com.umeng.socialize.UMShareAPI;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/10 18:19
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class ShareTransActivity extends Activity {
    private static OnMobShareListener sOnShareListener;
    private static OnMobLoginListener sOnLoginListener;
    private static ShareParams sShareParams;
    private static PlatformType sPlatform;

    public static void startQQShare(Activity activity, PlatformType platform, ShareParams params, final OnMobShareListener shareListener) {
        try {
            Intent intent = new Intent(activity, ShareTransActivity.class);
            intent.putExtra("action", 1);
            activity.startActivity(intent);
            sOnShareListener = shareListener;
            sShareParams = params;
            sPlatform = platform;
        } catch (Exception e) {
            if (shareListener != null) {
                shareListener.onError(PlatformType.QQ, new Exception("启动ShareTransActivity失败"));
            }
        }
    }

    public static void startQQLogin(Activity activity, final OnMobLoginListener loginListener) {
        try {
            Intent intent = new Intent(activity, ShareTransActivity.class);
            intent.putExtra("action", 2);
            activity.startActivity(intent);
            sOnLoginListener = loginListener;
        } catch (Exception e) {
            if (loginListener != null) {
                loginListener.onError(PlatformType.QQ, -1, new Exception("启动ShareTransActivity失败"));
            }
        }

    }

    public static void startSinaShare(Activity activity, PlatformType platform, ShareParams params, final OnMobShareListener shareListener) {
        try {
            Intent intent = new Intent(activity, ShareTransActivity.class);
            intent.putExtra("action", 3);
            activity.startActivity(intent);
            sOnShareListener = shareListener;
            sShareParams = params;
            sPlatform = platform;
        } catch (Exception e) {
            if (shareListener != null) {
                shareListener.onError(PlatformType.WEIBO, new Exception("启动ShareTransActivity失败"));
            }
        }
    }

    public static void startSinaLogin(Activity activity, final OnMobLoginListener loginListener) {
        try {
            Intent intent = new Intent(activity, ShareTransActivity.class);
            intent.putExtra("action", 4);
            activity.startActivity(intent);
            sOnLoginListener = loginListener;
        } catch (Exception e) {
            if (loginListener != null) {
                loginListener.onError(PlatformType.WEIBO, -1, new Exception("启动ShareTransActivity失败"));
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int action = getIntent().getIntExtra("action", 0);
        if (action == 1) {
            new QQShare().startShare(this, sPlatform, sShareParams, sOnShareListener);
        } else if (action == 2) {
            new QQLogin().startLogin(this, sOnLoginListener);
        } else if (action == 3) {
            new SinaShare().startShare(this, sPlatform, sShareParams, sOnShareListener);
        } else if (action == 4) {
            new SinaLogin().startLogin(this, sOnLoginListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    boolean mCanFinish;

    @Override
    protected void onStop() {
        super.onStop();
        mCanFinish = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCanFinish) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 5000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
