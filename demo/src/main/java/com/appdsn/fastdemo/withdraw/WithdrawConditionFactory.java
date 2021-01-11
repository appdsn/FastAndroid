package com.appdsn.fastdemo.withdraw;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/11/16 13:52
 */
public class WithdrawConditionFactory {
    public static boolean isisAchieve(int conditionId) {
        if (conditionId == 1) {
            return new GoldCountCondition().isAchieve();
        }

        return false;
    }
}
