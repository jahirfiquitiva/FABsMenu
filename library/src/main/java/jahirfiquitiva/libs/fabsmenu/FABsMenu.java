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
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("unused")
@CoordinatorLayout.DefaultBehavior(FABSnackbarBehavior.class)
public class FABsMenu extends ViewGroup {
    public static final int EXPAND_UP = 0;
    public static final int EXPAND_DOWN = 1;
    public static final int EXPAND_LEFT = 2;
    public static final int EXPAND_RIGHT = 3;
    public static final int LABELS_ON_LEFT_SIDE = 0;
    public static final int LABELS_ON_RIGHT_SIDE = 1;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;
    private static Interpolator expandInterpolator = new OvershootInterpolator();
    private static Interpolator collapseInterpolator = new DecelerateInterpolator(3f);
    private static Interpolator alphaExpandInterpolator = new DecelerateInterpolator();
    private int animationDuration = 500;
    private int menuMargins;
    private int menuTopMargin;
    private int menuBottomMargin;
    private int menuRightMargin;
    private int menuLeftMargin;
    private int menuButtonColor;
    private int menuButtonRippleColor;
    private int menuButtonSize;
    private int expandDirection;
    private MenuFAB menuButton;
    private Drawable menuButtonIcon;
    private RotatingDrawable rotatingDrawable;
    private int buttonSpacing;
    private int labelsMargin;
    private int labelsVerticalOffset;
    private int labelsPosition;
    private boolean expanded;
    private AnimatorSet expandAnimation = new AnimatorSet().setDuration(animationDuration);
    private AnimatorSet collapseAnimation = new AnimatorSet().setDuration(animationDuration);
    private int buttonsCount;
    private int maxButtonWidth;
    private int maxButtonHeight;
    private TouchDelegateGroup touchDelegateGroup;
    private FABsMenuListener menuListener;

    public FABsMenu(Context context) {
        this(context, null);
    }

    public FABsMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FABsMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        try {
            buttonSpacing = (int) DimensionUtils.convertDpToPixel(16, context);
            labelsMargin = getResources().getDimensionPixelSize(R.dimen.fab_labels_margin);
            labelsVerticalOffset = (int) DimensionUtils.convertDpToPixel(-1.5f, context);

            touchDelegateGroup = new TouchDelegateGroup(this);
            setTouchDelegate(touchDelegateGroup);

            TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.FABsMenu,
                                                             0, 0);

            menuMargins = attr.getDimensionPixelSize(R.styleable.FABsMenu_fab_menuMargins, 0);

            menuTopMargin = attr.getDimensionPixelSize(R.styleable.FABsMenu_fab_menuTopMargin,
                                                       menuMargins != 0
                                                       ? menuMargins
                                                       : (int) DimensionUtils
                                                               .convertDpToPixel(16, context));

            menuBottomMargin = attr.getDimensionPixelSize(R.styleable.FABsMenu_fab_menuBottomMargin,
                                                          menuMargins != 0
                                                          ? menuMargins
                                                          : (int) DimensionUtils
                                                                  .convertDpToPixel(16, context));

            menuRightMargin = attr.getDimensionPixelSize(R.styleable.FABsMenu_fab_menuRightMargin,
                                                         menuMargins != 0
                                                         ? menuMargins
                                                         : (int) DimensionUtils
                                                                 .convertDpToPixel(16, context));

            menuLeftMargin = attr.getDimensionPixelSize(R.styleable.FABsMenu_fab_menuLeftMargin,
                                                        menuMargins != 0
                                                        ? menuMargins
                                                        : (int) DimensionUtils
                                                                .convertDpToPixel(16, context));

            menuButtonIcon = attr.getDrawable(R.styleable.FABsMenu_fab_moreButtonPlusIcon);

            menuButtonColor = attr.getColor(R.styleable.FABsMenu_fab_moreButtonBackgroundColor,
                                            getColor(android.R.color.holo_blue_dark));
            menuButtonRippleColor = attr.getColor(R.styleable.FABsMenu_fab_moreButtonRippleColor,
                                                  getColor(android.R.color.holo_blue_light));

            menuButtonSize = attr.getInt(R.styleable.FABsMenu_fab_moreButtonSize,
                                         TitleFAB.SIZE_NORMAL);

