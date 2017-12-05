package com.framework.greendroid.banner.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.framework.R;

/**
 * a simple slider view, which just show an image. If you want to make your own slider view,
 * <p/>
 * just extend BaseSliderView, and implement getView() method.
 */
public class DefaultSliderView extends BaseSliderView {

    public DefaultSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_default, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, target, null, null, null);
        return v;
    }
}
