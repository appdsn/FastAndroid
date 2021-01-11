package com.appdsn.commonbase.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.just.agentweb.BaseIndicatorView;


/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/10/22 15:51
 */
public class WebLoadingIndicator extends BaseIndicatorView {
//    private LottieAnimationView likeAnimation;

    public WebLoadingIndicator(Context context) {
        super(context);
//        View content = inflate(context, R.layout.loading_view, this);
//        likeAnimation = content.findViewById(R.id.likeAnimation);
    }

    @Override
    public void show() {
        this.setVisibility(View.VISIBLE);
//        if (likeAnimation != null) {
//            likeAnimation.playAnimation();
//        }
    }

    @Override
    public void setProgress(int newProgress) {
    }

    @Override
    public void hide() {
        this.setVisibility(View.GONE);
//        if (likeAnimation != null) {
//            likeAnimation.cancelAnimation();
//        }
    }

    @Override
    public LayoutParams offerLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
