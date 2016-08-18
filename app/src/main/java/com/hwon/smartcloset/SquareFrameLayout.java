package com.hwon.smartcloset;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.jar.Attributes;

/**
 * Created by hwkim_000 on 2016-07-27.
 */
public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }



    protected void onMeasure(int widthSpec, int heightSpec){
        super.onMeasure(widthSpec, widthSpec);
    }
}
