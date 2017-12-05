package com.framework.greendroid.imagepicker.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class ItemModel implements Serializable{
    private static final long serialVersionUID = 1L;

    public String mPath;
    public String mThumbnail = null;
    public boolean isSeleted = false;
    public boolean isFunctionItem = false;
    public Drawable functionItemDrawale = null;
    public int tag;
    public int isUpload = 0;
    public int isError = 0;//0:失败 1:成功
    public int isAdd = 0;
    public String uploadUrl = null;
    public String uploadMd = null;
    public String ids;
    public HotelDetailBean hotelDetailBean;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
//
//    public ItemModel(){
//
//    }
//
//    private ItemModel(Parcel parcel){
//        mPath = parcel.readString();
//        mThumbnail = parcel.readString();
//    }
//
//    public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>() {
//
//        @Override
//        public ItemModel createFromParcel(Parcel source) {
//            return new ItemModel(source);
//        }
//
//        @Override
//        public ItemModel[] newArray(int size) {
//            return new ItemModel[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(mPath);
//        dest.writeString(mThumbnail);
//
//    }


    public String getmPath() {
        return mPath;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public boolean isFunctionItem() {
        return isFunctionItem;
    }

    public Drawable getFunctionItemDrawale() {
        return functionItemDrawale;
    }

    public int getTag() {
        return tag;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public int getIsError() {
        return isError;
    }

    public int getIsAdd() {
        return isAdd;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public String getUploadMd() {
        return uploadMd;
    }

    public String getIds() {
        return ids;
    }

    public HotelDetailBean getHotelDetailBean() {
        return hotelDetailBean;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    public void setFunctionItem(boolean functionItem) {
        isFunctionItem = functionItem;
    }

    public void setFunctionItemDrawale(Drawable functionItemDrawale) {
        this.functionItemDrawale = functionItemDrawale;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    public void setIsError(int isError) {
        this.isError = isError;
    }

    public void setIsAdd(int isAdd) {
        this.isAdd = isAdd;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public void setUploadMd(String uploadMd) {
        this.uploadMd = uploadMd;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public void setHotelDetailBean(HotelDetailBean hotelDetailBean) {
        this.hotelDetailBean = hotelDetailBean;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "mPath='" + mPath + '\'' +
                ", mThumbnail='" + mThumbnail + '\'' +
                ", isSeleted=" + isSeleted +
                ", isFunctionItem=" + isFunctionItem +
                ", functionItemDrawale=" + functionItemDrawale +
                ", tag=" + tag +
                ", isUpload=" + isUpload +
                ", isError=" + isError +
                ", isAdd=" + isAdd +
                ", uploadUrl='" + uploadUrl + '\'' +
                ", uploadMd='" + uploadMd + '\'' +
                ", ids='" + ids + '\'' +
                ", hotelDetailBean=" + hotelDetailBean +
                '}';
    }
}
