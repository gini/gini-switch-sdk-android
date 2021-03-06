package net.gini.switchsdk;

import static org.mockito.Mockito.verify;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReviewPicturePresenterTest {

    @Mock
    DocumentService mMockDocumentService;
    @Mock
    Uri mMockUri;
    @Mock
    ReviewPictureContract.View mMockView;

    @Test
    public void creatingPresenter_shouldSetTheImageInView() {
        new ReviewPicturePresenter(mMockView, mMockDocumentService, mMockUri);
        verify(mMockView).setImage(mMockUri);
    }

    @Test
    public void discardImage_ServiceShouldDeleteIt() {
        ReviewPicturePresenter presenter = new ReviewPicturePresenter(mMockView,
                mMockDocumentService, mMockUri);
        presenter.discardImage();

        verify(mMockDocumentService).deleteImage(mMockUri);
    }

    @Test
    public void discardImage_finishReview() {
        ReviewPicturePresenter presenter = new ReviewPicturePresenter(mMockView,
                mMockDocumentService, mMockUri);
        presenter.discardImage();

        verify(mMockView).discardImageAndFinishReview();
    }

    @Test
    public void keepImage_ServiceShouldSaveIt() {
        ReviewPicturePresenter presenter = new ReviewPicturePresenter(mMockView,
                mMockDocumentService, mMockUri);
        presenter.keepImage();

        verify(mMockDocumentService).keepImage(mMockUri);
    }

    @Test
    public void keepImage_finishReview() {
        ReviewPicturePresenter presenter = new ReviewPicturePresenter(mMockView,
                mMockDocumentService, mMockUri);
        presenter.keepImage();

        verify(mMockView).keepImageAndFinishReview();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}