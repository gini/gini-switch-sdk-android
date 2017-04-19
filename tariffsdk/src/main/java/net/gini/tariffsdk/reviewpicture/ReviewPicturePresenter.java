package net.gini.tariffsdk.reviewpicture;


import android.net.Uri;
import android.support.media.ExifInterface;

import net.gini.tariffsdk.documentservice.DocumentService;

import java.io.IOException;

class ReviewPicturePresenter implements ReviewPictureContract.Presenter {

    private final ReviewPictureContract.View mView;
    private final DocumentService mDocumentService;
    private final Uri mImageUri;

    ReviewPicturePresenter(final ReviewPictureContract.View view, DocumentService documentService, Uri imageUri) {
        mView = view;
        mDocumentService = documentService;
        mImageUri = imageUri;
        mView.setPresenter(this);
        mView.setImage(imageUri);
        final float rotation = getRequiredRotationDegrees(mImageUri);
        mView.setRotation(rotation);
    }

    private float getRequiredRotationDegrees(final Uri imageUri) {

        final ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri.getPath());
            final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            switch (orientation) {
                case "3":
                    return 180;
                case "0":
                case "6":
                    return 90;
                case "8":
                    return 270;
            }

        } catch (IOException ignored) {
        }

        return 0;
    }

    @Override
    public void discardImage() {
        mDocumentService.deleteImage(mImageUri);
        mView.finishReview();
    }

    @Override
    public void keepImage() {
        mDocumentService.keepImage(mImageUri);
        mView.finishReview();
    }

}
