package net.gini.switchsdk.utils;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CenterToolbar extends Toolbar {

    public CenterToolbar(Context context) {
        this(context, null);
    }

    public CenterToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof RecyclerView) {
                centerView(view);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof RecyclerView) {
                int width = getMeasuredWidth();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = width;
                view.setLayoutParams(layoutParams);
            }
        }
    }

    private void centerView(View view) {
        int toolbarWidth = getMeasuredWidth();
        int recyclerWidth = view.getMeasuredWidth();
        int newLeft = (toolbarWidth - recyclerWidth) / 2;
        int newRight = newLeft + recyclerWidth;
        view.layout(newLeft, view.getTop(), newRight, view.getBottom());
    }
}
