package com.appdsn.commoncore.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;


/**
 * Desc:shape 生成工具
 * <p>
 * Author: zhoutao
 * Date: 2019/10/30
 * Copyright: Copyright (c) 2016-2022
 * ""
 * <p>
 * Update Comments:
 *
 * @author zhoutao
 */
public class DrawableUtils {
    /**
     * 生成圆角图片
     *
     * @return
     */
    public static Drawable generateDrawable(int rgb, float radius) {
        radius = DisplayUtils.dp2px(radius);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置为矩形，默认就是矩形
        drawable.setColor(rgb);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    /**
     * 生成圆角图片
     *
     * @return
     */
    public static Drawable generateDrawable(String color, float radius) {
        radius = DisplayUtils.dp2px(radius);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置为矩形，默认就是矩形
        drawable.setColor(Color.parseColor(color));
        drawable.setCornerRadius(radius);
        return drawable;
    }

    /**
     * 生成渐变图片
     * 含圆角
     *
     * @return
     */
    public static Drawable generateDrawable(String startColorStr, String endColorStr, int roundRadius) {
        int startColor, endColor;

        roundRadius = DisplayUtils.dp2px(roundRadius);

        startColor = parseColor(startColorStr);
        endColor = parseColor(endColorStr);

        int colors[] = {startColor, endColor};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        drawable.setCornerRadius(roundRadius);
        return drawable;
    }

    /**
     * 生成渐变图片
     * 含自定义圆角
     *
     * @param radii         设置图片四个角圆形半径：1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
     * @param startColorStr 开始颜色
     * @param endColorStr   结束颜色
     * @return
     */
    public static Drawable generateDrawable(float[] radii, String startColorStr, String endColorStr) {
        int startColor, endColor;

        startColor = parseColor(startColorStr);
        endColor = parseColor(endColorStr);


        int colors[] = {startColor, endColor};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        drawable.setCornerRadii(radii);
        return drawable;
    }

    /**
     * 生成渐变圆角图片
     * 含描边
     *
     * @return
     */
    public static Drawable generateDrawable(String startColorStr, String endColorStr, int roundRadius, int strokeWidth, String strokeColorStr) {
        int startColor, endColor, strokeColor;

        strokeWidth = DisplayUtils.dp2px(strokeWidth);
        roundRadius = DisplayUtils.dp2px(roundRadius);

        strokeColor = parseColor(strokeColorStr);
        startColor = parseColor(startColorStr);
        endColor = parseColor(endColorStr);


        int colors[] = {startColor, endColor};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        drawable.setCornerRadius(roundRadius);
        drawable.setStroke(strokeWidth, strokeColor);
        return drawable;
    }

    /**
     * 生成空心圆角图片
     *
     * @param rgb
     * @param strokeWidth
     * @param radius
     * @return
     */
    public static Drawable generateHollowDrawable(int rgb, int strokeWidth, float radius) {

        strokeWidth = DisplayUtils.dp2px(strokeWidth);
        radius = DisplayUtils.dp2px(radius);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(strokeWidth, rgb);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    /**
     * 动态生成状态选择器
     *
     * @param pressed
     * @param normal
     * @return
     */
    public static Drawable generateSelector(Drawable pressed, Drawable normal) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);//设置按下的图片
        drawable.addState(new int[]{android.R.attr.state_enabled}, normal);//设置默认的图片
        drawable.addState(new int[]{}, normal);//设置默认的图片

        //设置状态选择器过度动画
        if (Build.VERSION.SDK_INT > 10) {
            drawable.setEnterFadeDuration(500);
            drawable.setExitFadeDuration(500);
        }

        return drawable;
    }

    /**
     * 颜色解析
     *
     * @param colorStr
     * @return
     */
    private static int parseColor(String colorStr) {
        try {
            return Color.parseColor(colorStr);
        } catch (Exception e) {
            return Color.parseColor("#000000");
        }
    }
}