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
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class LabelView extends CardView {

    @ColorInt
    private int rightBackgroundColor;
    private TextView content;

    public LabelView(Context context, @ColorInt int backgroundColor) {
        super(context);
        rightBackgroundColor = backgroundColor;
        setCardBackgroundColor(0);
    }

    public LabelView(Context context, AttributeSet attrs, @ColorInt int backgroundColor) {
        super(context, attrs);
        rightBackgroundColor = backgroundColor;
        setCardBackgroundColor(0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr,
                     @ColorInt int backgroundColor) {
        super(context, attrs, defStyleAttr);
        rightBackgroundColor = backgroundColor;
        setCardBackgroundColor(0);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (l != null) {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));
        } else {
            setForeground(null);
        }
        super.setOnClickListener(l);
    }

    @Override
    public void setForeground(Drawable foreground) {
        super.setForeground(foreground);
    }

    public TextView getContent() {
        return content;
    }

    public void setContent(TextView content) {
        this.content = content;
    }

    @Override
    public void setCardBackgroundColor(@ColorInt int ignored) {
        super.setCardBackgroundColor(rightBackgroundColor);
    }
}