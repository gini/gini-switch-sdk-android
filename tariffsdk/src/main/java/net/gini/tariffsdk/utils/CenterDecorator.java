package net.gini.tariffsdk.utils;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CenterDecorator extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent,
            final RecyclerView.State state) {

        final int position = parent.getChildAdapterPosition(view);
        final int itemCount = parent.getAdapter().getItemCount();

        final int totalWidth = Math.max(parent.getMeasuredWidth(), parent.getWidth());
        final int cardWidth = Math.max(view.getLayoutParams().width, view.getWidth());
        view.getMeasuredWidth();
        int left = 0;
        int right = 0;

        //first item -> First and last can be the same
        if (position == 0) {
            left = (totalWidth - cardWidth) / 2;
        }
        //last item -> First and last can be the same
        if (position == itemCount - 1) {
            right = (totalWidth - cardWidth) / 2;
        }
        outRect.set(left, 0, right, 0);
    }
}

