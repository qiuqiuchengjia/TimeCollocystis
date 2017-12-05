package com.framework.greendroid.banner.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView {
    public TextSliderView(Context context) {
        super(context);
    }


    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView) v.findViewById(R.id.description);
        TextView description2 = (TextView) v.findViewById(R.id.description_2);
        LinearLayout resLayout=(LinearLayout)v.findViewById(R.id.description_layout);
        bindEventAndShow(v, target, description,description2, resLayout);
        return v;
    }


}
