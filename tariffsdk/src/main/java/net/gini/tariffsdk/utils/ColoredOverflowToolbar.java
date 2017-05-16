package net.gini.tariffsdk.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.gini.tariffsdk.R;

public class ColoredOverflowToolbar extends Toolbar {

    private final int mItemColor;

    public ColoredOverflowToolbar(final Context context) {
        this(context, null);
    }

    public ColoredOverflowToolbar(final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
        final TypedValue typedValue = new TypedValue();
        final TypedArray typedArray = context.obtainStyledAttributes(typedValue.data,
                new int[]{R.attr.colorAccent});
        try {
            mItemColor = typedArray.getColor(0, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r,
            final int b) {
        super.onLayout(changed, l, t, r, b);
        changeOverflowColor(mItemColor);
    }

    private void changeOverflowColor(int color) {
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            changeOverflowColorRecursively(view, color);
        }
    }

    private void changeOverflowColorRecursively(View view, final int color) {

        if (view instanceof ImageView) {
            final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color,
                    PorterDuff.Mode.SRC_IN);
            ((ImageView) view).getDrawable().setAlpha(255);
            ((ImageView) view).getDrawable().setColorFilter(colorFilter);
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                changeOverflowColorRecursively(((ViewGroup) view).getChildAt(i), color);
            }
        }
    }
}
