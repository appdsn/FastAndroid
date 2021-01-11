package com.appdsn.commonbase.statistics;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/12/26 14:24
 */
public class StatisticsPage {
    private String mSourcePage;
    private String mCurrentPage;

    public StatisticsPage(String sourcePage, String currentPage) {
        mSourcePage = sourcePage;
        mCurrentPage = currentPage;
    }

    public String getCurPageId() {
        return mCurrentPage;
    }

    public String getSourcePageId() {
        return mSourcePage;
    }

    public void setSourcePageId(String sourcePage) {
        mSourcePage = sourcePage;
    }
}
