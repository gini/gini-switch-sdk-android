package net.gini.tariffsdk;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.ExtractionOrder;
import net.gini.tariffsdk.network.ExtractionOrderPage;
import net.gini.tariffsdk.network.NetworkCallback;
import net.jodah.concurrentunit.Waiter;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class TariffApiImplTest {

    @Mock
    NetworkCallback<Configuration> mMockConfigurationNetworkCallback;
    @Mock
    private AccessToken mMockAccessToken;
    @Mock
    private AuthenticationService mMockAuthenticationService;
    @Mock
    private ClientParameter mMockClientParameter;
    @Mock
    private NetworkCallback<ExtractionOrder> mMockExtractionOrderNetworkCallback;
    @Mock
    private File mMockFile;
    @Mock
    private NetworkCallback<ExtractionOrderPage> mMockStringNetworkCallback;
    private HttpUrl mMockUrl;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private MockWebServer mServer;
    private Waiter mWaiter;

    @Test
    public void addPage_shouldBeAPostRequest()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.addPage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void addPage_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.addPage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    public void addPage_shouldContainImageAsBody()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        final byte[] page = new byte[Short.MAX_VALUE];
        new Random().nextBytes(page);
        tariffApi.addPage(mMockUrl.toString(), page, mMockStringNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        final byte[] body = request.getBody().readByteArray();
        assertArrayEquals(page, body);
    }

    @Test
    public void addPage_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {

        final TariffApiImpl tariffApi = createTariffApi();
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);

        tariffApi.addPage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void addPage_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.addPage(mMockUrl.toString(), new byte[1],
                new NetworkCallback<ExtractionOrderPage>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.assertTrue(e instanceof NetworkErrorException);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderPage p) {
                        mWaiter.fail();
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void addPage_wasSuccessfulShouldCallOnSuccess()
            throws InterruptedException, JSONException, TimeoutException {

        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockCreatePagesResponse("",
                        ExtractionOrderPage.Status.processing));
        mServer.enqueue(mJSONMockResponse);

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.addPage(mMockUrl.toString(), new byte[1],
                new NetworkCallback<ExtractionOrderPage>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderPage p) {
                        mWaiter.assertNotNull(p);
                        mWaiter.resume();
                    }
                });

        mWaiter.await();
    }

    @Test
    public void addPage_wasSuccessfulShouldContainAPagesLink()
            throws InterruptedException, JSONException, TimeoutException {

        final String pagesLink = "http://self_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockCreatePagesResponse(pagesLink, ExtractionOrderPage.Status.processing));
        mServer.enqueue(mJSONMockResponse);

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.addPage(mMockUrl.toString(), new byte[1],
                new NetworkCallback<ExtractionOrderPage>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderPage e) {
                        mWaiter.assertEquals(pagesLink, e.getSelf());
                        mWaiter.resume();
                    }
                });

        mWaiter.await();
    }

    @Test
    public void addPage_wasSuccessfulShouldContainAStatus()
            throws InterruptedException, JSONException, TimeoutException {

        final ExtractionOrderPage.Status status = ExtractionOrderPage.Status.processing;
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockCreatePagesResponse("", status));
        mServer.enqueue(mJSONMockResponse);

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.addPage(mMockUrl.toString(), new byte[1],
                new NetworkCallback<ExtractionOrderPage>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderPage e) {
                        mWaiter.assertEquals(status, e.getStatus());
                        mWaiter.resume();
                    }
                });

        mWaiter.await();
    }

    @Test
    public void createExtractionOrder_CouldNotParseJsonShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        MockResponse mJSONMockResponse = new MockResponse().setBody("");
        mServer.enqueue(mJSONMockResponse);

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.assertTrue(e instanceof JSONException);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final ExtractionOrder extractionOrder) {
                mWaiter.fail();
                mWaiter.resume();
            }
        });

        mWaiter.await();
    }

    @Test
    public void createExtractionOrder_shouldBeAPostRequest()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void createExtractionOrder_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    public void createExtractionOrder_shouldContainEmptyJson()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertEquals("{ }", body);
    }

    @Test
    public void createExtractionOrder_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {

        final TariffApiImpl tariffApi = createTariffApi();
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);

        tariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void createExtractionOrder_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.assertTrue(e instanceof NetworkErrorException);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final ExtractionOrder extractionOrder) {
                mWaiter.fail();
                mWaiter.resume();
            }
        });
        mWaiter.await();
    }

    @Test
    public void createExtractionOrder_wasSuccessfulShouldCallOnSuccess()
            throws InterruptedException, JSONException, TimeoutException {

        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderResponse("", ""));
        mServer.enqueue(mJSONMockResponse);

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.fail(e);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final ExtractionOrder extractionOrder) {
                mWaiter.assertNotNull(extractionOrder);
                mWaiter.resume();
            }
        });

        mWaiter.await();
    }

    @Test
    public void createExtractionOrder_wasSuccessfulShouldContainAPagesLink()
            throws InterruptedException, JSONException, TimeoutException {

        final String pagesLink = "http://self_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderResponse("", pagesLink));
        mServer.enqueue(mJSONMockResponse);

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.fail(e);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final ExtractionOrder extractionOrder) {
                mWaiter.assertEquals(pagesLink, extractionOrder.getPages());
                mWaiter.resume();
            }
        });

        mWaiter.await();
    }

    @Test
    public void createExtractionOrder_wasSuccessfulShouldContainASelfLink()
            throws InterruptedException, JSONException, TimeoutException {

        final String selfLink = "http://self_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderResponse(selfLink, ""));
        mServer.enqueue(mJSONMockResponse);

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.fail(e);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final ExtractionOrder extractionOrder) {
                mWaiter.assertEquals(selfLink, extractionOrder.getSelf());
                mWaiter.resume();
            }
        });

        mWaiter.await();
    }

    @Test
    public void requestConfiguration_shouldBeAGetRequest()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.requestConfiguration(mMockClientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void requestConfiguration_shouldContainAnAuthorizationHeader()
            throws InterruptedException {
        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.requestConfiguration(mMockClientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());

    }

    @Test
    public void requestConfiguration_shouldContainClientDeviceModel()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        final String deviceModel = "Pixel";
        final ClientParameter clientParameter = new ClientParameter(0, null, deviceModel);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains(deviceModel));
    }

    @Test
    public void requestConfiguration_shouldContainClientOsVersion()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        final ClientParameter clientParameter = new ClientParameter(19, null, null);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains("19"));
    }

    @Test
    public void requestConfiguration_shouldContainClientPlatform()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        final ClientParameter clientParameter = new ClientParameter(0, null, null);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains("android"));
    }

    @Test
    public void requestConfiguration_shouldContainClientSdkVersion()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = createTariffApi();

        final String sdkVersion = "1.0.1";
        final ClientParameter clientParameter = new ClientParameter(0, sdkVersion, null);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains(sdkVersion));
    }

    @Test
    public void requestConfiguration_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {

        final TariffApiImpl tariffApi = createTariffApi();
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);

        tariffApi.requestConfiguration(mMockClientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void requestConfiguration_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        final TariffApiImpl tariffApi = createTariffApi();

        tariffApi.requestConfiguration(mMockClientParameter, new NetworkCallback<Configuration>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.assertTrue(e instanceof NetworkErrorException);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final Configuration configuration) {
                mWaiter.fail();
                mWaiter.resume();
            }
        });
        mWaiter.await();
    }

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer();
        mServer.start();
        mMockUrl = mServer.url("/");
        mWaiter = new Waiter();

        MockitoAnnotations.initMocks(this);

        when(mMockAccessToken.getToken()).thenReturn("");
        when(mMockAuthenticationService.getUserToken()).thenReturn(mMockAccessToken);

        when(mMockClientParameter.getSdkVersion()).thenReturn("1.2.3");
        when(mMockClientParameter.getDeviceModel()).thenReturn("Nexus 5");
        when(mMockClientParameter.getOsVersion()).thenReturn("25");

        when(mMockFile.getName()).thenReturn("file");
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

    private String createMockCreatePagesResponse(final String self,
            final ExtractionOrderPage.Status status) {
        return "{\n"
                + "  \"status\" : \"" + status.toString() + "\",\n"
                + "  \"_links\" : {\n"
                + "    \"self\" : {\n"
                + "      \"href\" : \"" + self + "\"\n"
                + "    }\n"
                + "  }\n"
                + "}";
    }

    private String createMockExtractionOrderResponse(final String self, final String pages) {
        return "{\n"
                + "  \"_links\" : {\n"
                + "    \"self\" : {\n"
                + "      \"href\" : \"" + self + "\"\n"
                + "    },\n"
                + "    \"pages\" : {\n"
                + "      \"href\" : \"" + pages + "\"\n"
                + "    }\n"
                + "  }\n"
                + "}";
    }

    @NonNull
    private TariffApiImpl createTariffApi() {
        return new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);
    }

}