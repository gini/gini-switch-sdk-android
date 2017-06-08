package net.gini.tariffsdk;

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

        verify(mMockView).finishReview();
    }

    @Test
    public void keepImage_ServiceShouldSaveIt() {
        ReviewPicturePresenter presenter = new ReviewPicturePresenter(mMockView,
                mMockDocumentService, mMockUri);
        presenter.keepImage();

        verify(mMockDocumentService).keepImage(mMockUri, mRotationCount);
    }

    @Test
    public void keepImage_finishReview() {
        ReviewPicturePresenter presenter = new ReviewPicturePresenter(mMockView,
                mMockDocumentService, mMockUri);
        presenter.keepImage();

        verify(mMockView).finishReview();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}