package com.framework.greendroid.imagepicker.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.framework.R;
import com.framework.greendroid.imagepicker.models.ItemModel;
import com.framework.greendroid.imagepicker.models.ViewParams;
import com.framework.greendroid.imagepicker.models.ViewParams.ShownStyle;
import com.framework.io.ImageLoaderManager;
import com.framework.log.LogUtil;
import com.framework.os.ApplicationInfo;
import com.framework.util.LocalDisplay;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {
    private static final String LOGTAG = LogUtil.makeLogTag(GalleryAdapter.class);
    //private static final String TAG = GalleryAdapter.class.getName();
    private int gridviewWidth = 0;
    private final ViewParams mParams;
    private final AdpterEventListener mEventListener;

    // private ArrayList<ItemModel> data = new ArrayList<ItemModel>();
    private ArrayList<ItemModel> data = null;
    private LayoutInflater mInfalter;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    int[] dx;
    private Context mContext = null;
    private int srccenWidth = 0;
    private int count = 0;

    public GalleryAdapter(Context mContext, ArrayList<ItemModel> data,
                          ImageLoader imageLoader, ViewParams params,
                          AdpterEventListener eventListener) {
        this.mContext = mContext;
        mInfalter = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mImageLoader = imageLoader;
        this.mParams = params;
        this.mEventListener = eventListener;
        this.data = data;
        this.options = ImageLoaderManager.initDisplayImageOptions(0, 0, 0);
        dx = ApplicationInfo.getDisplayMetrics(mContext);
        LocalDisplay.init(mContext);
        float width = LocalDisplay.designedDP2px(5);//10dp
        //float offset = LocalDisplay.designedDP2px(20);
//        float width = mContext.getResources().getDimension(
//                R.dimen.global_layout_padding);

        srccenWidth = params.getGridWidth() == 0 ? dx[0] : params.getGridWidth();

        gridviewWidth = (int) (srccenWidth - (2 + (mParams.getNumClumns() * 2)) * width) / mParams.getNumClumns();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted()){
                count++;
            }
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ItemModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSeleted = selection;

        }
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean isAllSelected = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isSeleted) {
                isAllSelected = false;
                break;
            }
        }

        return isAllSelected;
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public ArrayList<ItemModel> getSelected() {
        ArrayList<ItemModel> dataT = new ArrayList<ItemModel>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }

    public void addAll(ArrayList<ItemModel> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // notifyDataSetChanged();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInfalter.inflate(R.layout.picker_gallery_item, null);
            holder = new ViewHolder();
            holder.imgQueue = (ImageView) convertView
                    .findViewById(R.id.imgQueue);
            holder.imgCheckBox = (ImageView) convertView
                    .findViewById(R.id.imgQueueMultiSelected);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LayoutParams params = holder.imgQueue.getLayoutParams();
//        int parent_width = parent.getWidth();
//        int item_padding_pix = (int) (mParams.getDensity() * mParams
//                .getItemPaddingDip());
//        params.width = parent_width / mParams.getNumClumns() - 2
//                * item_padding_pix;
//        params.height = params.width;
//        params.height = (int) (((float) params.width) / 4 * 3);
//        DebugLog.i(LOGTAG, "params.width:" + params.height);
//        params.height = gridviewWidth * 3 / 4;
        params.width=gridviewWidth;
        params.height = gridviewWidth;
        holder.imgQueue.setLayoutParams(params);

        if (mParams.getCheckBoxDrawable() != null) {
            Drawable cloneDrawable = mParams.getCheckBoxDrawable()
                    .getConstantState().newDrawable();
            holder.imgCheckBox.setImageDrawable(cloneDrawable);
        }
        if (mParams.getShownStyle() == ShownStyle.Pick_Multiple) {
            holder.imgCheckBox.setOnClickListener(mCheckboxListener);
            holder.imgCheckBox.setTag(position);
            holder.imgCheckBox.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheckBox.setVisibility(View.GONE);
        }

        holder.imgQueue.setTag(position);
        try {
            mImageLoader.cancelDisplayTask(holder.imgQueue);
            if (data.get(position).isFunctionItem) {
                holder.imgCheckBox.setVisibility(View.GONE);
                if (null != data.get(position).functionItemDrawale) {
                    holder.imgQueue
                            .setImageDrawable(data.get(position).functionItemDrawale);
                } else {
                    holder.imgQueue.setImageResource(R.drawable.take_photo);
                }
            } else {
                String path = null;
                if (null != data.get(position).mThumbnail) {
                    path = data.get(position).mThumbnail;
                } else {
                    path = data.get(position).mPath;
                }
                mImageLoader.displayImage(path, holder.imgQueue, options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri,
                                                         View view) {
                                if (null != mParams.getLoadingImageDrawable()) {
                                    holder.imgQueue.setImageDrawable(mParams
                                            .getLoadingImageDrawable());
                                } else {
                                    holder.imgQueue
                                            .setImageResource(R.drawable.no_media);
                                }
                                super.onLoadingStarted(imageUri, view);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri,
                                                        View view, FailReason failReason) {
                                if (null != mParams.getLoadingFailedDrawable()) {
                                    holder.imgQueue.setImageDrawable(mParams
                                            .getLoadingFailedDrawable());
                                } else {
                                    holder.imgQueue
                                            .setImageResource(R.drawable.failed);
                                }
                                super.onLoadingFailed(imageUri, view,
                                        failReason);
                            }
                        });

                if (mParams.getShownStyle() == ShownStyle.Pick_Multiple) {
                    holder.imgCheckBox
                            .setSelected(data.get(position).isSeleted);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ItemModel itemModel = data.get(position);
        if (itemModel.isSeleted()){
            holder.imgCheckBox.setSelected(true);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgCheckBox;
    }

    public void clearCache() {
        mImageLoader.clearDiskCache();
        mImageLoader.clearMemoryCache();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    private OnClickListener mCheckboxListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            if (data.get(position).isSeleted) {
                data.get(position).isSeleted = false;
            } else {
                if (getSelected().size()-count >= mParams.getMaxPickSize()) {
                    if (null != mParams.getToastForReachingMax()) {
                        Toast.makeText(v.getContext(),
                                mParams.getToastForReachingMax(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String des = String.format(mContext.getResources().getString(R.string.reached_max_size),
                                mParams.getMaxPickSize() + "");
                        Toast.makeText(v.getContext(),
                                des, Toast.LENGTH_SHORT)
                                .show();
                    }
                    return;
                }
                data.get(position).isSeleted = true;
            }

            v.setSelected(data.get(position).isSeleted);
            mEventListener.onItemSelectedStatusChange(position,
                    data.get(position).isSeleted);
        }
    };

    public void updateStatus(int currentPosition, boolean isSelected) {
        data.get(currentPosition).isSeleted = isSelected;
        notifyDataSetChanged();
    }

    interface AdpterEventListener {
        void onItemSelectedStatusChange(int position, boolean isSelected);
    }

}
