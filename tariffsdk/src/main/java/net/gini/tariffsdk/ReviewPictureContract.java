package net.gini.tariffsdk;


import android.net.Uri;

interface ReviewPictureContract {
    interface Presenter {
        void discardImage();

        void keepImage();
    }

    interface View {

        void finishReview();

        void setImage(final Uri uri);
    }
}
