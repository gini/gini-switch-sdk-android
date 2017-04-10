package net.gini.tariffsdk.authentication.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import net.gini.tariffsdk.authentication.models.UserCredentials;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserManagerTest {

    private Context mContext;

    private UserManager mDefaultDomainUserManager;

    @Test
    @SmallTest
    public void credentialsStored_inSharedPreferences() {
        SharedPreferences sharedPreferences = getUserManagerSharedPreferences();
        assertFalse(sharedPreferences.contains(UserManager.USER_KEY_PASSWORD));
        assertFalse(sharedPreferences.contains(UserManager.USER_KEY_EMAIL));

        mDefaultDomainUserManager.getOrCreateUserCredentials();
        assertTrue(sharedPreferences.contains(UserManager.USER_KEY_PASSWORD));
        assertTrue(sharedPreferences.contains(UserManager.USER_KEY_EMAIL));
    }

    @Test
    @SmallTest
    public void customDomain_shouldBeUsed() {

        final String customDomain = "custom-doma.in";
        final UserManager userManager = new UserManager(mContext, customDomain);
        UserCredentials userCredentials = userManager.getOrCreateUserCredentials();
        assertThat(userCredentials.getEmail(), endsWith("@" + customDomain));
    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        final SharedPreferences sharedPreferences = getUserManagerSharedPreferences();
        sharedPreferences.edit().clear().apply();

        mDefaultDomainUserManager = new UserManager(mContext, null);
    }

    @Test
    @SmallTest
    public void storedCredentials_areNotOverwritten() {
        final UserCredentials userCredentials =
                mDefaultDomainUserManager.getOrCreateUserCredentials();
        final UserCredentials userCredentials2 =
                mDefaultDomainUserManager.getOrCreateUserCredentials();

        assertEquals(userCredentials, userCredentials2);
    }

    private SharedPreferences getUserManagerSharedPreferences() {
        return mContext.getSharedPreferences(
                UserManager.USER_SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }

}