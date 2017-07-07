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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

public class FABsMenu extends ViewGroup {
    public static final int EXPAND_UP = 0;
    public static final int EXPAND_DOWN = 1;
    public static final int EXPAND_LEFT = 2;
    public static final int EXPAND_RIGHT = 3;

    public static final int LABELS_ON_LEFT_SIDE = 0;
    public static final int LABELS_ON_RIGHT_SIDE = 1;

    private static final int ANIMATION_DURATION = 500;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;
    private static Interpolator sExpandInterpolator = new OvershootInterpolator();
    private static Interpolator sCollapseInterpolator = new DecelerateInterpolator(3f);
    private static Interpolator sAlphaExpandInterpolator = new DecelerateInterpolator();
    private Drawable mMenuButtonPlusIcon;
    private int mMenuMargins;
    private int mMenuButtonColorNormal;
    private int mMenuButtonColorPressed;
    private int mMenuButtonSize;
    private int mExpandDirection;
    private int mButtonSpacing;
    private int mLabelsMargin;
    private int mLabelTextPadding;
    private int mLabelsVerticalOffset;
    private boolean mExpanded;
    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private MenuFAB mMenuButton;
    private RotatingDrawable mRotatingDrawable;
    private int mMaxButtonWidth;
    private int mMaxButtonHeight;
    private int mLabelsPosition;
    private int mButtonsCount;
    private TouchDelegateGroup mTouchDelegateGroup;
    private OnFABsMenuUpdateListener mListener;

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
        mButtonSpacing = (int) convertDpToPixel(16, context);
        mLabelsMargin = getResources().getDimensionPixelSize(R.dimen.fab_labels_margin);
        mLabelsVerticalOffset = (int) convertDpToPixel(-1.5f, context);

