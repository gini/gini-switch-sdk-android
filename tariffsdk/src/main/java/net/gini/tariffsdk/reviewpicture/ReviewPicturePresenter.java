package net.gini.tariffsdk.reviewpicture;


import android.net.Uri;

import net.gini.tariffsdk.documentservice.DocumentService;

class ReviewPicturePresenter implements ReviewPictureContract.Presenter {

    private final DocumentService mDocumentService;
    private final Uri mImageUri;
    private final ReviewPictureContract.View mView;

    ReviewPicturePresenter(final ReviewPictureContract.View view, DocumentService documentService,
            Uri imageUri) {
        mView = view;
        mDocumentService = documentService;
        mImageUri = imageUri;
        mView.setImage(imageUri);
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
