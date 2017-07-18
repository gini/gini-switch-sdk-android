package net.gini.switchsdk;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TakePicturePresenterTest {

    private static final int AndroidLollipop = 21;

    private static final int AndroidMarshmallow = 23;
    @Mock
    private DocumentService mMockDocumentService;
    @Mock
    private ExtractionService mMockExtractionService;
    @Mock
    private OnboardingManager mMockOnboardingManager;
    @Mock
    private TakePictureContract.View mMockView;
    private TakePicturePresenter mPresenter;

    @Test
    public void permissionDenied_initCameraNotCalled() {
        mPresenter.mBuildVersion = AndroidMarshmallow;

        when(mMockView.hasCameraPermissions()).thenReturn(false);
        mPresenter.start();
        verify(mMockView, never()).initCamera();
    }

    @Test
    public void permissionGranted_initCameraCalled() {
        mPresenter.mBuildVersion = AndroidMarshmallow;

        when(mMockView.hasCameraPermissions()).thenReturn(true);
        mPresenter.start();
        verify(mMockView).initCamera();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new TakePicturePresenter(mMockView, mMockDocumentService,
                mMockExtractionService,
                mMockOnboardingManager);

    }

    @Test
    public void shouldRetrieveExtractions_WhenTheyAreReady() {
        final String orderUrl = "http://bl.ah";
        mPresenter.onOrderCompleted(orderUrl);
        verify(mMockExtractionService).fetchExtractions(anyString(),
                any(ExtractionService.ExtractionListener.class));
    }

    @Test
    public void startPresenter_shouldAddListener() {
        mPresenter.start();
        verify(mMockDocumentService).addDocumentListener(mPresenter);
    }

    @Test
    public void stopPresenter_shouldRemoveListener() {
        mPresenter.stop();
        verify(mMockDocumentService).removeDocumentListener(mPresenter);
    }

    @Test
    public void versionGreaterThan23_requestPermissions() {
        mPresenter.mBuildVersion = AndroidMarshmallow;

        when(mMockView.hasCameraPermissions()).thenReturn(false);
        mPresenter.start();
        verify(mMockView).requestPermissions();
    }

    @Test
    public void versionSmallerThan23_doNotRequestPermissions() {
        mPresenter.mBuildVersion = AndroidLollipop;
        mPresenter.start();
        verify(mMockView, never()).requestPermissions();
    }


}