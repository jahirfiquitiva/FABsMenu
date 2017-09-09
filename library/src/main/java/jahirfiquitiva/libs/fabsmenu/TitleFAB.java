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
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

@CoordinatorLayout.DefaultBehavior(FABSnackbarBehavior.class)
public class TitleFAB extends FloatingActionButton {

    private static final int MAX_CHARACTERS_COUNT = 25;
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

    void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.TitleFAB, 0, 0);
        title = attr.getString(R.styleable.TitleFAB_fab_title);
        titleClickEnabled = attr.getBoolean(R.styleable.TitleFAB_fab_enableTitleClick, false);
        titleBackgroundColor = attr.getInt(R.styleable.TitleFAB_fab_title_backgroundColor,
                                           ContextCompat.getColor(context, android.R.color.white));
        titleTextColor = attr.getInt(R.styleable.TitleFAB_fab_title_textColor,
                                     ContextCompat.getColor(context, android.R.color.black));
        titleCornerRadius = attr.getDimensionPixelSize(
                R.styleable.TitleFAB_fab_title_cornerRadius, -1);
        titleTextPadding = attr.getDimensionPixelSize(R.styleable.TitleFAB_fab_title_textPadding,
                                                      (int) DimensionUtils
                                                              .convertDpToPixel(8, context));
        attr.recycle();
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
        super.setOnClickListener(l);
        this.clickListener = l;
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
        if (label != null && label.getContent() != null) {
            label.getContent().setBackgroundColor(titleBackgroundColor);
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
}