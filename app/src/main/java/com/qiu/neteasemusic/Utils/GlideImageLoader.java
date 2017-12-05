package com.qiu.neteasemusic.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by qiu on 2017/4/13.
 * FindGroomFragment的图片加载器
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Picasso.with(context).load((Integer) path).into(imageView);
    }
}
