package com.appdsn.commonbase.config;


/**
 * Desc:路由地址
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/12
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 * 注意事项！！！  /模块/页面名称   或  /模块/子模块../页面名称
 * 至少两级  例子 /商城/商品详情 /shop/shopDetails
 * 【必须继承BaseRouterPath】
 *
 * @author anyabo
 */
public interface RouterPath {
    /**
     * 首页
     */
    String MAIN_ACTIVITY = "/main/main";


}
