package net.gini.tariffsdk.reviewpicture;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import net.gini.tariffsdk.R;

public class ReviewPictureView extends RelativeLayout implements ReviewPictureContract.View {

    public ReviewPictureView(final Context context) {
        this(context, null);
    }

    public ReviewPictureView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_review_picture, this);
    }
}
