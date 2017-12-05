package com.qiu.neteasemusic.Bean;

import com.qiu.neteasemusic.Base.BaseBean;

/**
 * Created by qiu on 2017/4/14.
 */

public class GridViewListViewBean extends BaseBean {
    private int ico;
    private int itemType;
    private String title;
    private String describe;

    public static final int ITEM_TYPE_GROOM_SONG=101;//推荐歌单

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getIco() {
        return ico;
    }

    public void setIco(int ico) {
        this.ico = ico;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
