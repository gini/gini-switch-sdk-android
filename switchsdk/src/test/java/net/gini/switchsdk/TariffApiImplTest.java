package net.gini.switchsdk;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import net.gini.switchsdk.authentication.AuthenticationService;
import net.gini.switchsdk.authentication.models.AccessToken;
import net.gini.switchsdk.configuration.models.ClientInformation;
import net.gini.switchsdk.configuration.models.Configuration;
import net.gini.switchsdk.network.ExtractionOrder;
import net.gini.switchsdk.network.ExtractionOrderPage;
import net.gini.switchsdk.network.ExtractionOrderState;
import net.gini.switchsdk.network.Extractions;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.utils.SwitchException;
import net.jodah.concurrentunit.Waiter;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private ClientInformation mMockClientInformation;
    @Mock
    private NetworkCallback<ExtractionOrder> mMockExtractionOrderNetworkCallback;
    @Mock
    private NetworkCallback<Extractions> mMockExtractionsNetworkCallback;
    @Mock
    private File mMockFile;
    @Mock
    private NetworkCallback<ExtractionOrderState> mMockOrderStateNetworkCallback;
    @Mock
    private NetworkCallback<ExtractionOrderPage> mMockStringNetworkCallback;
    private HttpUrl mMockUrl;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private MockWebServer mServer;
    private TariffApiImpl mTariffApi;
    private Waiter mWaiter;

    @Test
    public void addPage_shouldBeAPostRequest()
            throws InterruptedException, TimeoutException {
        mTariffApi.addPage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void addPage_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {
        mTariffApi.addPage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    public void addPage_shouldContainImageAsBody()
            throws InterruptedException, TimeoutException {
        final byte[] page = new byte[Short.MAX_VALUE];
        new Random().nextBytes(page);
        mTariffApi.addPage(mMockUrl.toString(), page, mMockStringNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        final byte[] body = request.getBody().readByteArray();
        assertArrayEquals(page, body);
    }

    @Test
    public void addPage_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);
        mTariffApi.addPage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void addPage_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        mTariffApi.addPage(mMockUrl.toString(), new byte[1],
                new NetworkCallback<ExtractionOrderPage>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.assertTrue(e instanceof SwitchException);
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
        mTariffApi.addPage(mMockUrl.toString(), new byte[1],
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
        mTariffApi.addPage(mMockUrl.toString(), new byte[1],
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
        mTariffApi.addPage(mMockUrl.toString(), new byte[1],
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
        mTariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.assertTrue(e instanceof SwitchException);
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
        mTariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void createExtractionOrder_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {
        mTariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    public void createExtractionOrder_shouldContainEmptyJson()
            throws InterruptedException, TimeoutException {
        mTariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertEquals("{ }", body);
    }

    @Test
    public void createExtractionOrder_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);
        mTariffApi.createExtractionOrder(mMockExtractionOrderNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void createExtractionOrder_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {
        mServer.enqueue(new MockResponse().setResponseCode(500));
        mTariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.assertTrue(e instanceof SwitchException);
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
        mTariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
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
        mTariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
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
        mTariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
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
    public void deletePage_shouldBeADeleteRequest()
            throws InterruptedException, TimeoutException {
        mTariffApi.deletePage(mMockUrl.toString());
        RecordedRequest request = mServer.takeRequest();
        assertEquals("Delete Page should be a DELETE request!", "DELETE", request.getMethod());
    }

    @Test
    public void deletePage_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {
        mTariffApi.deletePage(mMockUrl.toString());
        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse("Delete Page should contain an Authorization Header!",
                authorizationHeader.isEmpty());
    }

    @Test
    public void deletePage_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);
        mTariffApi.deletePage(mMockUrl.toString());
        RecordedRequest request = mServer.takeRequest();
        assertEquals("Delete Page should contain a Bearer token as Authorization Header!",
                "BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void getOrderState_shouldBeAGetRequest()
            throws InterruptedException, TimeoutException {
        mTariffApi.getOrderState(mMockUrl.toString(), mMockOrderStateNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void getOrderState_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {
        mTariffApi.getOrderState(mMockUrl.toString(), mMockOrderStateNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    public void getOrderState_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);
        mTariffApi.getOrderState(mMockUrl.toString(), mMockOrderStateNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void getOrderState_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {
        mServer.enqueue(new MockResponse().setResponseCode(500));
        mTariffApi.getOrderState(mMockUrl.toString(),
                new NetworkCallback<ExtractionOrderState>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.assertTrue(e instanceof SwitchException);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderState extractionOrderState) {
                        mWaiter.fail();
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void getOrderState_wasSuccessfulShouldCallOnSuccess()
            throws InterruptedException, JSONException, TimeoutException {
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderState(new ArrayList<String>(), null, null));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.getOrderState(mMockUrl.toString(),
                new NetworkCallback<ExtractionOrderState>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderState extractionOrderState) {
                        mWaiter.assertNotNull(extractionOrderState);
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void getOrderState_wasSuccessfulShouldContainAExtractionsReadyBoolean()
            throws InterruptedException, JSONException, TimeoutException {
        final String pagesLink = "http://pages_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderState(new ArrayList<String>(), null, pagesLink, true));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.getOrderState(mMockUrl.toString(),
                new NetworkCallback<ExtractionOrderState>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderState extractionOrderState) {
                        mWaiter.assertTrue(extractionOrderState.isOrderComplete());
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void getOrderState_wasSuccessfulShouldContainAPagesLink()
            throws InterruptedException, JSONException, TimeoutException {
        final String pagesLink = "http://pages_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderState(new ArrayList<String>(), null, pagesLink));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.getOrderState(mMockUrl.toString(),
                new NetworkCallback<ExtractionOrderState>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderState extractionOrderState) {
                        mWaiter.assertEquals(pagesLink, extractionOrderState.getOrder().getPages());
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void getOrderState_wasSuccessfulShouldContainASelfLink()
            throws InterruptedException, JSONException, TimeoutException {
        final String selfLink = "http://self_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderState(new ArrayList<String>(), selfLink, null));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.getOrderState(mMockUrl.toString(),
                new NetworkCallback<ExtractionOrderState>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderState extractionOrderState) {
                        mWaiter.assertEquals(selfLink, extractionOrderState.getOrder().getSelf());
                        mWaiter.resume();
                    }
                });

        mWaiter.await();
    }

    @Test
    public void getOrderState_wasSuccessfulShouldContainPages()
            throws InterruptedException, JSONException, TimeoutException {
        ArrayList<String> pages = new ArrayList<>();
        final String page1 = createMockCreatePagesResponse("Page1",
                ExtractionOrderPage.Status.processing);
        final String page2 = createMockCreatePagesResponse("Page2",
                ExtractionOrderPage.Status.processing);
        pages.add(page1);
        pages.add(page2);
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionOrderState(pages, null, null));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.getOrderState(mMockUrl.toString(),
                new NetworkCallback<ExtractionOrderState>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderState extractionOrderState) {
                        mWaiter.assertEquals(2, extractionOrderState.getOrderPages().size());
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void replacePage_shouldBeAPutRequest()
            throws InterruptedException, TimeoutException {
        mTariffApi.replacePage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("PUT", request.getMethod());
    }

    @Test
    public void replacePage_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {
        mTariffApi.replacePage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    public void replacePage_shouldContainImageAsBody()
            throws InterruptedException, TimeoutException {
        final byte[] page = new byte[Short.MAX_VALUE];
        new Random().nextBytes(page);
        mTariffApi.replacePage(mMockUrl.toString(), page, mMockStringNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        final byte[] body = request.getBody().readByteArray();
        assertArrayEquals(page, body);
    }

    @Test
    public void replacePage_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);
        mTariffApi.replacePage(mMockUrl.toString(), new byte[1], mMockStringNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void replacePage_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {
        mServer.enqueue(new MockResponse().setResponseCode(500));
        mTariffApi.replacePage(mMockUrl.toString(), new byte[1],
                new NetworkCallback<ExtractionOrderPage>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.assertTrue(e instanceof SwitchException);
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
    public void replacePage_wasSuccessfulShouldCallOnSuccess()
            throws InterruptedException, JSONException, TimeoutException {
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockCreatePagesResponse("",
                        ExtractionOrderPage.Status.processing));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.replacePage(mMockUrl.toString(), new byte[1],
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
    public void replacePage_wasSuccessfulShouldContainAPagesLink()
            throws InterruptedException, JSONException, TimeoutException {
        final String pagesLink = "http://self_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockCreatePagesResponse(pagesLink, ExtractionOrderPage.Status.processing));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.replacePage(mMockUrl.toString(), new byte[1],
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
    public void replacePage_wasSuccessfulShouldContainAStatus()
            throws InterruptedException, JSONException, TimeoutException {
        final ExtractionOrderPage.Status status = ExtractionOrderPage.Status.processing;
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockCreatePagesResponse("", status));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.replacePage(mMockUrl.toString(), new byte[1],
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
    public void requestConfiguration_shouldBeAGetRequest()
            throws InterruptedException, TimeoutException {
        mTariffApi.requestConfiguration(mMockClientInformation, mMockConfigurationNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void requestConfiguration_shouldContainAnAuthorizationHeader()
            throws InterruptedException {
        mTariffApi.requestConfiguration(mMockClientInformation, mMockConfigurationNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());

    }

    @Test
    public void requestConfiguration_shouldContainClientDeviceModel()
            throws InterruptedException, TimeoutException {
        final String deviceModel = "Pixel";
        final ClientInformation clientInformation = new ClientInformation(0, null, deviceModel);
        mTariffApi.requestConfiguration(clientInformation, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientInformation.PLATFORM_NAME));
        assertTrue(request.getPath().contains(deviceModel));
    }

    @Test
    public void requestConfiguration_shouldContainClientOsVersion()
            throws InterruptedException, TimeoutException {
        final int osVersion = 19;
        final ClientInformation clientInformation = new ClientInformation(osVersion, null, null);
        mTariffApi.requestConfiguration(clientInformation, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientInformation.PLATFORM_NAME));
        assertTrue(request.getPath().contains(Integer.toString(osVersion)));
    }

    @Test
    public void requestConfiguration_shouldContainClientPlatform()
            throws InterruptedException, TimeoutException {
        final ClientInformation clientInformation = new ClientInformation(0, null, null);
        mTariffApi.requestConfiguration(clientInformation, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientInformation.PLATFORM_NAME));
        assertTrue(request.getPath().contains("android"));
    }

    @Test
    public void requestConfiguration_shouldContainClientSdkVersion()
            throws InterruptedException, TimeoutException {
        final String sdkVersion = "1.0.1";
        final ClientInformation clientInformation = new ClientInformation(0, sdkVersion, null);
        mTariffApi.requestConfiguration(clientInformation, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientInformation.PLATFORM_NAME));
        assertTrue(request.getPath().contains(sdkVersion));
    }

    @Test
    public void requestConfiguration_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);
        mTariffApi.requestConfiguration(mMockClientInformation, mMockConfigurationNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void requestConfiguration_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {
        mServer.enqueue(new MockResponse().setResponseCode(500));
        mTariffApi.requestConfiguration(mMockClientInformation,
                new NetworkCallback<Configuration>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.assertTrue(e instanceof SwitchException);
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

    @Test
    public void retrieveExtractions_shouldBeAGetRequest()
            throws InterruptedException, TimeoutException {
        mTariffApi.retrieveExtractions(mMockUrl.toString(), mMockExtractionsNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void retrieveExtractions_shouldContainAuthorizationHeader()
            throws InterruptedException, TimeoutException {
        mTariffApi.retrieveExtractions(mMockUrl.toString(), mMockExtractionsNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());
    }

    @Test
    public void retrieveExtractions_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);
        mTariffApi.retrieveExtractions(mMockUrl.toString(), mMockExtractionsNetworkCallback);
        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void retrieveExtractions_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        mTariffApi.retrieveExtractions(mMockUrl.toString(),
                new NetworkCallback<Extractions>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.assertTrue(e instanceof SwitchException);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Extractions e) {
                        mWaiter.fail();
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void retrieveExtractions_wasSuccessfulShouldCallOnSuccess()
            throws InterruptedException, JSONException, TimeoutException {
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionsResponse(""));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.retrieveExtractions(mMockUrl.toString(),
                new NetworkCallback<Extractions>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Extractions e) {
                        mWaiter.assertNotNull(e);
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void retrieveExtractions_wasSuccessfulShouldContainACompanyName()
            throws InterruptedException, JSONException, TimeoutException {
        final String companyName = "Stadtwerke München";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionsResponse(null, companyName));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.retrieveExtractions(mMockUrl.toString(),
                new NetworkCallback<Extractions>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Extractions e) {
                        mWaiter.assertEquals(companyName, e.getCompanyName());
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void retrieveExtractions_wasSuccessfulShouldContainAConsumption()
            throws InterruptedException, JSONException, TimeoutException {
        final double consumptionValue = 315.0;
        final String consumptionUnit = "kWh";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionsResponse(null, null, null, consumptionValue, consumptionUnit));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.retrieveExtractions(mMockUrl.toString(),
                new NetworkCallback<Extractions>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Extractions e) {
                        mWaiter.assertEquals(consumptionValue, e.getConsumptionValue());
                        mWaiter.assertEquals(consumptionUnit, e.getConsumptionUnit());
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void retrieveExtractions_wasSuccessfulShouldContainASelfLink()
            throws InterruptedException, JSONException, TimeoutException {
        final String selfLink = "http://self_link";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionsResponse(selfLink));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.retrieveExtractions(mMockUrl.toString(),
                new NetworkCallback<Extractions>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Extractions e) {
                        mWaiter.assertEquals(selfLink, e.getSelf());
                        mWaiter.resume();
                    }
                });
        mWaiter.await();
    }

    @Test
    public void retrieveExtractions_wasSuccessfulShouldContainAnEnergyMeterNumber()
            throws InterruptedException, JSONException, TimeoutException {
        final String energyMeterNumber = "L4547";
        MockResponse mJSONMockResponse = new MockResponse().setBody(
                createMockExtractionsResponse(null, null, energyMeterNumber));
        mServer.enqueue(mJSONMockResponse);
        mTariffApi.retrieveExtractions(mMockUrl.toString(),
                new NetworkCallback<Extractions>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Extractions e) {
                        mWaiter.assertEquals(energyMeterNumber, e.getEnergyMeterNumber());
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

        when(mMockClientInformation.getSdkVersion()).thenReturn("1.2.3");
        when(mMockClientInformation.getDeviceModel()).thenReturn("Nexus 5");
        when(mMockClientInformation.getOsVersion()).thenReturn("25");

        when(mMockFile.getName()).thenReturn("file");

        mTariffApi = new TariffApiImpl(mOkHttpClient, mMockAuthenticationService, mMockUrl);
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

    private String createMockExtractionOrderState(final List<String> pages, final String self,
            final String pagesLink) {
        return createMockExtractionOrderState(pages, self, pagesLink, false);
    }

    private String createMockExtractionOrderState(final List<String> pages, final String self,
            final String pagesLink, final boolean extractionsComplete) {
        return "{\n"
                + "  \"extractionsComplete\" : " + extractionsComplete + ",\n"
                + "  \"_embedded\" : {\n"
                + "    \"pages\" : "
                + Arrays.toString(pages.toArray())
                + "  },\n"
                + "  \"_links\" : {\n"
                + "    \"self\" : {\n"
                + "      \"href\" : \"" + self + "\"\n"
                + "    },\n"
                + "    \"pages\" : {\n"
                + "      \"href\" : \"" + pagesLink + "\"\n"
                + "    }\n"
                + "  }\n"
                + "}";
    }

    private String createMockExtractionsResponse(final String selfLink) {
        return createMockExtractionsResponse(selfLink, "");
    }

    private String createMockExtractionsResponse(final String selfLink, final String companyName,
            final String energyMeterNumber,
            final double consumptionValue, final String consumptionUnit) {
        return "{\n"
                + "  \"companyName\" : {\n"
                + "    \"value\" : \"" + companyName + "\",\n"
                + "    \"alternatives\" : [ ]\n"
                + "  },\n"
                + "  \"energyMeterNumber\" : {\n"
                + "    \"value\" : \"" + energyMeterNumber + "\",\n"
                + "    \"alternatives\" : [ ]\n"
                + "  },\n"
                + "  \"consumption\" : {\n"
                + "    \"value\" : {\n"
                + "      \"value\" : " + consumptionValue + ",\n"
                + "      \"unit\" : \"" + consumptionUnit + "\"\n"
                + "    },\n"
                + "    \"alternatives\" : [ {\n"
                + "      \"value\" : 315.0,\n"
                + "      \"unit\" : \"kWh\"\n"
                + "    }, {\n"
                + "      \"value\" : 315.0,\n"
                + "      \"unit\" : \"kWh\"\n"
                + "    }, {\n"
                + "      \"value\" : 750.0,\n"
                + "      \"unit\" : \"kWh\"\n"
                + "    } ]\n"
                + "  },\n"
                + "  \"_links\" : {\n"
                + "    \"self\" : {\n"
                + "      \"href\" : \"" + selfLink + "\"\n"
                + "    }\n"
                + "  }\n"
                + "}";
    }

    private String createMockExtractionsResponse(final String selfLink, final String companyName,
            final String energyMeterNumber) {
        return createMockExtractionsResponse(selfLink, companyName, energyMeterNumber, 0.0, "");
    }

    private String createMockExtractionsResponse(final String selfLink, final String companyName) {
        return createMockExtractionsResponse(selfLink, companyName, "");
    }
}