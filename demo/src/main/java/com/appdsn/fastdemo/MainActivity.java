package com.appdsn.fastdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commoncore.event.BindEventBus;
import com.appdsn.commoncore.event.EventMessage;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.SingleRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.commoncore.widget.xrecyclerview.decoration.GridSpacingItemDecoration;
import com.appdsn.fastdemo.base.BaseDemoActivity;
import com.appdsn.fastdemo.bean.MainItemInfo;
import com.appdsn.fastdemo.http.HttpDemoActivity;
import com.appdsn.fastdemo.imageloader.ImageLoaderDemoActivity;
import com.appdsn.fastdemo.imageselect.ImageSelectActivityGuide;
import com.appdsn.fastdemo.indicator.MainIndicatorActivity;
import com.appdsn.fastdemo.loginshare.LoginShareActivity;
import com.appdsn.fastdemo.weather.SetCityActivity;
import com.appdsn.fastdemo.weather.entity.EventCode;
import com.appdsn.fastdemo.weather.rxhttp.RxHttpHelper;
import com.appdsn.fastdemo.xrecyclerview.XRecyclerViewActivity;

import java.util.ArrayList;

@BindEventBus
public class MainActivity extends BaseAppActivity {

    XRecyclerView mRecyclerView;
    ArrayList<MainItemInfo> mDataList = new ArrayList<>();
    private SingleRecyclerAdapter<MainItemInfo> mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
//        hideTitleBar();
        mRecyclerView = findViewById(R.id.xrecyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
//        mRecyclerView.addItemDecoration(CommonItemDecoration.newBuilder(this)
//                .color(Color.LTGRAY)
////                .dashWidth(8)
////                .dashGap(5)
////                .thickness(6)
//                .gridBottomVisible(true) //control bottom border
//                .gridTopVisible(false) //control top border
//                .gridLeftVisible(true) //control left border
//                .gridRightVisible(true) //control right border
//                .gridHorizontalSpacing(1)
//                .gridVerticalSpacing(1)
//                .create());

        GridSpacingItemDecoration itemDecorationFilter
                = new GridSpacingItemDecoration(50);
        mRecyclerView.addItemDecoration(itemDecorationFilter);
    }

    @Override
    protected void initStatusBar() {
        setStatusBarTranslucent(true);
    }

    @Override
    protected void setListener() {
        mRecyclerView.setOnItemClickListener(new XRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                mDataList.get(position).jump(mContext);
//                LogConfig.init("MigrationHelper", true, false);
//                LogUtils.list("123", DbHelper.getTableInfo(GreenDaoManager.getInstance().getDatabase(), "USER"));
//                try {
//                    User user = new User();
//                    user.setMemberSex(2);
//                    user.setNickname("昵称2");
//                    user.setPhone("000000000");
//                    GreenDaoManager.getInstance().getDaoSession().getUserDao().save(user);
//                    Log.i("MigrationHelper", "插入一条数据：" + new Gson().toJson(user));
//
//                    List<User> userList = GreenDaoManager.getInstance().getDaoSession().getUserDao().loadAll();
//                    Log.i("MigrationHelper", "查询出所有数据:" + new Gson().toJson(userList));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public boolean onItemLongClick(View itemView, int position) {
                return false;
            }
        });

        mAdapter = new SingleRecyclerAdapter<MainItemInfo>(mContext, R.layout.item_main_action) {
            @Override
            public void convert(CommonViewHolder holder, MainItemInfo itemData, int position) {
                holder.setText(R.id.tvAction, itemData.title);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        mDataList.add(new MainItemInfo("Base基类的使用", BaseDemoActivity.class));
        mDataList.add(new MainItemInfo("HTTP网络框架的使用", HttpDemoActivity.class));
        mDataList.add(new MainItemInfo("图片加载框架的使用", ImageLoaderDemoActivity.class));
        mDataList.add(new MainItemInfo("XRecyclerView的使用", XRecyclerViewActivity.class));
        mDataList.add(new MainItemInfo("图片选择器的使用", ImageSelectActivityGuide.class));
        mDataList.add(new MainItemInfo("SmartIndicator使用", MainIndicatorActivity.class));
        mDataList.add(new MainItemInfo("登录/分享", LoginShareActivity.class));
        mDataList.add(new MainItemInfo("城市设置", SetCityActivity.class));
        mAdapter.setData(mDataList);
    }

    /**
     * 收到添加关注城市的事件，需要做：
     * 1：根据areaCode请求远程接口数据，并更新当前界面数据
     * 2：将该城市的数据缓存到关注城市表
     * 3: 刷新关注城市列表的myAdapter
     */
    @Override
    public void onReceiveEvent(EventMessage event) {
        super.onReceiveEvent(event);
        if (event.getCode() == EventCode.EVENT_CODE_ADD_ATTENTION_CITY) {
            RxHttpHelper.requestAttentionCityWeather(null);
        }
    }


}
