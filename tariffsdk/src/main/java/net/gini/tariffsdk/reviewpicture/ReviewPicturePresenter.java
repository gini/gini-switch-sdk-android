package net.gini.tariffsdk.reviewpicture;


import android.net.Uri;

import net.gini.tariffsdk.documentservice.DocumentService;

class ReviewPicturePresenter implements ReviewPictureContract.Presenter {

    private final DocumentService mDocumentService;
    private final Uri mUri;
    private final ReviewPictureContract.View mView;

    ReviewPicturePresenter(final ReviewPictureContract.View view, DocumentService documentService,
            Uri uri) {
        mView = view;
        mDocumentService = documentService;
        mUri = uri;

        mView.setImage(uri);
    }

    @Override
    public void discardImage() {
        mDocumentService.deleteImage(mUri);
        mView.finishReview();
    }

    @Override
    public void keepImage() {
        mDocumentService.keepImage(mUri);
        mView.finishReview();
    }


}
