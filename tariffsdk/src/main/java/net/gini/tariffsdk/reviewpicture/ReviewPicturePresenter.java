package net.gini.tariffsdk.reviewpicture;


import android.net.Uri;

import net.gini.tariffsdk.documentservice.DocumentService;

class ReviewPicturePresenter implements ReviewPictureContract.Presenter {

    private final ReviewPictureContract.View mView;
    private final DocumentService mDocumentService;
    private final Uri mImageUri;

    ReviewPicturePresenter(final ReviewPictureContract.View view, DocumentService documentService, Uri imageUri) {
        mView = view;
        mDocumentService = documentService;
        mImageUri = imageUri;
        mView.setPresenter(this);
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
