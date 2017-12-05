package com.framework.greendroid.shapeimageview;

import android.content.Context;
import android.util.AttributeSet;

import com.framework.greendroid.shapeimageview.shader.ShaderHelper;
import com.framework.greendroid.shapeimageview.shader.SvgShader;

public class ShapeImageView extends ShaderImageView {

    public ShapeImageView(Context context) {
        super(context);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader();
    }
}
