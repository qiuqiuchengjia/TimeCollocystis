package com.framework.greendroid.banner.SliderTypes;

import java.io.File;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods. I provide two example:
 * {@link com.daimajia.slider.library.SliderTypes.DefaultSliderView} and
 * {@link com.daimajia.slider.library.SliderTypes.TextSliderView} if you want to
 * show progressbar, you just need to set a progressbar id as @+id/loading_bar.
 */
public abstract class BaseSliderView {

    protected Context mContext;

    private Bundle mBundle;

    /**
     * Error place holder image.
     */
    private int mErrorPlaceHolderRes;

    /**
     * Empty imageView placeholder.
     */
    private int mEmptyPlaceHolderRes;

    private String mUrl;
    private File mFile;
    private int mRes;

    protected OnSliderClickListener mOnSliderClickListener;

    private boolean mErrorDisappear;

    private ImageLoadListener mLoadListener;

    private String mDescription;
    private String mDescription2;

    private Picasso mPicasso;
    private DisplayImageOptions options;

    /**
     * Scale type of the image.
     */
    private ScaleType mScaleType = ScaleType.Fit;

    public enum ScaleType {
        CenterCrop, CenterInside, Fit, FitCenterCrop
    }

    protected BaseSliderView(Context context) {
        mContext = context;
    }

    /**
     * the placeholder image when loading image from url or file.
     *
     * @param resId Image resource id
     * @return
     */
    public BaseSliderView empty(int resId) {
        mEmptyPlaceHolderRes = resId;
        return this;
    }

    /**
     * determine whether remove the image which failed to download or load from
     * file
     *
     * @param disappear
     * @return
     */
    public BaseSliderView errorDisappear(boolean disappear) {
        mErrorDisappear = disappear;
        return this;
    }

    /**
     * if you set errorDisappear false, this will set a error placeholder image.
     *
     * @param resId image resource id
     * @return
     */
    public BaseSliderView error(int resId) {
        mErrorPlaceHolderRes = resId;
        return this;
    }

    /**
     * the description of a slider image.
     *
     * @param description
     * @return
     */
    public BaseSliderView description(String description, String description2) {
        mDescription = description;
        mDescription2 = description2;
        return this;
    }

    /**
     * set a url as a image that preparing to load
     *
     * @param url
     * @return
     */
    public BaseSliderView image(String url) {
        if (mFile != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function,"
                    + "you only have permission to call it once");
        }
        mUrl = url;
        return this;
    }

    /**
     * set a file as a image that will to load
     *
     * @param file
     * @return
     */
    public BaseSliderView image(File file) {
        if (mUrl != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function,"
                    + "you only have permission to call it once");
        }
        mFile = file;
        return this;
    }

    public BaseSliderView image(int res) {
        if (mUrl != null || mFile != null) {
            throw new IllegalStateException("Call multi image function,"
                    + "you only have permission to call it once");
        }
        mRes = res;
        return this;
    }

    /**
     * lets users add a bundle of additional information
     *
     * @param bundle
     * @return
     */
    public BaseSliderView bundle(Bundle bundle) {
        mBundle = bundle;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isErrorDisappear() {
        return mErrorDisappear;
    }

    public int getEmpty() {
        return mEmptyPlaceHolderRes;
    }

    public int getError() {
        return mErrorPlaceHolderRes;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDescription2() {
        return mDescription2;
    }


    public Context getContext() {
        return mContext;
    }

    private boolean isShow = false;

    public boolean isDescrption(boolean isShow) {
        return this.isShow;
    }

    /**
     * set a slider image click listener
     *
     * @param l
     * @return
     */
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        mOnSliderClickListener = l;
        return this;
    }

    public void setOptions(DisplayImageOptions options) {
        this.options = options;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }

    /**
     * When you want to implement your own slider view, please call this method
     * in the end in `getView()` method
     *
     * @param v               the whole view
     * @param targetImageView where to place image
     */
    protected void bindEventAndShow(final View v, ImageView targetImageView,
                                    TextView txtDescrption, TextView txtDescrption2, LinearLayout resLayout) {
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        if (targetImageView == null)
            return;

        if (mLoadListener != null) {
            mLoadListener.onStart(me);
        }
        if (getDescription() != null || getDescription2() != null) {
            resLayout.setVisibility(View.VISIBLE);
        } else {
            resLayout.setVisibility(View.GONE);
        }

        if (txtDescrption != null) {
            if (getDescription() != null && !getDescription().equals("")) {
                txtDescrption.setVisibility(View.VISIBLE);
            } else {
                txtDescrption.setVisibility(View.GONE);
            }

            txtDescrption.setText(getDescription());
        }
        if (txtDescrption2 != null) {
            if (getDescription2() != null && !getDescription2().equals("")) {
                txtDescrption2.setVisibility(View.VISIBLE);

            } else {
                txtDescrption2.setVisibility(View.GONE);
            }
            txtDescrption2.setText(getDescription2());
        }

        ImageLoader.getInstance().displayImage(mUrl, targetImageView, getOptions());
        // Picasso p = (mPicasso != null) ? mPicasso : Picasso.with(mContext);
        // RequestCreator rq = null;
        // if(mUrl!=null){
        // rq = p.load(mUrl);
        // }else if(mFile != null){
        // rq = p.load(mFile);
        // }else if(mRes != 0){
        // rq = p.load(mRes);
        // }else{
        // return;
        // }
        //
        // if(rq == null){
        // return;
        // }
        //
        // if(getEmpty() != 0){
        // rq.placeholder(getEmpty());
        // }
        //
        // if(getError() != 0){
        // rq.error(getError());
        // }
        //
        switch (mScaleType) {
            case Fit:
                // rq.fit();
                targetImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case CenterCrop:
                // rq.fit().centerCrop();
                targetImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case CenterInside:
                // rq.fit().centerInside();
                targetImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
        }
        //
        // rq.into(targetImageView,new Callback() {
        // @Override
        // public void onSuccess() {
        // if(v.findViewById(R.id.loading_bar) != null){
        // v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
        // }
        // }
        //
        // @Override
        // public void onError() {
        // if(mLoadListener != null){
        // mLoadListener.onEnd(false,me);
        // }
        // if(v.findViewById(R.id.loading_bar) != null){
        // v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
        // }
        // }
        // });
    }

    public BaseSliderView setScaleType(ScaleType type) {
        mScaleType = type;
        return this;
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * the extended class have to implement getView(), which is called by the
     * adapter, every extended class response to render their own view.
     *
     * @return
     */
    public abstract View getView();

    /**
     * set a listener to get a message , if load error.
     *
     * @param l
     */
    public void setOnImageLoadListener(ImageLoadListener l) {
        mLoadListener = l;
    }

    public interface OnSliderClickListener {
        public void onSliderClick(BaseSliderView slider);
    }

    /**
     * when you have some extra information, please put it in this bundle.
     *
     * @return
     */
    public Bundle getBundle() {
        return mBundle;
    }

    public interface ImageLoadListener {
        public void onStart(BaseSliderView target);

        public void onEnd(boolean result, BaseSliderView target);
    }

    /**
     * Get the last instance set via setPicasso(), or null if no user provided
     * instance was set
     *
     * @return The current user-provided Picasso instance, or null if none
     */
    public Picasso getPicasso() {
        return mPicasso;
    }

    /**
     * Provide a Picasso instance to use when loading pictures, this is useful
     * if you have a particular HTTP cache you would like to share.
     *
     * @param picasso The Picasso instance to use, may be null to let the system use
     *                the default instance
     */
    public void setPicasso(Picasso picasso) {
        mPicasso = picasso;
    }
}
