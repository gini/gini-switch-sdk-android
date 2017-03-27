package net.gini.tariffsdk.user;


public class UserCredentials {
    private final String mUserId;

    public UserCredentials(final String userId) {
        mUserId = userId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserCredentials that = (UserCredentials) o;

        return mUserId != null ? mUserId.equals(that.mUserId) : that.mUserId == null;

    }

    @Override
    public int hashCode() {
        return mUserId != null ? mUserId.hashCode() : 0;
    }

    public String getUserId() {
        return mUserId;
    }
}
