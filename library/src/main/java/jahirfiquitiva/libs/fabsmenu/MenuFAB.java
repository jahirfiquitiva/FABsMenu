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
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

@CoordinatorLayout.DefaultBehavior(FABSnackbarBehavior.class)
public class MenuFAB extends TitleFAB {

    public MenuFAB(Context context) {
        super(context);
    }

    public MenuFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuFAB(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String ignored) {
        super.setTitle(null);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        throw new UnsupportedOperationException("Don't set the bitmap for menu button using " +
                                                        "this method. Use FABs Menu " +
                                                        "setMenuButtonIcon() method instead.");
    }

    @Override
    public void setImageIcon(@Nullable Icon icon) {
        throw new UnsupportedOperationException("This method is not available for now." +
                                                        " Use FABs Menu setMenuButtonIcon() " +
                                                        "method instead.");
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        throw new UnsupportedOperationException("Don't set the uri for menu button using " +
                                                        "this method. Use FABs Menu " +
                                                        "setMenuButtonIcon() method instead.");
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        throw new UnsupportedOperationException("Don't set the resource for menu button using " +
                                                        "this method. Use FABs Menu " +
                                                        "setMenuButtonIcon() method instead.");
    }
}