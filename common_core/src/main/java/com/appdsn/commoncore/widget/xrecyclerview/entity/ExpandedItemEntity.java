package com.appdsn.commoncore.widget.xrecyclerview.entity;

import java.util.ArrayList;
import java.util.List;

public class ExpandedItemEntity extends MultiItemEntity {
    public int selected;//是否选中:0未选中，1选中，2半选中
    public boolean expanded = false;//是否展开该分组
    public long selectedChildSize;//该组所有选中子节点数量之和
    public long selfSize;//自身数量，如果当前是组：该组所有子节点量之和（选中和未选中），如果是子节点：自身的量之和
    private List<ExpandedItemEntity> childList;//该组孩子节点，如果本身就是孩子，设置为空即可
    private ExpandedItemEntity rootItemInfo;//最顶级分组信息

    public ExpandedItemEntity(int itemType) {
        super(itemType);
        rootItemInfo = this;//默认是自己本身
    }

    public List<ExpandedItemEntity> getChildList() {
        return childList;
    }

    /*如果加入当前节点的子节点，各个节点的rootGroupInfo都是第一个节点*/
    public void addChildItem(ExpandedItemEntity itemInfo) {
        if (itemInfo == null) {
            return;
        }
        if (childList == null) {
            childList = new ArrayList<>();
        }
        initRootItem(itemInfo);
        childList.add(itemInfo);
    }

    public void addChildItem(List<ExpandedItemEntity> allItems) {
        if (allItems == null) {
            return;
        }
        if (childList == null) {
            childList = new ArrayList<>();
        }
        for (int i = 0; i < allItems.size(); i++) {
            ExpandedItemEntity itemInfo = allItems.get(i);
            initRootItem(itemInfo);
            childList.add(itemInfo);
        }
    }

    private void initRootItem(ExpandedItemEntity itemInfo) {
        if (itemInfo != null) {
            itemInfo.rootItemInfo = rootItemInfo;
            if (itemInfo.hasChild()) {
                for (int i = 0; i < itemInfo.getChildList().size(); i++) {
                    initRootItem(itemInfo.getChildList().get(i));
                }
            }
        }
    }

    public boolean hasChild() {
        return childList != null && childList.size() > 0;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public long getSelectedChildSize() {
        return selectedChildSize;
    }

    public void setSelectedChildSize(long selectedChildSize) {
        this.selectedChildSize = selectedChildSize;
    }

    public long getSelfSize() {
        return selfSize;
    }

    public void setSelfSize(long selfSize) {
        this.selfSize = selfSize;
    }

    public ExpandedItemEntity getRootItem() {
        return rootItemInfo;
    }
}
