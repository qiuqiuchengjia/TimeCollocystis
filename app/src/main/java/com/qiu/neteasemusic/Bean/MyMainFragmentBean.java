package com.qiu.neteasemusic.Bean;

import com.qiu.neteasemusic.Base.BaseBean;

/**
 * Created by qiu on 2017/4/11.
 */

public class MyMainFragmentBean extends BaseBean{
    private int itemType;
    private String ItemName,ItemNumName;
    private int itemIco;
    public static int ITEM_TYPE_LOCAL_MUSIC=1;//本地音乐
    public static int ITEM_TYPE_LATELY_PLAY=2;//最近播放
    public static int ITEM_TYPE_DJ=3;//电台
    public static int ITEM_TYPE_DOWNLOAD=4;//下载管理
    public static int ITEM_TYPE_COLLECT=5;//我的收藏

    public String getItemNumName() {
        return ItemNumName;
    }

    public void setItemNumName(String itemNumName) {
        ItemNumName = itemNumName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getItemIco() {
        return itemIco;
    }

    public void setItemIco(int itemIco) {
        this.itemIco = itemIco;
    }


}
