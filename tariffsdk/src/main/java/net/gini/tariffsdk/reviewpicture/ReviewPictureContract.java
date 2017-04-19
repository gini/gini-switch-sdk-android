package net.gini.tariffsdk.reviewpicture;


import android.net.Uri;

import net.gini.tariffsdk.base.BaseView;

interface ReviewPictureContract {
    interface Presenter {
        void discardImage();

        void keepImage();

    }

    interface View extends BaseView<ReviewPictureContract.Presenter> {

        void finishReview();

        void setImage(final Uri uri);

        void setRotation(final float degrees);
    }
}
