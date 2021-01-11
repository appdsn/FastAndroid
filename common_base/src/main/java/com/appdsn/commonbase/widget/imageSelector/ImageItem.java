package com.appdsn.commonbase.widget.imageSelector;

import android.provider.MediaStore;

/**
 * Desc:图片实体
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
public class ImageItem {
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件类型
     */
    private int type;

    private long duration;

    public ImageItem(String path, int type, long duration) {
        this.path = path;
        this.type = type;
        this.duration = duration;
    }


    public String getPath() {
        return path;
    }

    /**
     * 是否是视频
     *
     * @return
     */
    public boolean isVideoFile() {
        return type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDuration() {
        return duration / 1000 / 60 + ":" + duration / 1000;
    }
}
