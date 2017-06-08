package net.gini.tariffsdk;


import android.net.Uri;

class ReviewPicturePresenter implements ReviewPictureContract.Presenter {

    private final DocumentService mDocumentService;
    private final Uri mUri;
    private final ReviewPictureContract.View mView;
    private int mRotationCount = 0;

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
        mDocumentService.keepImage(mUri, mRotationCount);
        mView.finishReview();
    }

    @Override
    public void rotateImage() {
        mRotationCount++;
        mView.rotateView();
    }


}
