package com.appdsn.commoncore.imageloader.core;

/**
 * @Desc: java类作用描述
 */
public class CornerPosition {
    public boolean mTopLeft = true;
    public boolean mTopRight = true;
    public boolean mBottomRight = true;
    public boolean mBottomLeft = true;

    public CornerPosition(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        mTopLeft = topLeft;
        mTopRight = topRight;
        mBottomRight = bottomRight;
        mBottomLeft = bottomLeft;
    }

    public boolean allCorner() {
        return mTopLeft && mTopRight && mBottomLeft && mBottomRight;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CornerPosition &&
                ((CornerPosition) obj).mTopLeft == mTopLeft &&
                ((CornerPosition) obj).mTopRight == mTopRight &&
                ((CornerPosition) obj).mBottomRight == mBottomRight &&
                ((CornerPosition) obj).mBottomLeft == mBottomLeft;
    }

    public int getKey() {
        return (mTopLeft ? 1 : 0) + (mTopRight ? 1 : 0) + (mBottomRight ? 1 : 0) + (mBottomLeft ? 1 : 0);
    }
}
