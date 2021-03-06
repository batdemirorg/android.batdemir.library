package com.android.batdemir.mylibrary.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.AnimRes;

import com.android.batdemir.mylibrary.R;

import java.util.Objects;

public class Tool {

    @SuppressLint("StaticFieldLeak")
    private static Tool ourInstance = null;
    private Context context;

    private Tool() {
    }

    public static Tool getInstance(Context context) {
        if (ourInstance == null) ourInstance = new Tool();
        ourInstance.setContext(context);
        return ourInstance;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public void move(Class<?> to, boolean isLeft, boolean isHistory, Bundle bundle) {
        try {
            Activity activity = (Activity) context;
            Intent intent = new Intent(context, to);
            if (bundle != null)
                intent.putExtras(bundle);
            context.startActivity(intent);
            if (isLeft) {
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                if (!isHistory)
                    activity.finish();
            } else {
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                if (!isHistory)
                    activity.finish();
            }
        } catch (Exception e) {
            Log.e(Tool.class.getSimpleName(), Objects.requireNonNull(e.getMessage()));
        }
    }

    public void move(Class<?> to, View view, boolean isLeft, boolean isHistory, Bundle bundle) {
        try {
            ProgressBar progressBar = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyle);
            progressBar.setLayoutParams(view.getLayoutParams());
            progressBar.setIndeterminate(true);
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.addView(progressBar);
            view.setVisibility(View.GONE);
            Activity activity = (Activity) context;
            Intent intent = new Intent(context, to);
            if (bundle != null)
                intent.putExtras(bundle);
            context.startActivity(intent);
            if (isLeft) {
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                if (!isHistory)
                    activity.finish();
            } else {
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                if (!isHistory)
                    activity.finish();
            }
        } catch (Exception e) {
            Log.e(Tool.class.getSimpleName(), Objects.requireNonNull(e.getMessage()));
        }
    }

    public void anim(View view, @AnimRes int animId) {
        try {
            Animation animation = AnimationUtils.loadAnimation(context, animId == 0 ? R.anim.slide_down : animId);
            view.startAnimation(animation);
        } catch (Exception e) {
            Log.e(Tool.class.getSimpleName(), Objects.requireNonNull(e.getMessage()));
        }
    }
}
