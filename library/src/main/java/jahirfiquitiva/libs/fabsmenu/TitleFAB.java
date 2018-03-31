/*
 * Copyright (c) 2017. Jahir Fiquitiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jahirfiquitiva.libs.fabsmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

@CoordinatorLayout.DefaultBehavior(FABSnackbarBehavior.class)
public class TitleFAB extends FloatingActionButton {

    private static final int MAX_CHARACTERS_COUNT = 25;

    /**
     * Hide/show animations from FloatingActionButton
     * https://goo.gl/e5DWfT
     */
    private static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR =
            new FastOutLinearInInterpolator();
    private static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR =
            new LinearOutSlowInInterpolator();

    private static final int SHOW_HIDE_ANIM_DURATION = 200;
    private static final int ANIM_STATE_NONE = 0;
    private static final int ANIM_STATE_HIDING = 1;
    private static final int ANIM_STATE_SHOWING = 2;
    private static final String TAG = TitleFAB.class.getSimpleName();

    int mAnimState = ANIM_STATE_NONE;

    private String title;
    private boolean titleClickEnabled;
    @ColorInt
    private int titleBackgroundColor;
    @ColorInt
    private int titleTextColor;
    private float titleCornerRadius;
    private int titleTextPadding;

    private View.OnClickListener clickListener;

    public TitleFAB(Context context) {
        this(context, null);
    }

    public TitleFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleFAB(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @SuppressLint("PrivateResource")
    void init(Context context, AttributeSet attributeSet) {
        int fabSize = TitleFAB.SIZE_MINI;
        TypedArray attr =
                context.obtainStyledAttributes(attributeSet, R.styleable.TitleFAB, 0, 0);
        try {
            title = attr.getString(R.styleable.TitleFAB_fab_title);
            titleClickEnabled = attr.getBoolean(R.styleable.TitleFAB_fab_enableTitleClick, true);
            titleBackgroundColor = attr.getInt(R.styleable.TitleFAB_fab_title_backgroundColor,
                                               ContextCompat
                                                       .getColor(context, android.R.color.white));
            titleTextColor = attr.getInt(R.styleable.TitleFAB_fab_title_textColor,
                                         ContextCompat.getColor(context, android.R.color.black));
            titleCornerRadius =
                    attr.getDimensionPixelSize(R.styleable.TitleFAB_fab_title_cornerRadius, -1);
            titleTextPadding =
                    attr.getDimensionPixelSize(R.styleable.TitleFAB_fab_title_textPadding,
                                               (int) DimensionUtils.convertDpToPixel(8, context));
            fabSize = attr.getInt(R.styleable.FloatingActionButton_fabSize, fabSize);
        } catch (Exception e) {
            Log.w(TAG, "Failure reading attributes", e);
        } finally {
            attr.recycle();
        }
        setOnClickListener(null);
        setSize(fabSize);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        super.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public OnClickListener getOnClickListener() {
        return clickListener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.clickListener = l;
        setClickable(l != null);
        super.setOnClickListener(l);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        setFocusable(clickable);
        View label = getLabelView();
        if (label != null)
            label.setOnClickListener(titleClickEnabled && clickable ? clickListener : null);
    }

    LabelView getLabelView() {
        return (LabelView) getTag(R.id.fab_label);
    }

    public String getTitle() {
        if (title == null) return null;
        StringBuilder titleBuilder = new StringBuilder();
        if (title.length() > MAX_CHARACTERS_COUNT) {
            titleBuilder.append(title.substring(0, MAX_CHARACTERS_COUNT)).append("...");
        } else {
            titleBuilder.append(title);
        }
        return titleBuilder.toString();
    }

    public void setTitle(String title) {
        this.title = title;
        LabelView label = getLabelView();
        if (label != null && label.getContent() != null) {
            label.getContent().setText(title);
        }
    }

    public boolean isTitleClickEnabled() {
        return titleClickEnabled;
    }

    public void setTitleClickEnabled(boolean titleClickEnabled) {
        this.titleClickEnabled = titleClickEnabled;
        LabelView label = getLabelView();
        if (label != null) {
            label.setClickable(titleClickEnabled);
        }
    }

    public int getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    public void setTitleBackgroundColor(@ColorInt int titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
        LabelView label = getLabelView();
        if (label != null) {
            label.setBackgroundColor(titleBackgroundColor);
        }
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(@ColorInt int titleTextColor) {
        this.titleTextColor = titleTextColor;
        LabelView label = getLabelView();
        if (label != null && label.getContent() != null) {
            label.getContent().setTextColor(titleTextColor);
        }
    }

    public float getTitleCornerRadius() {
        return titleCornerRadius;
    }

    public void setTitleCornerRadius(float titleCornerRadius) {
        this.titleCornerRadius = titleCornerRadius;
        LabelView label = getLabelView();
        if (label != null) {
            label.setRadius(titleCornerRadius);
        }
    }

    public int getTitleTextPadding() {
        return titleTextPadding;
    }

    public void setTitleTextPadding(int titleTextPadding) {
        this.titleTextPadding = titleTextPadding;
        LabelView label = getLabelView();
        if (label != null && label.getContent() != null) {
            label.getContent().setPadding(titleTextPadding, titleTextPadding / 2, titleTextPadding,
                                          titleTextPadding / 2);
        }
    }

    boolean isOrWillBeShown() {
        View label = getLabelView();
        if (label == null) return false;
        if (label.getVisibility() != View.VISIBLE) {
            // If we not currently visible, return true if we're animating to be shown
            return mAnimState == ANIM_STATE_SHOWING;
        } else {
            // Otherwise if we're visible, return true if we're not animating to be hidden
            return mAnimState != ANIM_STATE_HIDING;
        }
    }

    boolean isOrWillBeHidden() {
        View label = getLabelView();
        if (label == null) return true;
        if (label.getVisibility() == View.VISIBLE) {
            // If we currently visible, return true if we're animating to be hidden
            return mAnimState == ANIM_STATE_HIDING;
        } else {
            // Otherwise if we're not visible, return true if we're not animating to be shown
            return mAnimState != ANIM_STATE_SHOWING;
        }
    }

    @Override
    public void show() {
        final View label = getLabelView();
        if (label == null) {
            super.show();
            return;
        }

        if (isOrWillBeShown()) {
            // We either are or will soon be visible, skip the call
            return;
        }

        label.animate().cancel();

        if (shouldAnimateVisibilityChange()) {
            mAnimState = ANIM_STATE_SHOWING;

            if (label.getVisibility() != View.VISIBLE) {
                // If the view isn't visible currently, we'll animate it from a single pixel
                label.setAlpha(0f);
                label.setScaleY(0f);
                label.setScaleX(0f);
            }

            label.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(SHOW_HIDE_ANIM_DURATION)
                    .setInterpolator(LINEAR_OUT_SLOW_IN_INTERPOLATOR)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            TitleFAB.super.show();
                            label.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnimState = ANIM_STATE_NONE;
                        }
                    });
        } else {
            label.setVisibility(View.VISIBLE);
            label.setAlpha(1f);
            label.setScaleY(1f);
            label.setScaleX(1f);
        }
    }

    @Override
    public void hide() {
        final View label = getLabelView();
        if (label == null) {
            super.hide();
            return;
        }

        if (isOrWillBeHidden()) {
            // We either are or will soon be hidden, skip the call
            return;
        }

        label.animate().cancel();

        if (shouldAnimateVisibilityChange()) {
            mAnimState = ANIM_STATE_HIDING;

            label.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .alpha(0f)
                    .setDuration(SHOW_HIDE_ANIM_DURATION)
                    .setInterpolator(FAST_OUT_LINEAR_IN_INTERPOLATOR)
                    .setListener(new AnimatorListenerAdapter() {
                        private boolean mCancelled;

                        @Override
                        public void onAnimationStart(Animator animation) {
                            TitleFAB.super.hide();
                            label.setVisibility(View.VISIBLE);
                            mCancelled = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            mCancelled = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnimState = ANIM_STATE_NONE;

                            if (!mCancelled) {
                                label.setVisibility(View.GONE);
                            }
                        }
                    });
        } else {
            // If the view isn't laid out, or we're in the editor, don't run the animation
            label.setVisibility(View.GONE);
        }
    }

    private boolean shouldAnimateVisibilityChange() {
        View label = getLabelView();
        if (label != null) {
            return ViewCompat.isLaidOut(this) && ViewCompat.isLaidOut(label) && !isInEditMode();
        } else {
            return ViewCompat.isLaidOut(this) && !isInEditMode();
        }
    }
}
