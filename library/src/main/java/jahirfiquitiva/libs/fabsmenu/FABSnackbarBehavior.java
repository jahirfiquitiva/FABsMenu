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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Credits to: https://goo.gl/ZWHwaw
 * <p>
 * Layout behavior that can be applied to either {@link TitleFAB} or {@link
 * FABsMenu} to make components automatically animate to stay above {@code Snackbar}
 * instances within the same parent {@code CoordinatorLayout}.
 * <p>
 * Usage:
 * <pre>
 * &lt;android.support.design.widget.CoordinatorLayout ...&gt;
 *   ...
 *   &lt;jahirfiquitiva.libs.fabsmenu.FABsMenuLayout
 *     ...
 *     app:layout_behavior="jahirfiquitiva.libs.fabsmenu.FABSnackbarBehavior"
 * /&gt;
 * &lt;/android.support.design.widget.CoordinatorLayout&gt;
 * </pre>
 */
@SuppressWarnings("unused")
public class FABSnackbarBehavior extends CoordinatorLayout.Behavior<View> {

    private float mTranslationY;

    public FABSnackbarBehavior() {
        super();
    }

    public FABSnackbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Find the {@code translation Y} value for any child Snackbar components.
     *
     * @return 0.0F if there are no Snackbar components found, otherwise returns the min offset that
     * the FAB component should be animated.
     */
    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, View fab) {
        float minOffset = 0.0F;
        final List<View> dependencies = parent.getDependencies(fab);

        for (View view : dependencies) {
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset, view.getTranslationY() - (float) view.getHeight());
            }
        }

        return minOffset;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            this.updateFabTranslationForSnackbar(parent, child, dependency);
        }
        return false;
    }

    /**
     * Animate FAB on snackbar change.
     */
    private void updateFabTranslationForSnackbar(CoordinatorLayout parent, View fab,
                                                 View snackbar) {
        final float translationY = getFabTranslationYForSnackbar(parent, fab);
        if (translationY != this.mTranslationY) {
            ViewCompat.animate(fab).cancel();
            if (Math.abs(translationY - this.mTranslationY) == (float) snackbar.getHeight()) {
                ViewCompat.animate(fab).translationY(translationY).setInterpolator(
                        new FastOutSlowInInterpolator());
            } else {
                fab.setTranslationY(translationY);
            }
            this.mTranslationY = translationY;
        }
    }
}