            expandDirection = attr.getInt(R.styleable.FABsMenu_fab_expandDirection, EXPAND_UP);

            labelsPosition = attr.getInt(R.styleable.FABsMenu_fab_labelsPosition,
                                         isRtl() ? LABELS_ON_RIGHT_SIDE : LABELS_ON_LEFT_SIDE);

            attr.recycle();

            if (menuListener == null) {
                setMenuListener(new FABsMenuListener() {
                });
            }
            createMenuButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean expandsHorizontally() {
        return expandDirection == EXPAND_LEFT || expandDirection == EXPAND_RIGHT;
    }

    private void createMenuButton(Context context) {
        menuButton = new MenuFAB(context);

        if (menuButtonIcon != null) {
            createRotatingDrawable();
        }

        menuButton.setBackgroundTintList(ColorStateList.valueOf(menuButtonColor));
        menuButton.setRippleColor(menuButtonRippleColor);

        menuButton.setId(R.id.fab_expand_menu_button);
        menuButton.setSize(menuButtonSize);
        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuListener != null) menuListener.onMenuClicked(FABsMenu.this);
            }
        });

        addView(menuButton, super.generateDefaultLayoutParams());
        buttonsCount++;
    }

    private void createRotatingDrawable() {
        RotatingDrawable dr = new RotatingDrawable(menuButtonIcon);

        final OvershootInterpolator interpolator = new OvershootInterpolator();

        final ObjectAnimator collapseAnimator = ObjectAnimator
                .ofFloat(dr, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
        final ObjectAnimator expandAnimator = ObjectAnimator
                .ofFloat(dr, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);

        collapseAnimator.setInterpolator(interpolator);
        expandAnimator.setInterpolator(interpolator);

        expandAnimation.play(expandAnimator);
        collapseAnimation.play(collapseAnimator);

        menuButton.setImageDrawable(dr);
        rotatingDrawable = dr;
    }

    public void addAllButtons(TitleFAB... buttons) throws IllegalArgumentException {
        for (TitleFAB button : buttons) {
            addButton(button);
        }
    }

    public void addButton(TitleFAB button, int index) throws IllegalArgumentException {
        if (buttonsCount >= 6)
            throw new IllegalArgumentException("A floating action buttons menu should have no " +
                                                       "more than six options.");
        addView(button, index);
        buttonsCount += 1;
        createLabels();
        if (buttonsCount > 1 && getVisibility() != View.VISIBLE) {
            show(false);
        }
        if (buttonsCount < 3)
            Log.w("FABsMenu", "A floating action buttons menu should have at least three options");
    }

    public void addButton(TitleFAB button) throws IllegalArgumentException {
        addButton(button, buttonsCount - 1);
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    private void removeButtonInternal(int index, boolean throwException)
            throws NullPointerException, IllegalArgumentException {
        View child = getChildAt(index);
        if (child != null) {
            if (child instanceof MenuFAB) {
                return;
            } else if (child instanceof TitleFAB) {
                removeButton((TitleFAB) child);
            } else {
                if (throwException)
                    throw new IllegalArgumentException(
                            "The view you want to remove is not an instance of TitleFAB");
            }
        } else {
            if (throwException)
                throw new NullPointerException("The button you want to remove does not exists");
        }
    }

    @SuppressLint("ResourceType")
    public void removeButton(int index) throws IndexOutOfBoundsException {
        removeButtonInternal(index, false);
    }

    public void removeButton(TitleFAB button) {
        try {
            button.hide();
            removeView(button.getLabelView());
            removeView(button);
            button.setTag(R.id.fab_label, null);
            buttonsCount -= 1;
            if (buttonsCount <= 1) hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllButtons() {
        for (int i = 0; i <= getChildCount(); i++) {
            removeButton(0);
        }
    }

    private int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = buttonSpacing;
        int height = buttonSpacing;

        maxButtonWidth = 0;
        maxButtonHeight = 0;
        int maxLabelWidth = 0;

        for (int i = 0; i < buttonsCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            switch (expandDirection) {
                case EXPAND_UP:
                case EXPAND_DOWN:
                    maxButtonWidth = Math.max(maxButtonWidth, child.getMeasuredWidth());
                    height += child.getMeasuredHeight();
                    break;
                case EXPAND_LEFT:
                case EXPAND_RIGHT:
                    width += child.getMeasuredWidth();
                    maxButtonHeight = Math.max(maxButtonHeight, child.getMeasuredHeight());
                    break;
                default: // Do Nothing
                    break;
            }

            if (!expandsHorizontally()) {
                LabelView label = (LabelView) child.getTag(R.id.fab_label);
                if (label != null) {
                    maxLabelWidth = Math.max(maxLabelWidth, label.getMeasuredWidth());
                }
            }
        }

        if (!expandsHorizontally()) {
            width = maxButtonWidth + (maxLabelWidth > 0 ? maxLabelWidth + labelsMargin : 0);
        } else {
            height = maxButtonHeight;
        }

        switch (expandDirection) {
            case EXPAND_UP:
            case EXPAND_DOWN:
                height += buttonSpacing * (buttonsCount - 1);
                height = adjustForOvershoot(height);
                break;
            case EXPAND_LEFT:
            case EXPAND_RIGHT:
                width += buttonSpacing * (buttonsCount - 1);
                width = adjustForOvershoot(width);
                break;
            default: // Do Nothing
                break;
        }

        height += (Math.max(menuTopMargin, menuBottomMargin)) * 2;
        width += (Math.max(menuLeftMargin, menuRightMargin)) * 2;

        setMeasuredDimension(width, height);
    }

    private int adjustForOvershoot(int dimension) {
        return dimension * 12 / 10;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (expandDirection) {
            case EXPAND_UP:
            case EXPAND_DOWN:
                boolean expandUp = expandDirection == EXPAND_UP;

                touchDelegateGroup.clearTouchDelegates();

                int addButtonY = expandUp ? b - t - menuButton.getMeasuredHeight() : 0;

                if (expandUp) addButtonY -= menuBottomMargin;
                else addButtonY += menuTopMargin;

                // Ensure menuButton is centered on the line where the buttons should be
                int buttonsHorizontalCenter = labelsPosition == LABELS_ON_LEFT_SIDE
                                              ? r - l - maxButtonWidth / 2
                                              : maxButtonWidth / 2;

                buttonsHorizontalCenter -= labelsPosition == LABELS_ON_LEFT_SIDE
                                           ? menuRightMargin
                                           : -menuLeftMargin;

                int addButtonLeft = buttonsHorizontalCenter - menuButton.getMeasuredWidth() / 2;
                menuButton.layout(addButtonLeft, addButtonY,
                                  addButtonLeft + menuButton.getMeasuredWidth(),
                                  addButtonY + menuButton.getMeasuredHeight());

                int labelsOffset = maxButtonWidth / 2 + labelsMargin;
                int labelsXNearButton = labelsPosition == LABELS_ON_LEFT_SIDE
                                        ? buttonsHorizontalCenter - labelsOffset
                                        : buttonsHorizontalCenter + labelsOffset;

                int nextY = expandUp ?
                            addButtonY - buttonSpacing :
                            addButtonY + menuButton.getMeasuredHeight() + buttonSpacing;

                for (int i = buttonsCount - 1; i >= 0; i--) {
                    final View child = getChildAt(i);

                    if (child.equals(menuButton) || child.getVisibility() == GONE) continue;

                    int childX = buttonsHorizontalCenter - child.getMeasuredWidth() / 2;
                    int childY = expandUp ? nextY - child.getMeasuredHeight() : nextY;
                    child.layout(childX, childY, childX + child.getMeasuredWidth(), childY +
                            child.getMeasuredHeight());

                    float collapsedTranslation = addButtonY - childY;
                    float expandedTranslation = 0f;

                    child.setTranslationY(expanded ? expandedTranslation : collapsedTranslation);
                    child.setAlpha(expanded ? 1f : 0f);

                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
                    params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
                    params.setAnimationsTarget(child);

                    View label = (View) child.getTag(R.id.fab_label);
                    if (label != null) {
                        int labelXAwayFromButton = labelsPosition == LABELS_ON_LEFT_SIDE
                                                   ? labelsXNearButton - label.getMeasuredWidth()
                                                   : labelsXNearButton + label.getMeasuredWidth();

                        int labelLeft = labelsPosition == LABELS_ON_LEFT_SIDE
                                        ? labelXAwayFromButton
                                        : labelsXNearButton;

                        int labelRight = labelsPosition == LABELS_ON_LEFT_SIDE
                                         ? labelsXNearButton
                                         : labelXAwayFromButton;

                        int labelTop = childY - labelsVerticalOffset +
                                (child.getMeasuredHeight() - label.getMeasuredHeight()) / 2;

                        label.layout(labelLeft, labelTop, labelRight, labelTop +
                                label.getMeasuredHeight());

                        Rect touchArea = new Rect(
                                Math.min(childX, labelLeft),
                                childY - buttonSpacing / 2,
                                Math.max(childX + child.getMeasuredWidth(), labelRight),
                                childY + child.getMeasuredHeight() + buttonSpacing / 2);
                        touchDelegateGroup.addTouchDelegate(new TouchDelegate(touchArea, child));

                        label.setTranslationY(expanded ? expandedTranslation :
                                              collapsedTranslation);
                        label.setAlpha(expanded ? 1f : 0f);

                        LayoutParams labelParams = (LayoutParams) label.getLayoutParams();
                        labelParams.mCollapseDir.setFloatValues(expandedTranslation,
                                                                collapsedTranslation);
                        labelParams.mExpandDir.setFloatValues(collapsedTranslation,
                                                              expandedTranslation);
                        labelParams.setAnimationsTarget(label);
                    }

                    nextY = expandUp ?
                            childY - buttonSpacing :
                            childY + child.getMeasuredHeight() + buttonSpacing;
                }
                break;

            case EXPAND_LEFT:
            case EXPAND_RIGHT:
                boolean expandLeft = expandDirection == EXPAND_LEFT;

                int addButtonX = expandLeft ? r - l - menuButton.getMeasuredWidth() : 0;

                if (expandLeft) addButtonX -= menuRightMargin;
                else addButtonX += menuLeftMargin;

                // Ensure menuButton is centered on the line where the buttons should be
                int addButtonTop = b - t - maxButtonHeight + (maxButtonHeight -
                        menuButton.getMeasuredHeight()) / 2;

                addButtonTop -= menuBottomMargin;

                menuButton.layout(addButtonX, addButtonTop, addButtonX + menuButton
                        .getMeasuredWidth(), addButtonTop + menuButton.getMeasuredHeight());

                int nextX = expandLeft ?
                            addButtonX - buttonSpacing :
                            addButtonX + menuButton.getMeasuredWidth() + buttonSpacing;

                for (int i = buttonsCount - 1; i >= 0; i--) {
                    final View child = getChildAt(i);

                    if (child.equals(menuButton) || child.getVisibility() == GONE) continue;

                    int childX = expandLeft ? nextX - child.getMeasuredWidth() : nextX;
                    int childY = addButtonTop + (menuButton.getMeasuredHeight() -
                            child.getMeasuredHeight()) / 2;
                    child.layout(childX, childY, childX + child.getMeasuredWidth(), childY +
                            child.getMeasuredHeight());

                    float collapsedTranslation = addButtonX - childX;
                    float expandedTranslation = 0f;

                    child.setTranslationX(expanded ? expandedTranslation : collapsedTranslation);
                    child.setAlpha(expanded ? 1f : 0f);

                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
                    params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
                    params.setAnimationsTarget(child);

                    nextX = expandLeft ?
                            childX - buttonSpacing :
                            childX + child.getMeasuredWidth() + buttonSpacing;
                }
                break;
            default: // Do Nothing
                break;
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(super.generateLayoutParams(attrs));
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(super.generateLayoutParams(p));
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bringChildToFront(menuButton);
        buttonsCount = getChildCount();

        createLabels();
    }

    private void createLabels() {
        if (!expandsHorizontally()) {
            for (int i = 0; i < buttonsCount; i++) {
                final TitleFAB button = (TitleFAB) getChildAt(i);
                String title = button.getTitle();

                if (button.equals(menuButton) || title == null || title.length() <= 0 ||
                        button.getTag(R.id.fab_label) != null) continue;

                final LabelView label = new LabelView(getContext(),
                                                      button.getTitleBackgroundColor());
                label.setId(i + 1);

                if (button.getTitleCornerRadius() != -1)
                    label.setRadius(button.getTitleCornerRadius());

                final TextView labelText = new TextView(getContext());
                labelText.setText(button.getTitle());
                labelText.setTextColor(button.getTitleTextColor());
                int mLabelTextPadding = button.getTitleTextPadding();
                labelText.setPadding(mLabelTextPadding, mLabelTextPadding / 2, mLabelTextPadding,
                                     mLabelTextPadding / 2);

                label.addView(labelText);
                label.setContent(labelText);
                addView(label);

                button.setTag(R.id.fab_label, label);
            }
        } else {
            Log.e("FABs Menu", "FABs menu items can't have labels when the menu expands " +
                    "horizontally");
        }
    }

    private void toggleOverlay(final boolean show, boolean immediately) {
        final ViewParent parent = getParent();
        if (parent != null && parent instanceof FABsMenuLayout) {
            ((FABsMenuLayout) parent).toggle(show, immediately, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collapse();
                }
            });
        }
    }

    public void collapse() {
        collapse(false);
    }

    public void collapseImmediately() {
        collapse(true);
    }

    private AnimatorListenerAdapter collapseListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            setMenuButtonsClickable(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            setMenuButtonsVisibility(false);
        }
    };

    private void collapse(boolean immediately) {
        if (expanded) {
            expanded = false;
            touchDelegateGroup.setEnabled(false);
            toggleOverlay(false, immediately);
            collapseAnimation.setDuration(immediately ? 0 : animationDuration);
            collapseAnimation.removeListener(collapseListener);
            collapseAnimation.addListener(collapseListener);
            collapseAnimation.start();
            expandAnimation.cancel();
            if (menuListener != null) {
                menuListener.onMenuCollapsed(this);
            }
        }
    }

    public void toggle() {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
    }

    private AnimatorListenerAdapter expandListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            setMenuButtonsVisibility(true);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            setMenuButtonsClickable(true);
        }
    };

    public void expand() {
        if (!expanded) {
            expanded = true;
            touchDelegateGroup.setEnabled(true);
            toggleOverlay(true, false);
            collapseAnimation.cancel();
            expandAnimation.removeListener(expandListener);
            expandAnimation.addListener(expandListener);
            expandAnimation.start();
            if (menuListener != null) {
                menuListener.onMenuExpanded(this);
            }
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    private boolean isRtl() {
        return getResources().getBoolean(R.bool.is_right_to_left);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        menuButton.setEnabled(enabled);
    }

    public void setMenuButtonsVisibility(boolean visible) {
        for (int i = 0; i < buttonsCount; i++) {
            View child = getChildAt(i);
            if ((child instanceof TitleFAB) && (!(child instanceof MenuFAB))) {
                if (visible) ((TitleFAB) child).show();
                else ((TitleFAB) child).hide();
            }
        }
    }

    public void setMenuButtonsClickable(boolean clickable) {
        for (int i = 0; i < buttonsCount; i++) {
            View child = getChildAt(i);
            if ((child instanceof TitleFAB) && (!(child instanceof MenuFAB))) {
                child.setClickable(clickable);
            }
        }
    }

    public void attachToRecyclerView(@NonNull RecyclerView rv) {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (isExpanded()) collapse(true);
                    menuButton.hide();
                } else menuButton.show();
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.expanded = expanded;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            expanded = savedState.expanded;
            touchDelegateGroup.setEnabled(expanded);
            if (rotatingDrawable != null) {
                rotatingDrawable.setRotation(expanded
                                             ? EXPANDED_PLUS_ROTATION
                                             : COLLAPSED_PLUS_ROTATION);
            }
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void show() {
        show(false);
    }

    public void show(boolean expand) {
        setVisibility(View.VISIBLE);
        menuButton.show();
        if (expand) expand();
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean collapse) {
        if (collapse) collapse();
        menuButton.hide();
        setVisibility(View.GONE);
    }

    public void setOverlayColor(@ColorInt int color) {
        final ViewParent parent = getParent();
        if (parent != null && parent instanceof FABsMenuLayout) {
            ((FABsMenuLayout) parent).setOverlayColor(color);
        }
    }

    public int getMenuMargins() {
        return menuMargins;
    }

    public void setMenuMargins(int menuMargins) {
        this.menuMargins = menuMargins;
        setMenuTopMargin(menuMargins);
        setMenuBottomMargin(menuMargins);
        setMenuLeftMargin(menuMargins);
        setMenuRightMargin(menuMargins);
        requestLayout();
    }

    public int getMenuTopMargin() {
        return menuTopMargin;
    }

    public void setMenuTopMargin(int menuTopMargin) {
        this.menuTopMargin = menuTopMargin;
        requestLayout();
    }

    public int getMenuBottomMargin() {
        return menuBottomMargin;
    }

    public void setMenuBottomMargin(int menuBottomMargin) {
        this.menuBottomMargin = menuBottomMargin;
        requestLayout();
    }

    public int getMenuRightMargin() {
        return menuRightMargin;
    }

    public void setMenuRightMargin(int menuRightMargin) {
        this.menuRightMargin = menuRightMargin;
        requestLayout();
    }

    public int getMenuLeftMargin() {
        return menuLeftMargin;
    }

    public void setMenuLeftMargin(int menuLeftMargin) {
        this.menuLeftMargin = menuLeftMargin;
        requestLayout();
    }

    @ColorInt
    public int getMenuButtonColor() {
        return menuButtonColor;
    }

    public void setMenuButtonColor(@ColorInt int menuButtonColor) {
        this.menuButton.setBackgroundColor(menuButtonColor);
        this.menuButtonColor = menuButtonColor;
    }

    @ColorInt
    public int getMenuButtonRippleColor() {
        return menuButtonRippleColor;
    }

    public void setMenuButtonRippleColor(@ColorInt int menuButtonRippleColor) {
        this.menuButton.setRippleColor(menuButtonRippleColor);
        this.menuButtonRippleColor = menuButtonRippleColor;
    }

    public int getMenuButtonSize() {
        return menuButtonSize;
    }

    public void setMenuButtonSize(@FloatingActionButton.Size int menuButtonSize) {
        this.menuButton.setSize(menuButtonSize);
        this.menuButtonSize = menuButtonSize;
        requestLayout();
    }

    public int getExpandDirection() {
        return expandDirection;
    }

    public void setExpandDirection(@EXPAND_DIRECTION int expandDirection) {
        this.expandDirection = expandDirection;
        requestLayout();
    }

    public MenuFAB getMenuButton() {
        return menuButton;
    }

    public void setMenuButton(@NonNull MenuFAB menuButton) {
        this.menuButton = menuButton;
        requestLayout();
    }

    public Drawable getMenuButtonIcon() {
        return menuButtonIcon;
    }

    public void setMenuButtonIcon(@NonNull Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            Drawable icon = Drawable.createFromStream(inputStream, uri.toString());
            if (icon != null) setMenuButtonIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMenuButtonIcon(@DrawableRes int resId) {
        try {
            Drawable icon = ContextCompat.getDrawable(getContext(), resId);
            if (icon != null) setMenuButtonIcon(icon);
            else throw new NullPointerException(
                    "The icon you try to assign to FABsMenu does not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMenuButtonIcon(@NonNull Drawable menuButtonIcon) {
        this.menuButton.setImageDrawable(menuButtonIcon);
        this.menuButtonIcon = menuButtonIcon;
        createRotatingDrawable();
    }

    public RotatingDrawable getRotatingDrawable() {
        return rotatingDrawable;
    }

    public int getButtonSpacing() {
        return buttonSpacing;
    }

    public void setButtonSpacing(int buttonSpacing) {
        this.buttonSpacing = buttonSpacing;
        requestLayout();
    }

    public int getLabelsMargin() {
        return labelsMargin;
    }

    public void setLabelsMargin(int labelsMargin) {
        this.labelsMargin = labelsMargin;
        requestLayout();
    }

    public int getLabelsPosition() {
        return labelsPosition;
    }

    public void setLabelsPosition(@LABELS_POSITION int labelsPosition) {
        this.labelsPosition = labelsPosition;
        requestLayout();
    }

    public int getButtonsCount() {
        return buttonsCount;
    }

    public int getMaxButtonWidth() {
        return maxButtonWidth;
    }

    public void setMaxButtonWidth(int maxButtonWidth) {
        this.maxButtonWidth = maxButtonWidth;
        requestLayout();
    }

    public int getMaxButtonHeight() {
        return maxButtonHeight;
    }

    public void setMaxButtonHeight(int maxButtonHeight) {
        this.maxButtonHeight = maxButtonHeight;
        requestLayout();
    }

    public FABsMenuListener getMenuListener() {
        return menuListener;
    }

    public void setMenuListener(FABsMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    /**
     * @param menuListener
     *         the menu listener
     *
     * @deprecated Use {@link #setMenuListener(FABsMenuListener)} instead
     */
    @Deprecated
    public void setMenuUpdateListener(FABsMenuListener menuListener) {
        setMenuListener(menuListener);
    }

    public void setMenuButtonIcon(@NonNull Bitmap bitmap) {
        try {
            setMenuButtonIcon(new BitmapDrawable(getResources(), bitmap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLabelsVerticalOffset(int labelsVerticalOffset) {
        this.labelsVerticalOffset = labelsVerticalOffset;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        final ViewParent parent = getParent();
        if (parent != null && parent instanceof FABsMenuLayout) {
            ((FABsMenuLayout) parent).setAnimationDuration(animationDuration);
        }
        this.animationDuration = animationDuration;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EXPAND_UP, EXPAND_DOWN, EXPAND_LEFT, EXPAND_RIGHT})
    public @interface EXPAND_DIRECTION {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LABELS_ON_LEFT_SIDE, LABELS_ON_RIGHT_SIDE})
    public @interface LABELS_POSITION {
    }

    private static class RotatingDrawable extends LayerDrawable {
        private float mRotation;

        public RotatingDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public boolean expanded;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            expanded = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(expanded ? 1 : 0);
        }
    }

    private class LayoutParams extends ViewGroup.LayoutParams {

        private ObjectAnimator mExpandDir = new ObjectAnimator();
        private ObjectAnimator mExpandAlpha = new ObjectAnimator();
        private ObjectAnimator mCollapseDir = new ObjectAnimator();
        private ObjectAnimator mCollapseAlpha = new ObjectAnimator();
        private boolean animationsSetToPlay;

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);

            mExpandDir.setInterpolator(expandInterpolator);
            mExpandAlpha.setInterpolator(alphaExpandInterpolator);
            mCollapseDir.setInterpolator(collapseInterpolator);
            mCollapseAlpha.setInterpolator(collapseInterpolator);

            mCollapseAlpha.setProperty(View.ALPHA);
            mCollapseAlpha.setFloatValues(1f, 0f);

            mExpandAlpha.setProperty(View.ALPHA);
            mExpandAlpha.setFloatValues(0f, 1f);

            switch (expandDirection) {
                case EXPAND_UP:
                case EXPAND_DOWN:
                    mCollapseDir.setProperty(View.TRANSLATION_Y);
                    mExpandDir.setProperty(View.TRANSLATION_Y);
                    break;
                case EXPAND_LEFT:
                case EXPAND_RIGHT:
                    mCollapseDir.setProperty(View.TRANSLATION_X);
                    mExpandDir.setProperty(View.TRANSLATION_X);
                    break;
                default: // Do Nothing
                    break;
            }
        }

        public void setAnimationsTarget(View view) {
            mCollapseAlpha.setTarget(view);
            mCollapseDir.setTarget(view);
            mExpandAlpha.setTarget(view);
            mExpandDir.setTarget(view);

            // Now that the animations have targets, set them to be played
            if (!animationsSetToPlay) {
                addLayerTypeListener(mExpandDir, view);
                addLayerTypeListener(mCollapseDir, view);

                collapseAnimation.play(mCollapseAlpha);
                collapseAnimation.play(mCollapseDir);
                expandAnimation.play(mExpandAlpha);
                expandAnimation.play(mExpandDir);
                animationsSetToPlay = true;
            }
        }

        private void addLayerTypeListener(Animator animator, final View view) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setLayerType(LAYER_TYPE_NONE, null);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setLayerType(LAYER_TYPE_HARDWARE, null);
                }
            });
        }
    }
}