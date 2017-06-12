package net.gini.tariffsdk.onboarding;


import net.gini.tariffsdk.R;

enum OnboardingPage {
    FLAT(R.string.onboarding_flat, R.drawable.ic_onboarding_flat),
    PARALLEL(R.string.onboarding_parallel, R.drawable.ic_onboarding_parallel),
    ALIGN(R.string.onboarding_align, R.drawable.ic_onboarding_align);

    private final int mDrawable;
    private final int mText;

    OnboardingPage(final int text, final int drawable) {
        mText = text;
        mDrawable = drawable;
    }

    public int getDrawable() {
        return mDrawable;
    }

    public int getText() {
        return mText;
    }
}
