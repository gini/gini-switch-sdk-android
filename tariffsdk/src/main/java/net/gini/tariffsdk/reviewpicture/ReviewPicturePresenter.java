package net.gini.tariffsdk.reviewpicture;


class ReviewPicturePresenter implements ReviewPictureContract.Presenter {

    private final ReviewPictureContract.View mView;

    ReviewPicturePresenter(final ReviewPictureContract.View view) {
        mView = view;
    }
}
