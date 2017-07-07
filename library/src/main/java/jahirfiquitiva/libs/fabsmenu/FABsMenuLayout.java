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
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FABsMenuLayout extends FrameLayout {

    @ColorInt
    private int backgroundColor;

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
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FABsMenuLayout, 0, 0);
        backgroundColor = a.getColor(R.styleable.FABsMenuLayout_fabs_menu_backgroundColor,
                ContextCompat.getColor(context, android.R.color.black));
        a.recycle();
    }

}
