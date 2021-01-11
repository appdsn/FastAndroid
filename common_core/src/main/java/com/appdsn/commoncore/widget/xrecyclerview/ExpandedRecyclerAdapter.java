package com.appdsn.commoncore.widget.xrecyclerview;

import android.content.Context;

import com.appdsn.commoncore.widget.xrecyclerview.entity.ExpandedItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:分组可收缩布局
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/30 10:10
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public abstract class ExpandedRecyclerAdapter extends MultiRecyclerAdapter<ExpandedItemEntity> {
    List<ExpandedItemEntity> mOriginDatas;

    public ExpandedRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public void setData(List<? extends ExpandedItemEntity> datas) {
        if (datas != null) {
            mOriginDatas = (List<ExpandedItemEntity>) datas;
            mDatas.clear();
            mDatas.addAll(datas);
            initData();
            notifyDataSetChanged();
        }
    }

    @Override
    public List<ExpandedItemEntity> getData() {
        return mOriginDatas;
    }

    private void initData() {
        for (int i = 0; i < mDatas.size(); i++) {
            ExpandedItemEntity itemInfo = mDatas.get(i);
            /*更新选中状态*/
            updateSelectedData(itemInfo);

            /*更新展开状态*/
            if (itemInfo == null || !itemInfo.hasChild() || !itemInfo.expanded) {
                continue;
            }

            List<ExpandedItemEntity> childList = getAllExpandedChildList(itemInfo);
            mDatas.addAll(i + 1, childList);
        }
    }

    public void selectAll(boolean selected) {
        for (int i = 0; i < mOriginDatas.size(); i++) {
            ExpandedItemEntity itemInfo = mOriginDatas.get(i);
            itemInfo.selected = selected ? 1 : 0;
            if (itemInfo.hasChild()) {
                selectAll(itemInfo.getChildList(), selected);
            }
            /*更新选中状态*/
            updateSelectedData(itemInfo);
        }
        notifyDataSetChanged();
    }

    private void selectAll(List<ExpandedItemEntity> targetList, boolean selected) {
        for (int i = 0; i < targetList.size(); i++) {
            ExpandedItemEntity itemInfo = targetList.get(i);
            itemInfo.selected = selected ? 1 : 0;
            if (itemInfo.hasChild()) {
                selectAll(itemInfo.getChildList(), selected);
            }
        }
    }

    private void removeData(List<ExpandedItemEntity> targetList, ExpandedItemEntity targetInfo) {
        targetList.remove(targetInfo);
        for (int i = 0; i < targetList.size(); i++) {
            ExpandedItemEntity itemInfo = targetList.get(i);
            if (itemInfo.hasChild()) {
                removeData(itemInfo.getChildList(), targetInfo);
            }
        }
    }

    public void removeData(ExpandedItemEntity targetInfo) {
        removeData(mOriginDatas, targetInfo);
        setData(mOriginDatas);
    }

    public void removeSelectedData() {
        removeSelectedData(mOriginDatas);
        setData(mOriginDatas);
    }

    private void removeSelectedData(List<ExpandedItemEntity> targetList) {
        for (int i = 0; i < targetList.size(); i++) {
            ExpandedItemEntity itemInfo = targetList.get(i);
            if (itemInfo.selected == 1) {
                targetList.remove(i);
                i--;
            } else {
                if (itemInfo.hasChild()) {
                    removeSelectedData(itemInfo.getChildList());
                }
            }
        }
    }

    private List<ExpandedItemEntity> getSelectedData(List<ExpandedItemEntity> targetList) {
        List<ExpandedItemEntity> selectedList = new ArrayList<>();
        for (int i = 0; i < targetList.size(); i++) {
            ExpandedItemEntity itemInfo = targetList.get(i);
            if (itemInfo.hasChild()) {
                selectedList.addAll(getSelectedData(itemInfo.getChildList()));
            } else {
                if (itemInfo.selected == 1) {
                    selectedList.add(itemInfo);
                }
            }
        }
        return selectedList;
    }

    public List<ExpandedItemEntity> getSelectedData() {
        return getSelectedData(mOriginDatas);
    }

    public long getSelectedSize() {
        List<ExpandedItemEntity> selectedList = getSelectedData(mOriginDatas);
        long allSize = 0l;
        for (int i = 0; i < selectedList.size(); i++) {
            allSize += selectedList.get(i).selfSize;
        }
        return allSize;
    }

    //取消或者选中全部子项
    private void updateSelectedData(ExpandedItemEntity itemData) {
        if (itemData.hasChild()) {
            List<ExpandedItemEntity> childList = itemData.getChildList();
            int selected = -1;
            itemData.selectedChildSize = 0;
            itemData.selfSize = 0;
            for (int i = 0; i < childList.size(); i++) {
                ExpandedItemEntity childItem = childList.get(i);
                updateSelectedData(childItem);

                if (childItem.selected == 2) {
                    selected = childItem.selected;
                }

                if (selected != 2) {
                    if (selected == 1 && childItem.selected == 0) {
                        selected = 2;
                    } else if (selected == 0 && childItem.selected == 1) {
                        selected = 2;
                    } else {
                        selected = childItem.selected;
                    }
                }
                itemData.selectedChildSize += childItem.selectedChildSize;
                itemData.selfSize += childItem.selfSize;
            }
            itemData.selected = selected;
        } else {
            if (itemData.selected == 1) {
                itemData.selectedChildSize = itemData.selfSize;
            } else {
                itemData.selectedChildSize = 0;
            }
        }
    }

    /*收缩该分组子节点*/
    public void expandGroup(ExpandedItemEntity groupData, int position) {
        if (groupData.hasChild()) {
            List<ExpandedItemEntity> childList = getAllExpandedChildList(groupData);
            if (groupData.expanded) {
                groupData.expanded = false;
                mDatas.removeAll(childList);
            } else {
                groupData.expanded = true;
                mDatas.addAll(position + 1, childList);
            }
            notifyDataSetChanged();
        }
    }

    private List<ExpandedItemEntity> getAllExpandedChildList(ExpandedItemEntity itemInfo) {
        List<ExpandedItemEntity> allChildList = new ArrayList<>();
        for (int i = 0; i < itemInfo.getChildList().size(); i++) {
            ExpandedItemEntity childItem = itemInfo.getChildList().get(i);
            allChildList.add(childItem);
            if (childItem.expanded && childItem.hasChild()) {
                allChildList.addAll(getAllExpandedChildList(childItem));
            }
        }
        return allChildList;
    }

    /*收缩该分组子节点*/
    private void expandGroup2(ExpandedItemEntity groupData, int position) {
        if (groupData.hasChild()) {
            List<ExpandedItemEntity> childList = groupData.getChildList();


            if (groupData.expanded) {
                groupData.expanded = false;
                mDatas.removeAll(childList);
            } else {
                groupData.expanded = true;
                mDatas.addAll(position + 1, childList);
            }
            notifyDataSetChanged();
        }
    }

    //取消或者选中全部子项
    public void selectChild(ExpandedItemEntity itemData) {
        updateChildSelected(itemData, itemData.selected == 1 ? 0 : 1);
        //重新计算选中的数据
        updateSelectedData(itemData.getRootItem());
        notifyDataSetChanged();
    }

    private void updateChildSelected(ExpandedItemEntity itemData, int selected) {
        itemData.selected = selected;
        if (itemData.hasChild()) {
            List<ExpandedItemEntity> childList = itemData.getChildList();
            for (int i = 0; i < childList.size(); i++) {
                updateChildSelected(childList.get(i), selected);
            }
        }
    }
}
