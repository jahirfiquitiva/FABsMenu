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
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

@CoordinatorLayout.DefaultBehavior(FABSnackbarBehavior.class)
public class TitleFAB extends FloatingActionButton {

    private static final int MAX_CHARACTERS_COUNT = 25;
    private String mTitle;
    private boolean titleClickEnabled;
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
        mTitle = attr.getString(R.styleable.TitleFAB_fab_title);
        titleClickEnabled = attr.getBoolean(R.styleable.TitleFAB_fab_enableTitleClick, false);
        attr.recycle();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        this.clickListener = l;
    }

    public OnClickListener getClickListener() {
        return clickListener;
    }

    TextView getLabelView() {
        return (TextView) getTag(R.id.fab_label);
    }

    public String getTitle() {
        if (mTitle == null) return null;
        StringBuilder title = new StringBuilder();
        if (mTitle.length() > MAX_CHARACTERS_COUNT) {
            title.append(mTitle.substring(0, MAX_CHARACTERS_COUNT)).append("...");
        } else {
            title.append(mTitle);
        }
        return title.toString();
    }

    public void setTitle(String title) {
        mTitle = title;
        TextView label = getLabelView();
        if (label != null) {
            label.setText(title);
        }
    }

    public boolean isTitleClickEnabled() {
        return titleClickEnabled;
    }

    public void setTitleClickEnabled(boolean titleClickEnabled) {
        this.titleClickEnabled = titleClickEnabled;
    }
}