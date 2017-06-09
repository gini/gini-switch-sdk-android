package net.gini.tariffsdk.onboarding;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gini.tariffsdk.R;

public class OnboardingAdapter extends PagerAdapter {
    private final Context mContext;

    public OnboardingAdapter(final Context context) {
        mContext = context;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return OnboardingPage.values().length + 1;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.container_onboarding, collection, false);
        if (position < OnboardingPage.values().length) {
            OnboardingPage onboardingPage = OnboardingPage.values()[position];
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            imageView.setImageResource(onboardingPage.getDrawable());
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(onboardingPage.getText());
            view.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.onboarding_background));
        }
        collection.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == object;
    }

    public boolean isLastItem(final int position) {
        return getCount() - 1 == position;
    }
}