        mTouchDelegateGroup = new TouchDelegateGroup(this);
        setTouchDelegate(mTouchDelegateGroup);

        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.FABsMenu,
                0, 0);

        mMenuMargins = attr.getDimensionPixelSize(R.styleable.FABsMenu_fab_menuMargins, (int)
                convertDpToPixel(16, context));

        mMenuButtonPlusIcon = attr.getDrawable(R.styleable.FABsMenu_fab_moreButtonPlusIcon);

        mMenuButtonColorNormal = attr.getColor(R.styleable.FABsMenu_fab_moreButtonBackgroundColor,
                getColor(android.R.color.holo_blue_dark));
        mMenuButtonColorPressed = attr.getColor(R.styleable.FABsMenu_fab_moreButtonRippleColor,
                getColor(android.R.color.holo_blue_light));

        mMenuButtonSize = attr.getInt(R.styleable.FABsMenu_fab_moreButtonSize, TitleFAB
                .SIZE_NORMAL);

        mExpandDirection = attr.getInt(R.styleable.FABsMenu_fab_expandDirection, EXPAND_UP);

        mLabelsPosition = attr.getInt(R.styleable.FABsMenu_fab_labelsPosition, LABELS_ON_LEFT_SIDE);

        mLabelTextPadding = attr.getDimensionPixelSize(R.styleable.FABsMenu_fab_labelTextPadding,
                (int) convertDpToPixel(8, context));

        attr.recycle();

        createAddButton(context);
    }

    public void setOnFABsMenuUpdateListener(OnFABsMenuUpdateListener
                                                    listener) {
        mListener = listener;
    }

    private boolean expandsHorizontally() {
        return mExpandDirection == EXPAND_LEFT || mExpandDirection == EXPAND_RIGHT;
    }

    private void createAddButton(Context context) {
        mMenuButton = new MenuFAB(context);

        if (mMenuButtonPlusIcon != null) {
            RotatingDrawable rotatingDrawable = new RotatingDrawable(mMenuButtonPlusIcon);
            mRotatingDrawable = rotatingDrawable;

            final OvershootInterpolator interpolator = new OvershootInterpolator();

            final ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(rotatingDrawable,
                    "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
            final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(rotatingDrawable,
                    "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);

            collapseAnimator.setInterpolator(interpolator);
            expandAnimator.setInterpolator(interpolator);

            mExpandAnimation.play(expandAnimator);
            mCollapseAnimation.play(collapseAnimator);

            mMenuButton.setImageDrawable(rotatingDrawable);
        }

        mMenuButton.setBackgroundTintList(ColorStateList.valueOf(mMenuButtonColorNormal));
        mMenuButton.setRippleColor(mMenuButtonColorPressed);

        mMenuButton.setId(R.id.fab_expand_menu_button);
        mMenuButton.setSize(mMenuButtonSize);
        mMenuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onMenuClicked();
                toggle();
            }
        });

        addView(mMenuButton, super.generateDefaultLayoutParams());
        mButtonsCount++;
    }

    public void addButton(TitleFAB button) {
        if (mButtonsCount >= 6)
            throw new IllegalArgumentException("A floating action buttons menu should have no " +
                    "more than six options.");
        addView(button, mButtonsCount - 1);
        mButtonsCount++;
        createLabels();
        if (mButtonsCount < 3)
            Log.w("FABsMenu", "A floating action buttons menu should have at least three options");
    }

    public void removeButton(TitleFAB button) {
        if (mButtonsCount <= 0) return;
        try {
            removeView(button.getLabelView());
            removeView(button);
            button.setTag(R.id.fab_label, null);
            mButtonsCount--;
        } catch (Exception ignored) {
        }
    }

    private int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = mButtonSpacing;
        int height = mButtonSpacing;

        mMaxButtonWidth = 0;
        mMaxButtonHeight = 0;
        int maxLabelWidth = 0;

        for (int i = 0; i < mButtonsCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            switch (mExpandDirection) {
                case EXPAND_UP:
                case EXPAND_DOWN:
                    mMaxButtonWidth = Math.max(mMaxButtonWidth, child.getMeasuredWidth());
                    height += child.getMeasuredHeight();
                    break;
                case EXPAND_LEFT:
                case EXPAND_RIGHT:
                    width += child.getMeasuredWidth();
                    mMaxButtonHeight = Math.max(mMaxButtonHeight, child.getMeasuredHeight());
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
            width = mMaxButtonWidth + (maxLabelWidth > 0 ? maxLabelWidth + mLabelsMargin : 0);
        } else {
            height = mMaxButtonHeight;
        }

        switch (mExpandDirection) {
            case EXPAND_UP:
            case EXPAND_DOWN:
                height += mButtonSpacing * (mButtonsCount - 1);
                height = adjustForOvershoot(height);
                break;
            case EXPAND_LEFT:
            case EXPAND_RIGHT:
                width += mButtonSpacing * (mButtonsCount - 1);
                width = adjustForOvershoot(width);
                break;
        }

        setMeasuredDimension((int) (width + (mMenuMargins * 1.5)), height);
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private int adjustForOvershoot(int dimension) {
        return dimension * 12 / 10;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (mExpandDirection) {
            case EXPAND_UP:
            case EXPAND_DOWN:
                boolean expandUp = mExpandDirection == EXPAND_UP;

                mTouchDelegateGroup.clearTouchDelegates();

                int addButtonY = expandUp ? b - t - mMenuButton.getMeasuredHeight() : 0;
                addButtonY -= mMenuMargins;

                // Ensure mMenuButton is centered on the line where the buttons should be
                int buttonsHorizontalCenter = mLabelsPosition == LABELS_ON_LEFT_SIDE
                        ? r - l - mMaxButtonWidth / 2
                        : mMaxButtonWidth / 2;
                buttonsHorizontalCenter -= mMenuMargins;
                int addButtonLeft = buttonsHorizontalCenter - mMenuButton.getMeasuredWidth() / 2;
                mMenuButton.layout(addButtonLeft, addButtonY,
                        addButtonLeft + mMenuButton.getMeasuredWidth(),
                        addButtonY + mMenuButton.getMeasuredHeight());

                int labelsOffset = mMaxButtonWidth / 2 + mLabelsMargin;
                int labelsXNearButton = mLabelsPosition == LABELS_ON_LEFT_SIDE
                        ? buttonsHorizontalCenter - labelsOffset
                        : buttonsHorizontalCenter + labelsOffset;

                int nextY = expandUp ?
                        addButtonY - mButtonSpacing :
                        addButtonY + mMenuButton.getMeasuredHeight() + mButtonSpacing;

                for (int i = mButtonsCount - 1; i >= 0; i--) {
                    final View child = getChildAt(i);

                    if (child == mMenuButton || child.getVisibility() == GONE) continue;

                    int childX = buttonsHorizontalCenter - child.getMeasuredWidth() / 2;
                    int childY = expandUp ? nextY - child.getMeasuredHeight() : nextY;
                    child.layout(childX, childY, childX + child.getMeasuredWidth(), childY +
                            child.getMeasuredHeight());

                    float collapsedTranslation = addButtonY - childY;
                    float expandedTranslation = 0f;

                    child.setTranslationY(mExpanded ? expandedTranslation : collapsedTranslation);
                    child.setAlpha(mExpanded ? 1f : 0f);

                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
                    params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
                    params.setAnimationsTarget(child);

                    View label = (View) child.getTag(R.id.fab_label);
                    if (label != null) {
                        int labelXAwayFromButton = mLabelsPosition == LABELS_ON_LEFT_SIDE
                                ? labelsXNearButton - label.getMeasuredWidth()
                                : labelsXNearButton + label.getMeasuredWidth();

                        int labelLeft = mLabelsPosition == LABELS_ON_LEFT_SIDE
                                ? labelXAwayFromButton
                                : labelsXNearButton;

                        int labelRight = mLabelsPosition == LABELS_ON_LEFT_SIDE
                                ? labelsXNearButton
                                : labelXAwayFromButton;

                        int labelTop = childY - mLabelsVerticalOffset +
                                (child.getMeasuredHeight() - label.getMeasuredHeight()) / 2;

                        label.layout(labelLeft, labelTop, labelRight, labelTop +
                                label.getMeasuredHeight());

                        Rect touchArea = new Rect(
                                Math.min(childX, labelLeft),
                                childY - mButtonSpacing / 2,
                                Math.max(childX + child.getMeasuredWidth(), labelRight),
                                childY + child.getMeasuredHeight() + mButtonSpacing / 2);
                        mTouchDelegateGroup.addTouchDelegate(new TouchDelegate(touchArea, child));

                        label.setTranslationY(mExpanded ? expandedTranslation :
                                collapsedTranslation);
                        label.setAlpha(mExpanded ? 1f : 0f);

                        LayoutParams labelParams = (LayoutParams) label.getLayoutParams();
                        labelParams.mCollapseDir.setFloatValues(expandedTranslation,
                                collapsedTranslation);
                        labelParams.mExpandDir.setFloatValues(collapsedTranslation,
                                expandedTranslation);
                        labelParams.setAnimationsTarget(label);
                    }

                    nextY = expandUp ?
                            childY - mButtonSpacing :
                            childY + child.getMeasuredHeight() + mButtonSpacing;
                }
                break;

            case EXPAND_LEFT:
            case EXPAND_RIGHT:
                boolean expandLeft = mExpandDirection == EXPAND_LEFT;

                int addButtonX = expandLeft ? r - l - mMenuButton.getMeasuredWidth() : 0;
                // Ensure mMenuButton is centered on the line where the buttons should be
                int addButtonTop = b - t - mMaxButtonHeight + (mMaxButtonHeight - mMenuButton
                        .getMeasuredHeight()) / 2;
                mMenuButton.layout(addButtonX, addButtonTop, addButtonX + mMenuButton
                        .getMeasuredWidth(), addButtonTop + mMenuButton.getMeasuredHeight());

                int nextX = expandLeft ?
                        addButtonX - mButtonSpacing :
                        addButtonX + mMenuButton.getMeasuredWidth() + mButtonSpacing;

                for (int i = mButtonsCount - 1; i >= 0; i--) {
                    final View child = getChildAt(i);

                    if (child == mMenuButton || child.getVisibility() == GONE) continue;

                    int childX = expandLeft ? nextX - child.getMeasuredWidth() : nextX;
                    int childY = addButtonTop + (mMenuButton.getMeasuredHeight() - child
                            .getMeasuredHeight()) / 2;
                    child.layout(childX, childY, childX + child.getMeasuredWidth(), childY +
                            child.getMeasuredHeight());

                    float collapsedTranslation = addButtonX - childX;
                    float expandedTranslation = 0f;

                    child.setTranslationX(mExpanded ? expandedTranslation : collapsedTranslation);
                    child.setAlpha(mExpanded ? 1f : 0f);

                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
                    params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
                    params.setAnimationsTarget(child);

                    nextX = expandLeft ?
                            childX - mButtonSpacing :
                            childX + child.getMeasuredWidth() + mButtonSpacing;
                }
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

        bringChildToFront(mMenuButton);
        mButtonsCount = getChildCount();

        createLabels();
    }

    private void createLabels() {
        if (!expandsHorizontally()) {
            for (int i = 0; i < mButtonsCount; i++) {
                final TitleFAB button = (TitleFAB) getChildAt(i);
                String title = button.getTitle();

                if (button == mMenuButton || title == null || title.length() <= 0 ||
                        button.getTag(R.id.fab_label) != null) continue;

                final LabelView label = new LabelView(getContext(),
                        button.getTitleBackgroundColor());
                label.setId(i + 1);

                if (button.getTitleCornerRadius() != -1)
                    label.setRadius(button.getTitleCornerRadius());

                final TextView labelText = new TextView(getContext());
                labelText.setText(button.getTitle());
                labelText.setTextColor(button.getTitleTextColor());
                labelText.setPadding(mLabelTextPadding, mLabelTextPadding / 2, mLabelTextPadding,
                        mLabelTextPadding / 2);

                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                        outValue, true);
                label.setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));

                if (button.isTitleClickEnabled()) {
                    label.setClickable(true);
                    label.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (button.getClickListener() != null)
                                button.getClickListener().onClick(label);
                        }
                    });
                }

                label.addView(labelText);
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
        if (parent != null) {
            if (parent instanceof FABsMenuLayout) {
                final View overlay = ((FABsMenuLayout) parent).getOverlayView();
                if (show) {
                    overlay.setAlpha(0);
                    overlay.setVisibility(VISIBLE);
                }
                overlay.animate().alpha(show ? 1f : 0f)
                        .setDuration(immediately ? 0 : ANIMATION_DURATION).setListener(
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (!show) {
                                    overlay.setVisibility(GONE);
                                    overlay.setOnClickListener(null);
                                } else {
                                    if (((FABsMenuLayout) parent).hasClickableOverlay())
                                        overlay.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                collapse();
                                            }
                                        });
                                }
                            }
                        }).start();
            }
        }
    }

    public void collapse() {
        collapse(false);
    }

    public void collapseImmediately() {
        collapse(true);
    }

    private void collapse(boolean immediately) {
        if (mExpanded) {
            mExpanded = false;
            mTouchDelegateGroup.setEnabled(false);
            toggleOverlay(false, immediately);
            mCollapseAnimation.setDuration(immediately ? 0 : ANIMATION_DURATION);
            mCollapseAnimation.start();
            mExpandAnimation.cancel();
            if (mListener != null) {
                mListener.onMenuCollapsed();
            }
        }
    }

    public void toggle() {
        if (mExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (!mExpanded) {
            mExpanded = true;
            mTouchDelegateGroup.setEnabled(true);
            toggleOverlay(true, false);
            mCollapseAnimation.cancel();
            mExpandAnimation.start();
            if (mListener != null) {
                mListener.onMenuExpanded();
            }
        }
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mMenuButton.setEnabled(enabled);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mExpanded = mExpanded;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mExpanded = savedState.mExpanded;
            mTouchDelegateGroup.setEnabled(mExpanded);
            if (mRotatingDrawable != null) {
                mRotatingDrawable.setRotation(mExpanded ? EXPANDED_PLUS_ROTATION :
                        COLLAPSED_PLUS_ROTATION);
            }
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public interface OnFABsMenuUpdateListener {
        void onMenuClicked();

        void onMenuExpanded();

        void onMenuCollapsed();
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
        public boolean mExpanded;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mExpanded = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mExpanded ? 1 : 0);
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

            mExpandDir.setInterpolator(sExpandInterpolator);
            mExpandAlpha.setInterpolator(sAlphaExpandInterpolator);
            mCollapseDir.setInterpolator(sCollapseInterpolator);
            mCollapseAlpha.setInterpolator(sCollapseInterpolator);

            mCollapseAlpha.setProperty(View.ALPHA);
            mCollapseAlpha.setFloatValues(1f, 0f);

            mExpandAlpha.setProperty(View.ALPHA);
            mExpandAlpha.setFloatValues(0f, 1f);

            switch (mExpandDirection) {
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

                mCollapseAnimation.play(mCollapseAlpha);
                mCollapseAnimation.play(mCollapseDir);
                mExpandAnimation.play(mExpandAlpha);
                mExpandAnimation.play(mExpandDir);
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


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into
     *                pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public TitleFAB getMenuButton() {
        return mMenuButton;
    }
}