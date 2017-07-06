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
import android.util.AttributeSet;

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
    public void setTitle(String ignored) {
        super.setTitle(null);
    }

    @Override
    public String getTitle() {
        return null;
    }
}
