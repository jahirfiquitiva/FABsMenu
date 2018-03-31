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
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

@SuppressWarnings("unused")
public class FABsMenuLayout extends FrameLayout implements CoordinatorLayout.AttachedBehavior {

    private static final String TAG = FABsMenuLayout.class.getSimpleName();
    @ColorInt
    private int overlayColor;
    private View overlayView;
    private boolean clickableOverlay;
    private int animationDuration = 500;

    public FABsMenuLayout(@NonNull Context context) {
        super(context);
    }

    public FABsMenuLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FABsMenuLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs,
                                                                    R.styleable.FABsMenuLayout, 0,
                                                                    0);
        try {
            overlayColor = attr.getColor(R.styleable.FABsMenuLayout_fabs_menu_overlayColor,
                                         Color.parseColor("#4d000000"));
            clickableOverlay =
                    attr.getBoolean(R.styleable.FABsMenuLayout_fabs_menu_clickableOverlay,
                                    true);
        } catch (Exception e) {
            Log.e(TAG, "Failure configuring FABsMenuLayout overlay", e);
        } finally {
            attr.recycle();
        }

        overlayView = new View(context);
        overlayView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        overlayView.setBackgroundColor(overlayColor);
        overlayView.setVisibility(View.GONE);
        addView(overlayView);

    }

    @ColorInt
    public int getOverlayColor() {
        return overlayColor;
    }

    public void setOverlayColor(@ColorInt int overlayColor) {
        overlayView.setBackgroundColor(overlayColor);
        this.overlayColor = overlayColor;
    }

    public View getOverlayView() {
        return overlayView;
    }

    public void setOverlayView(View overlayView) {
        this.overlayView = overlayView;
    }

    public boolean hasClickableOverlay() {
        return clickableOverlay;
    }

    public void setClickableOverlay(boolean clickableOverlay) {
        this.clickableOverlay = clickableOverlay;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void show() {
        toggle(true);
    }

    public void show(boolean immediately) {
        toggle(true, immediately);
    }

    public void hide() {
        toggle(false);
    }

    public void hide(boolean immediately) {
        toggle(false, immediately);
    }

    public void toggle(boolean show) {
        toggle(show, false);
    }

    public void toggle(boolean show, boolean immediately) {
        toggle(show, immediately, null);
    }

    public void toggle(final boolean show, boolean immediately,
                       final OnClickListener onOverlayClick) {
        if (show) {
            overlayView.setAlpha(0);
            overlayView.setVisibility(VISIBLE);
        }
        overlayView.animate().alpha(show ? 1.0F : 0.0F)
                .setDuration(immediately ? 0 : animationDuration).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (!show) {
                            overlayView.setVisibility(GONE);
                            overlayView.setOnClickListener(null);
                        } else {
                            if (hasClickableOverlay())
                                overlayView.setOnClickListener(onOverlayClick);
                        }
                    }
                }).start();
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return new FABSnackbarBehavior();
    }
}
