package com.appdsn.commonbase.widget.imageSelector;

import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:文件夹实体
 * <p>
 * Author: zhoutao
 * Date: 2019/10/15
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:zhoutao@appdsn.com
 * Update Comments:
 *
 * @author zhoutao
 */
public class ImageFloder {


    public List<ImageItem> images = new ArrayList<ImageItem>();
    /**
     * 图片的文件夹路径
     */
    private String dir;
    /**
     * 第一张图片的路径
     */
    private String firstImagePath = "";
    /**
     * 文件夹的名称
     */
    private String name;
    /**
     * 标识是图片还是视频
     */
    private int folderType;
    private int size;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        if (!TextUtils.isEmpty(name)) {
            return name.replace("/", "");
        } else {
            return name;
        }
    }

    public void setFolderTypeVideo(int folderType) {
        this.folderType = folderType;
    }

    public boolean isVideoFolder() {
        return folderType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}