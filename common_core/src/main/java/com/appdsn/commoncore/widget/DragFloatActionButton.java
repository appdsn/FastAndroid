package com.appdsn.commoncore.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.appdsn.commoncore.base.BaseActivity;
import com.appdsn.commoncore.base.BaseFragment;
import com.appdsn.commoncore.utils.BarUtils;
import com.appdsn.commoncore.utils.ContextUtils;
import com.appdsn.commoncore.utils.DisplayUtils;


/**
 * @author fengpeihao
 * @date 2018/10/23
 */
public class DragFloatActionButton extends AppCompatImageView {
    private static int virtualBarHeight;
    private static int screenHeight = DisplayUtils.getScreenHeight();
    private static int screenWidth = DisplayUtils.getScreenWidth();
    private static int statusBarHeight = DisplayUtils.getStatusBarHeight();
    private Activity mActivity;
    private int lastX;
    private int lastY;
    private int firstX;
    private int firstY;
    private boolean isTouched;
    private boolean isMoved;
    private boolean isStickySide;
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBottom;
    private Rect mRect;
    private int maxBottom;
    private int maxTop;
    private int maxLeft;
    private int maxRight;

    public DragFloatActionButton(Context context) {
        super(context);
        init(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (Activity) context;
        //这里必须延时加载，因为window未加载完成时 不能判断是否显示底部导航栏
        this.post(new Runnable() {
            @Override
            public void run() {
                DragFloatActionButton.virtualBarHeight = BarUtils.getNavigationBarHeight(mActivity);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(getParent());
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouched = true;
                firstX = lastX = rawX;
                firstY = lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                if (this.mRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    //向右滑
                    if (dx > 0) {
                        if (getX() + getWidth() < screenWidth - marginRight) {
                            setX(Math.min(x, screenWidth - marginRight - getWidth()));
                        }
                    } else {//向左滑
                        if (getX() > marginLeft) {
                            setX(Math.max(x, marginLeft));
                        }
                    }
                    //向下滑
                    if (dy > 0) {
                        if (getY() < screenHeight - virtualBarHeight - marginBottom - getMeasuredHeight()) {
                            setY(Math.min(y, screenHeight - marginBottom - getMeasuredHeight()));
                        }
                    } else {//向上滑
                        if (getY() > statusBarHeight + marginTop) {
                            setY(Math.max(y, statusBarHeight + marginTop));
                        }
                    }
                }
                lastX = rawX;
                lastY = rawY;
                if (dx > 10 || dy > 10) {
                    isMoved = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouched = false;
                if (!isMoved && distance(firstX, firstY, event.getRawX(), event.getRawY()) < 10) {
                    callOnClick();
                }
                //防止快速滑动，导致滑动出界
                if (getY() < this.maxTop) {
                    setY(this.maxTop);
                }
                if (getY() > this.maxBottom - getMeasuredHeight()) {
                    setY(this.maxBottom - getMeasuredHeight());
                }
                if (getX() < this.maxLeft) {
                    setX(this.maxLeft);
                }
                if (getX() > this.maxRight - getMeasuredWidth()) {
                    setX(this.maxRight - getMeasuredWidth());
                }
                //是否移动false
                isMoved = false;
                //是否粘边
                if (this.isStickySide) {
                    this.stickySide();
                }
                break;
            default:
                break;
        }
        return isTouched || super.onTouchEvent(event);
    }

    private double distance(float startX, float startY, float endX, float endY) {
        return Math.sqrt(Math.pow(Math.abs(endX - startX), 2) + Math.pow(Math.abs(endY - startY), 2));
    }

    /**
     * 黏贴左右边
     */
    private void stickySide() {
        int x = (int) getX();
        int buttonWidth = this.getMeasuredWidth();
        if (x + buttonWidth / 2 > screenWidth / 2) {
            //右边
            setX(screenWidth - buttonWidth - marginRight);
        } else {
            //左边
            setX(marginLeft);
        }
    }

    private void requestDisallowInterceptTouchEvent(ViewParent viewParent) {
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(true);
            requestDisallowInterceptTouchEvent(viewParent.getParent());
        }
    }

    public boolean isTouched() {
        return isTouched;
    }

    /**
     * 可移动范围
     */
    private void updateRect() {
        this.maxBottom = screenHeight - virtualBarHeight - marginBottom;
        this.maxTop = statusBarHeight + marginTop;
        this.maxLeft = marginLeft;
        this.maxRight = screenWidth - marginRight;
        this.post(new Runnable() {
            @Override
            public void run() {
                DragFloatActionButton.this.mRect = new Rect(maxLeft, maxTop, maxRight, maxBottom);
            }
        });
    }


    /**
     * 构建悬浮按钮
     */
    public static class Builder {
        /**
         * 显示重心
         */
        private int mGravity;
        /**
         * 绑定activity上
         */
        BaseActivity mActivity;
        /**
         * 绑定fragment上
         */
        BaseFragment mFragment;
        DragFloatActionButton mFloatButton;
        /**
         * 点击监听
         */
        OnClickListener mClickListener;
        /**
         * 是否显示
         */
        boolean isShow = true;
        /**
         * 图片地址
         */
        String imageUrl;
        /**
         * 默认x轴坐标
         */
        int defaultX;
        /**
         * 默认y轴坐标
         */
        int defaultY;
        /**
         * 按钮宽度
         */
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        /**
         * 按钮高度
         */
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        /**
         * 显示按钮内容布局
         */
        ViewGroup mContentView;
        /**
         * 是否黏贴左右边框
         */
        boolean isStickySide;
        /**
         * 是否是圆形
         */
        boolean isCircle;
        /**
         * 左边距
         */
        int marginLeft;
        /**
         * 右边距
         */
        int marginRight;
        /**
         * 顶部边距
         */
        int marginTop;
        /**
         * 底部边距
         */
        int marginBottom;
        /**
         * 圆角的角度大小
         */
        int roundedCorner;
        /**
         * 图片缩放类型
         */
        private ScaleType mScaleType = ScaleType.CENTER_CROP;

        public Builder() {
            mGravity = 0;
        }

        public Builder setPosition(int x, int y) {
            this.defaultX = x;
            this.defaultY = y;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setMarginRight(int right) {
            this.marginRight = right;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        public Builder setMargin(int left, int top, int right, int bottom) {
            this.marginLeft = left;
            this.marginTop = top;
            this.marginRight = right;
            this.marginBottom = bottom;
            return this;
        }

        public Builder setMarginLeft(int left) {
            this.marginLeft = left;
            return this;
        }

        public Builder setMarginTop(int top) {
            this.marginTop = top;
            return this;
        }

        public Builder setMarginBottom(int bottom) {
            this.marginBottom = bottom;
            return this;
        }

        public Builder setImageUrl(String url) {
            this.imageUrl = url;
            return this;
        }

        public Builder setOnClickListener(OnClickListener listener) {
            this.mClickListener = listener;
            return this;
        }

        public Builder bindActivity(BaseActivity activity) {
            this.mActivity = activity;
            return this;
        }

        public Builder bindFragment(BaseFragment fragment) {
            this.mFragment = fragment;
            return this;
        }

        public Builder setContentView(ViewGroup contentView) {
            this.mContentView = contentView;
            return this;
        }

        public Builder isShow(boolean isShow) {
            this.isShow = isShow;
            return this;
        }

        public Builder isStickySide(boolean isStickySide) {
            this.isStickySide = isStickySide;
            return this;
        }

        public Builder isCircle(boolean isCircle) {
            this.isCircle = isCircle;
            return this;
        }

        public Builder setRounded(int roundedCorner) {
            this.roundedCorner = roundedCorner;
            return this;
        }

        public Builder setScaleType(ScaleType scaleType) {
            this.mScaleType = scaleType;
            return this;
        }

        public DragFloatActionButton create() {
            if (null == this.mFragment && null == this.mActivity) {
                throw new RuntimeException("You must bind an activity or fragment");
            }
            Context context = null == this.mActivity ? this.mFragment.getActivity() : this.mActivity;
            this.mFloatButton = new DragFloatActionButton(context);
            if (null == this.mContentView) {
                if (null == this.mActivity) {
                    this.mContentView = findContentView(this.mFragment.getView());
                }
                if (null == this.mFragment) {
                    this.mContentView = this.mActivity.findViewById(Window.ID_ANDROID_CONTENT);
                }
            }
            if (null == this.mContentView) {
                throw new RuntimeException("You must bind an activity or fragment");
            }

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.width, this.height);
            layoutParams.width = this.width;
            layoutParams.height = this.height;
            this.mContentView.addView(this.mFloatButton, layoutParams);
//            if (this.roundedCorner > 0) {
//                ImageUtil.getInstance().displayRoundPic(imageUrl, this.mFloatButton, this.roundedCorner);
//            } else if (this.isCircle) {
//                ImageUtil.getInstance().displayCircle(imageUrl, this.mFloatButton, R.drawable.common_dialog_bg);
//            } else {
//                ImageUtil.getInstance().display(imageUrl, this.mFloatButton, R.drawable.common_dialog_bg);
//            }
            this.mFloatButton.setBackground(ContextCompat.getDrawable(ContextUtils.getContext(), android.R.drawable.ic_menu_compass));
            this.mFloatButton.setScaleType(this.mScaleType);
            this.mFloatButton.bringToFront();
            this.mFloatButton.setVisibility(isShow ? VISIBLE : GONE);
            this.mFloatButton.isStickySide = this.isStickySide;
            this.mFloatButton.marginLeft = this.marginLeft;
            this.mFloatButton.marginRight = this.marginRight;
            this.mFloatButton.marginTop = this.marginTop;
            this.mFloatButton.marginBottom = this.marginBottom;
            if (null != this.mClickListener) {
                this.mFloatButton.setOnClickListener(this.mClickListener);
            }
            if (this.defaultX <= 0 || this.defaultY <= 0) {
                this.setPosition();
            } else {
                this.mFloatButton.setX(this.defaultX);
                this.mFloatButton.setY(this.defaultY);
            }
            this.mFloatButton.updateRect();
            return this.mFloatButton;
        }

        private void setPosition() {
            ViewGroup.LayoutParams params = this.mFloatButton.getLayoutParams();
            if (null == params) {
                return;
            }
            if (params instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) params).gravity = this.mGravity;
                ((FrameLayout.LayoutParams) params).topMargin = this.marginTop + BarUtils.getStatusBarHeight(mActivity);
                ((FrameLayout.LayoutParams) params).bottomMargin = this.marginBottom + virtualBarHeight;
                ((FrameLayout.LayoutParams) params).leftMargin = this.marginLeft;
                ((FrameLayout.LayoutParams) params).rightMargin = this.marginRight;
            }

            if (params instanceof ConstraintLayout.LayoutParams) {
                this.mFloatButton.post(new Runnable() {
                    @Override
                    public void run() {
                        int top, left;
                        if ((Builder.this.mGravity & Gravity.CENTER) == Gravity.CENTER) {
                            top = DisplayUtils.getScreenHeight() / 2 - Builder.this.mFloatButton.getMeasuredHeight() / 2;
                            left = DisplayUtils.getScreenWidth() / 2 - Builder.this.mFloatButton.getMeasuredWidth() / 2;
                            Builder.this.mFloatButton.setX(left);
                            Builder.this.mFloatButton.setY(top);
                        }
                        if ((Builder.this.mGravity & Gravity.TOP) == Gravity.TOP) {
                            Builder.this.mFloatButton.setY(Builder.this.marginTop + BarUtils.getStatusBarHeight(mActivity));
                        }
                        if ((Builder.this.mGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                            top = DisplayUtils.getScreenHeight() - Builder.this.mFloatButton.getMeasuredHeight();
                            Builder.this.mFloatButton.setY(top - Builder.this.marginBottom - virtualBarHeight);
                        }
                        if ((Builder.this.mGravity & Gravity.RIGHT) == Gravity.RIGHT) {
                            left = DisplayUtils.getScreenWidth() - Builder.this.mFloatButton.getMeasuredWidth();
                            Builder.this.mFloatButton.setX(left - Builder.this.marginRight);
                        }
                        if ((Builder.this.mGravity & Gravity.LEFT) == Gravity.LEFT) {
                            Builder.this.mFloatButton.setX(Builder.this.marginLeft);
                        }
                    }
                });
            }
            if (params instanceof RelativeLayout.LayoutParams) {
                if ((this.mGravity & Gravity.CENTER) == Gravity.CENTER) {
                    ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.CENTER_IN_PARENT);
                }
                if ((this.mGravity & Gravity.TOP) == Gravity.TOP) {
                    ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_TOP);
                }
                if ((this.mGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                    ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }
                if ((this.mGravity & Gravity.RIGHT) == Gravity.RIGHT) {
                    ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                if ((this.mGravity & Gravity.LEFT) == Gravity.LEFT) {
                    ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                ((RelativeLayout.LayoutParams) params).topMargin = this.marginTop + statusBarHeight;
                ((RelativeLayout.LayoutParams) params).bottomMargin = this.marginBottom + screenHeight;
                ((RelativeLayout.LayoutParams) params).leftMargin = this.marginLeft;
                ((RelativeLayout.LayoutParams) params).rightMargin = this.marginRight;
            }
        }

        private ViewGroup findContentView(View contentView) {
            while (null != contentView) {
                if (contentView.getClass().isAssignableFrom(FrameLayout.class)
                        || contentView.getClass().isAssignableFrom(RelativeLayout.class)
                        || contentView.getClass().isAssignableFrom(ConstraintLayout.class)) {
                    return (ViewGroup) contentView;
                }
                contentView = (View) contentView.getParent();
            }
            return null;
        }
    }
}

