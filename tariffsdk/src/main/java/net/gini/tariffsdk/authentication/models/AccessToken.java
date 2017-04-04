package net.gini.tariffsdk.authentication.models;


public class AccessToken {

    private final int mExpiresIn;
    private final String mScope;
    private final String mToken;
    private final String mType;

    public AccessToken(final int expiresIn, final String scope,
            final String token, final String type) {
        mExpiresIn = expiresIn;
        mScope = scope;
        mToken = token;
        mType = type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AccessToken that = (AccessToken) o;

        if (mExpiresIn != that.mExpiresIn) return false;
        if (mScope != null ? !mScope.equals(that.mScope) : that.mScope != null) return false;
        if (mToken != null ? !mToken.equals(that.mToken) : that.mToken != null) return false;
        return mType != null ? mType.equals(that.mType) : that.mType == null;

    }

    @Override
    public int hashCode() {
        int result = mExpiresIn;
        result = 31 * result + (mScope != null ? mScope.hashCode() : 0);
        result = 31 * result + (mToken != null ? mToken.hashCode() : 0);
        result = 31 * result + (mType != null ? mType.hashCode() : 0);
        return result;
    }

    public int getExpiresInSeconds() {
        return mExpiresIn;
    }

    public String getToken() {
        return mToken;
    }

    public String getType() {
        return mType;
    }
}
