package net.gini.tariffsdk.authentication.user;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        final SharedPreferences sharedPreferences = getUserManagerSharedPreferences();
        sharedPreferences.edit().clear().apply();

    }

    @Test
    @SmallTest
    public void storedUser_isNotOverwritten() {
        final UserCredentials userCredentials = UserManager.getUserCredentials(mContext);
        final UserCredentials userCredentials2 = UserManager.getUserCredentials(mContext);

        assertEquals(userCredentials, userCredentials2);
    }

    @Test
    @SmallTest
    public void userStored_inSharedPreferences() {
        SharedPreferences sharedPreferences = getUserManagerSharedPreferences();
        assertFalse(sharedPreferences.contains(UserManager.USER_KEY_ID));

        UserManager.getUserCredentials(mContext);
        assertTrue(sharedPreferences.contains(UserManager.USER_KEY_ID));
    }

    private SharedPreferences getUserManagerSharedPreferences() {
        return mContext.getSharedPreferences(
                UserManager.USER_SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }

}