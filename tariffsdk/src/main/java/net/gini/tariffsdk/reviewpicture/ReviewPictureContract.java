package net.gini.tariffsdk.reviewpicture;


import android.net.Uri;

interface ReviewPictureContract {
    interface Presenter {
        void discardImage();

        void keepImage();

    }

    interface View {

        void finishReview();

        void setImage(final Uri uri);

        void setRotation(final float degrees);
    }
}
