/*
 * Copyright 2016 czy1121
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appdsn.commoncore.update;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appdsn.commoncore.R;
import com.appdsn.commoncore.update.listener.IDownloadAgent;
import com.appdsn.commoncore.update.listener.IUpdateAgent;
import com.appdsn.commoncore.update.listener.IUpdateDownloader;
import com.appdsn.commoncore.update.listener.IUpdatePrompter;
import com.appdsn.commoncore.update.listener.OnCancelListener;
import com.appdsn.commoncore.update.listener.OnDownloadListener;
import com.appdsn.commoncore.update.listener.OnFailureListener;
import com.appdsn.commoncore.utils.DisplayUtils;
import com.appdsn.commoncore.utils.NotificationUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.List;

public class UpdateAgent implements IUpdateAgent, IDownloadAgent {

    private static boolean mForce;
    /**
     * 是否有md5校验
     */
    private final boolean mMd5Check;
    private Activity mActivity;
    private File mTmpFile;
    private File mApkFile;

    private static UpdateInfo mUpdateInfo;
    private UpdateError mError = null;

    //private IUpdateChecker mChecker = new DefaultUpdateChecker();
    private IUpdateDownloader mDownloader;
    private IUpdatePrompter mPrompter;

    private OnFailureListener mOnFailureListener;

    private OnDownloadListener mOnDownloadListener;
    private OnDownloadListener mOnNotificationDownloadListener;

    private OnCancelListener mOnCancelListener;

    public UpdateAgent(Activity activity, UpdateInfo updateInfo, OnCancelListener onCancelListener) {
        mActivity = activity;
        mUpdateInfo = updateInfo;
        mOnCancelListener = onCancelListener;
        mDownloader = new DefaultUpdateDownloader(mActivity);
        mPrompter = new DefaultUpdatePrompter(mActivity);
        mOnFailureListener = new DefaultFailureListener(activity);
        mOnDownloadListener = new DefaultDialogDownloadListener(activity);
        //是否强更
        mForce = updateInfo.getIsForcedUpdate();

        mMd5Check = !TextUtils.isEmpty(mUpdateInfo.getMd5String());

        mOnNotificationDownloadListener = new DefaultNotificationDownloadListener(mActivity, 1);

    }

    public static class Builder {

        private String downloadUrl;
        private boolean isForceUpdate;
        private String md5String;
        private String title;
        private String content;
        private int updateLayoutId;
        private int downloadLayoutId;
        private UpdateInfo mUpdateInfo;
        private boolean isUpdate;
        private Activity mActivity;
        private OnCancelListener mOnCancelListener;
        private String versionName;
        private int logoIcon;

        public Builder setNotifyIcon(int logoIcon) {
            this.logoIcon = logoIcon;
            return this;
        }


        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder with(Activity activity) {
            mActivity = activity;
            return this;
        }

        public Builder setUpdate(boolean update) {
            isUpdate = update;
            return this;
        }

        public Builder setUpdateLayoutId(@LayoutRes int updateLayoutId) {
            this.updateLayoutId = updateLayoutId;
            return this;
        }

        public Builder setDownloadLayoutId(@LayoutRes int downloadLayoutId) {
            this.downloadLayoutId = downloadLayoutId;
            return this;
        }

        public Builder setUpdateInfo(UpdateInfo updateInfo) {
            mUpdateInfo = updateInfo;
            return this;
        }

        public Builder setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder setForceUpdate(boolean forceUpdate) {
            isForceUpdate = forceUpdate;
            return this;
        }

        public Builder setMd5String(String md5String) {
            this.md5String = md5String;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setVersionName(String version) {
            this.versionName = version;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public UpdateAgent build() {

            if (mUpdateInfo == null) {
                mUpdateInfo = new UpdateInfo(isUpdate, downloadUrl, isForceUpdate, title, content, md5String, updateLayoutId, downloadLayoutId, versionName, logoIcon);
            }

            if (mActivity == null) {
                throw new IllegalArgumentException("Activity不能为空");
            }

            if (TextUtils.isEmpty(mUpdateInfo.getDownloadUrl())) {
                throw new IllegalArgumentException("下载链接不能为空");
            }

            return new UpdateAgent(mActivity, mUpdateInfo, mOnCancelListener);
        }
    }

    public void dissmiss() {
        if (mPrompter != null) {
            mPrompter.dismiss();
        }
    }

    @Override
    public void setError(UpdateError error) {
        mError = error;
    }

    /**
     * 开始更新
     */
    @Override
    public void update() {
//        mApkFile = new File(UpdateUtil.getFilePath(mContext));
        mApkFile = UpdateUtil.makeFile(mActivity);

        if (UpdateUtil.verify(mApkFile, mUpdateInfo.getMd5String())) {
            doInstall();
        } else {
            doDownload();
        }
        //关闭弹窗
        mPrompter.dismiss();
    }

    @Override
    public OnCancelListener getCancelListener() {
        return mOnCancelListener;
    }

    /**
     * 开始下载
     */
    @Override
    public void onStart() {
        if (mForce) {
            //强更弹出下载对话框
            mOnDownloadListener.onStart();
        } else {
            //不抢更弹出notification
            mOnNotificationDownloadListener.onStart();
        }
    }

    /**
     * 下载的进度
     *
     * @param progress
     */
    @Override
    public void onProgress(int progress) {
        if (mForce) {
            mOnDownloadListener.onProgress(progress);
        } else {
            mOnNotificationDownloadListener.onProgress(progress);
        }
    }

    /**
     * 下载结束
     */
    @Override
    public void onFinish() {
        if (mForce) {
            mOnDownloadListener.onFinish();
        } else {
            mOnNotificationDownloadListener.onFinish();
        }
        if (mError != null) {
            mOnFailureListener.onFailure(mError);
        } else {
            mTmpFile.renameTo(mApkFile);
            doInstall();
        }

    }

    //弹出更新弹窗。。
    public void check() {
        UpdateError error = mError;
        if (error != null) {
            doFailure(error);
        } else {
            if (mUpdateInfo == null) {
                doFailure(new UpdateError(UpdateError.CHECK_UNKNOWN));
            } else {
                mApkFile = UpdateUtil.makeFile(mActivity, mMd5Check);
                mTmpFile = new File(UpdateUtil.getTempPath(mActivity));
                doPrompt();
            }
        }

    }

    //弹窗操作
    void doPrompt() {
        mPrompter.prompt(this);
    }

    //下载操作
    void doDownload() {
        mDownloader.download(this, mUpdateInfo.getDownloadUrl(), mTmpFile);
    }

    //安装操作
    void doInstall() {
        UpdateUtil.install(mActivity, mApkFile, mForce);
    }

    //失败
    void doFailure(UpdateError error) {
        if (error.isError()) {
            mOnFailureListener.onFailure(error);
        }
    }

    //下载
    private static class DefaultUpdateDownloader implements IUpdateDownloader {
        final Context mContext;

        public DefaultUpdateDownloader(Context context) {
            mContext = context;
        }

        @Override
        public void download(IDownloadAgent agent, String url, File temp) {
            new UpdateDownloader(agent, mContext, url, temp, mUpdateInfo.getMd5String()).execute();
        }
    }


    //更新弹窗
    private static class DefaultUpdatePrompter implements IUpdatePrompter {

        private Activity mActivity;

        private TextView update_content;
        private TextView update_id_ok;
        private ImageView update_id_cancel;
        private TextView update_version_title;

        private Dialog dialog;

        public DefaultUpdatePrompter(Activity context) {
            mActivity = context;
        }


        @Override
        public void prompt(final IUpdateAgent agent) {
            if (mActivity.isFinishing()) {
                return;
            }
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            dialog = new Dialog(mActivity, R.style.BaseDialogTheme);

            //dialog.setTitle("应用更新");

            View inflate = View.inflate(mActivity, mUpdateInfo.getUpdateLayoutId(), null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (DisplayUtils.getScreenWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;

            this.dialog.setContentView(inflate, params);

            update_content = inflate.findViewById(R.id.update_content);
            update_id_ok = inflate.findViewById(R.id.update_id_ok);
            update_id_cancel = inflate.findViewById(R.id.update_id_cancel);
            update_version_title = inflate.findViewById(R.id.update_title);
            //设置内容
            String temp = mUpdateInfo.getChangeContent().replace("\\n", "\n");

            if (update_content != null) {
                update_content.setText(temp);
            }

            if (update_version_title != null) {
                update_version_title.setText(mUpdateInfo.getChangeTitle());
            }
            //设置点击监听
            //升级
            if (update_id_ok != null) {
                update_id_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        requestPermission(agent);
                        //开始更新
                        agent.update();
                    }
                });
            }
            //取消
            update_id_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (mForce) {
                //弹窗无法关闭
                update_id_cancel.setVisibility(View.GONE);
            }
            dialog.setCancelable(false);
            this.dialog.show();

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (!mForce) {
                        if (agent.getCancelListener() != null) {
                            agent.getCancelListener().onCancel();
                        }
                    }
                }
            });
        }

        @Override
        public void dismiss() {
            dialog.dismiss();
        }

        /**
         * android 6.0请求写入sd权限
         *
         * @param agent
         */
        public void requestPermission(final IUpdateAgent agent) {
            AndPermission.with(mActivity)
                    .runtime()
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                            //开始更新
                            agent.update();
                        }
                    }).onDenied(new Action<List<String>>() {
                @Override
                public void onAction(List<String> permissions) {
                    // TODO what to do
                }
            })
                    .start();
        }

    }

    //失败
    private static class DefaultFailureListener implements OnFailureListener {

        private Context mContext;

        public DefaultFailureListener(Context context) {
            mContext = context;
        }

        @Override
        public void onFailure(UpdateError error) {
            UpdateUtil.log(error.toString());
            Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 下载进度条对话框回调
     */
    private static class DefaultDialogDownloadListener implements OnDownloadListener {
        private Context mContext;
        private Dialog mDialog;
        private ProgressBar pgBar;
        private TextView tvPg;

        private TextView update_content;
        private ImageView update_id_cancel;
        private TextView update_version_title;

        public DefaultDialogDownloadListener(Context context) {
            mContext = context;
        }

        /**
         * 开始下载
         */
        @Override
        public void onStart() {
            if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                final Dialog dialog = new Dialog(mContext, R.style.BaseDialogTheme);

                View inflate = View.inflate(mContext, mUpdateInfo.getDownloadLayoutId(), null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (DisplayUtils.getScreenWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                dialog.setContentView(inflate, params);

                pgBar = (ProgressBar) inflate.findViewById(R.id.update_progress_bar);
                tvPg = (TextView) inflate.findViewById(R.id.update_progress_text);

                update_content = inflate.findViewById(R.id.update_content);
                update_id_cancel = inflate.findViewById(R.id.update_id_cancel);
                update_version_title = inflate.findViewById(R.id.update_title);

                //设置内容
                String temp = mUpdateInfo.getChangeContent().replace("\\n", "\n");
                if (update_content != null) {
                    update_content.setText(temp);
                }
                if (update_version_title != null) {
                    update_version_title.setText(mUpdateInfo.getChangeTitle());
                }

                if (mForce) {
                    //弹窗无法关闭
                    if (update_id_cancel != null) {
                        update_id_cancel.setVisibility(View.GONE);
                    }
                }
                dialog.setCancelable(false);
                if (update_id_cancel != null) {
                    //取消
                    update_id_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }

                dialog.show();
                mDialog = dialog;
            }
        }

        /**
         * 进度回调
         *
         * @param i
         */
        @Override
        public void onProgress(int i) {
            if (mDialog != null) {
                if (pgBar != null) {
                    pgBar.setProgress(i);
                }
                if (tvPg != null) {
                    tvPg.setText(i + "%");
                }
            }
        }

        /**
         * 下载完成以后回调
         */
        @Override
        public void onFinish() {
            if (mDialog != null) {
                if (mContext == null) {
                    return;
                }
                if (!(mContext instanceof Activity)) {
                    return;
                }
                if (((Activity) mContext).isDestroyed()) {
                    return;
                }
                mDialog.dismiss();
                mDialog = null;
            }
        }
    }

    //Notification
    private static class DefaultNotificationDownloadListener implements OnDownloadListener {
        private Context mContext;
        private int mNotifyId;
        private NotificationCompat.Builder mBuilder;

        public DefaultNotificationDownloadListener(Context context, int notifyId) {
            mContext = context;
            mNotifyId = notifyId;
        }

        @Override
        public void onStart() {
            if (mBuilder == null) {
                String title = "下载中 - " + mContext.getString(mContext.getApplicationInfo().labelRes);
                mBuilder = NotificationUtils.getNotification(title, "", mUpdateInfo.getLogoIcon());
                mBuilder.setSound(null);
                mBuilder.setVibrate(null);
                mBuilder.setAutoCancel(false);
            }
            onProgress(0);
        }

        @Override
        public void onProgress(int progress) {
            if (mBuilder != null) {
                if (progress > 0) {
                    mBuilder.setPriority(Notification.PRIORITY_LOW);
                    mBuilder.setDefaults(0);
                }
                mBuilder.setProgress(100, progress, false);

                NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(mNotifyId, mBuilder.build());
            }
        }

        @Override
        public void onFinish() {
            //下载完成后更改布局，点击安装
            NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            //nm.cancel(mNotifyId);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.fromFile(new File(UpdateUtil.getFilePath(mContext))), "application/vnd.android.package-archive");
            } else {
                Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".updatefileprovider", new File(UpdateUtil.getFilePath(mContext)));
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                mBuilder.setContentTitle("下载完成").setContentText("点击安装").setContentIntent(pendingIntent).setProgress(100, 100, false).setAutoCancel(true);
                nm.notify(mNotifyId, mBuilder.build());

                File file = new File(UpdateUtil.getFilePath(mContext));
                if (!file.exists()) {
                    nm.cancel(mNotifyId);
                }
            } catch (Exception e) {

            }


        }
    }
}