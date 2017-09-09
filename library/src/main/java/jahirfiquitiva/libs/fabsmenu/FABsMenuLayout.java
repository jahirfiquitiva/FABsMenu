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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

@CoordinatorLayout.DefaultBehavior(FABSnackbarBehavior.class)
public class FABsMenuLayout extends FrameLayout {

    @ColorInt
    private int overlayColor;
    private View overlayView;
    private boolean clickableOverlay;

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
        try {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                                                                     R.styleable.FABsMenuLayout, 0,
                                                                     0);
            overlayColor = a.getColor(R.styleable.FABsMenuLayout_fabs_menu_overlayColor,
                                      Color.parseColor("#4d000000"));
            clickableOverlay = a.getBoolean(R.styleable.FABsMenuLayout_fabs_menu_clickableOverlay,
                                            true);
            a.recycle();

            overlayView = new View(context);
            overlayView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            overlayView.setBackgroundColor(overlayColor);
            overlayView.setVisibility(View.GONE);
            addView(overlayView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ColorInt
    public int getOverlayColor() {
        return overlayColor;
    }

    public void setOverlayColor(@ColorInt int overlayColor) {
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
}