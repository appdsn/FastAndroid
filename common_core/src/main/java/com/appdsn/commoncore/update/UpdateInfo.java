package com.appdsn.commoncore.update;


import java.io.Serializable;

/**
 * Created by tie on 2017/6/16.
 */

public class UpdateInfo implements Serializable {

    private String versionName;
    private boolean isUpdate;
    private String downloadUrl;
    private boolean isForcedUpdate;
    private String changeTitle;
    private String changeContent;
    private String md5String;
    private int updateLayoutId;
    private int downloadLayoutId;
    private int logoIcon;

    public int getLogoIcon() {
        return logoIcon;
    }

    public void setLogoIcon(int logoIcon) {
        this.logoIcon = logoIcon;
    }

    public String getMd5String() {
        return md5String;
    }

    public void setMd5String(String md5String) {
        this.md5String = md5String;
    }

    public int getUpdateLayoutId() {
        return updateLayoutId;
    }

    public void setUpdateLayoutId(int updateLayoutId) {
        this.updateLayoutId = updateLayoutId;
    }

    public int getDownloadLayoutId() {
        return downloadLayoutId;
    }

    public void setDownloadLayoutId(int downloadLayoutId) {
        this.downloadLayoutId = downloadLayoutId;
    }

    public boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean getIsForcedUpdate() {
        return isForcedUpdate;
    }

    public void setIsForcedUpdate(boolean isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }

    public String getChangeTitle() {
        return changeTitle;
    }

    public void setChangeTitle(String changeTitle) {
        this.changeTitle = changeTitle;
    }

    public String getChangeContent() {
        return changeContent;
    }

    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
    }

    public String getVersionName() {
        return versionName;
    }

    public UpdateInfo(boolean isUpdate, String downloadUrl, boolean isForcedUpdate, String changeTitle, String changeContent, String md5String, int updateLayoutId, int downloadLayoutId, String versionName, int icon) {
        this.isUpdate = isUpdate;
        this.downloadUrl = downloadUrl;
        this.isForcedUpdate = isForcedUpdate;
        this.changeTitle = changeTitle;
        this.changeContent = changeContent;
        this.md5String = md5String;
        this.updateLayoutId = updateLayoutId;
        this.downloadLayoutId = downloadLayoutId;
        this.versionName = versionName;
        this.logoIcon = icon;
    }
}
