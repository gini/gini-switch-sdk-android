package net.gini.tariffsdk;

import static junit.framework.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import net.gini.tariffsdk.network.ExtractionOrder;
import net.gini.tariffsdk.network.ExtractionOrderPage;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DocumentServiceImplTest {

    private Context mContext;
    private DocumentServiceImpl mDocumentService;
    private TariffApi mMockTariffApi;
    private Uri mMockUri;

    @Test
    @SmallTest
    public void keepImage_shouldAddImageToImageList() throws IOException {
        mDocumentService.keepImage(mMockUri, 0);
        List<Image> imageList = mDocumentService.getImageList();
        assertEquals("Keep image should add image to image list!", 1, imageList.size());
    }

    @Test
    @SmallTest
    public void keepImage_shouldNotUploadAgainWhenNoRotation() throws IOException {
        mDocumentService.keepImage(mMockUri, 0);
        verify(mMockTariffApi, never()).addPage(anyString(), any(byte[].class),
                Matchers.<NetworkCallback<ExtractionOrderPage>>any());
    }

    @Test
    @SmallTest
    public void keepImage_shouldUploadAgainWhenThereIsRotation() throws IOException {
        mDocumentService.keepImage(mMockUri, 1);
        verify(mMockTariffApi).addPage(anyString(), any(byte[].class),
                Matchers.<NetworkCallback<ExtractionOrderPage>>any());
    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        mMockTariffApi = Mockito.mock(TariffApi.class);
        mMockUri = Mockito.mock(Uri.class);
        when(mMockUri.getPath()).thenReturn("/");
        mDocumentService = new DocumentServiceImpl(mContext, mMockTariffApi);
        mDocumentService.mExtractionOrder = Mockito.mock(ExtractionOrder.class);
    }
}