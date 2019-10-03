<img src="https://www.jahirfiquitiva.com/assets/images/projects/android/fabsmenu.png" width="192" align="right" hspace="20" />

FloatingActionButtons Menu (FABsMenu)
======

![API](https://img.shields.io/badge/API-14%2B-34bf49.svg)
[![JitPack Badge](https://jitpack.io/v/jahirfiquitiva/FABsMenu.svg)](https://jitpack.io/#jahirfiquitiva/FABsMenu)
[![Build Status](https://travis-ci.org/jahirfiquitiva/FABsMenu.svg?branch=master)](https://travis-ci.org/jahirfiquitiva/FABsMenu)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3ab596acdd5648599c34d56dba1eea39)](https://www.codacy.com/app/jahirfiquitiva/FABsMenu?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jahirfiquitiva/FABsMenu&amp;utm_campaign=Badge_Grade)

Just a simple library to use a menu of `FloatingActionButton`s from Design Support Library that follows [Material Design guidelines](https://material.io/guidelines/components/buttons-floating-action-button.html#buttons-floating-action-button-transitions)

<a target="_blank" href="http://bit.ly/DLFABsMenuAPK">
<img src="http://www.jahirfiquitiva.com/share/download_sample.svg?maxAge=432000" width="200"/>
</a>

<a target="_blank" href="http://www.jahirfiquitiva.com/support/">
<img src="http://www.jahirfiquitiva.com/share/support_my_work.svg?maxAge=432000" width="200"/>
</a>

## Show some  :blue_heart:
[![GitHub stars](https://img.shields.io/github/stars/jahirfiquitiva/FABsMenu.svg?style=social&label=Star)](https://github.com/jahirfiquitiva/FABsMenu)
[![GitHub forks](https://img.shields.io/github/forks/jahirfiquitiva/FABsMenu.svg?style=social&label=Fork)](https://github.com/jahirfiquitiva/FABsMenu/fork)
[![GitHub watchers](https://img.shields.io/github/watchers/jahirfiquitiva/FABsMenu.svg?style=social&label=Watch)](https://github.com/jahirfiquitiva/FABsMenu)

[![Follow on GitHub](https://img.shields.io/github/followers/jahirfiquitiva.svg?style=social&label=Follow)](https://github.com/jahirfiquitiva)
[![Twitter Follow](https://img.shields.io/twitter/follow/jahirfiquitiva.svg?style=social)](https://twitter.com/jahirfiquitiva)

---

### Long story

Android Support/Design libraries still doesn't include a FloatingActionButtons menu, and the libraries found here and there are either too cluttered or filled with unnecessary stuff or using custom views that simply don't follow guidelines, which made me avoid them.

I was looking for a new library, and the ones I knew of, have been deprecated/abandoned because the simple FABs are included in the design support library, so I felt the urge of having something that could suffice my needs, and couldn't help but create a library, although is mostly based on [one of those abandoned libraries](https://github.com/jahirfiquitiva/FABsMenu#credits).

This library uses the design library FABs but wraps them inside a ViewGroup to make them look like the FloatingActionButtons menu suggested in Material Design guidelines.


---

## Deprecated

Unfortunately, due to my limited time between work+University, I don't really find the way to provide proper support for this library. I really appreciate everyone that has been using it and that might continue using it, but, at least for now, this library is deprecated.

If you want to use a similar and more frequently updated library, please refer to @leinardi 's [FloatingActionSpeedDial](https://github.com/leinardi/FloatingActionButtonSpeedDial)

Thanks again for the amazing support. :raised_hands:

---

# Preview

![FABs Menu Preview](https://github.com/jahirfiquitiva/FABsMenu/raw/master/preview/preview.gif)

## Changelog
:radio_button: You can find it in the [Releases page](https://github.com/jahirfiquitiva/FABsMenu/releases)

---

# Including in your project
FABsMenu is available via JitPack, so getting it as simple as adding it as a dependency, like this:

1. Add JitPack repository to your root `build.gradle` file
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency in your project `build.gradle` file
```gradle
dependencies {
    implementation 'me.jahirfiquitiva:FABsMenu:{latest version}'
}
```
where `{latest version}` corresponds to published version in   [![JitPack](https://jitpack.io/v/jahirfiquitiva/FABsMenu.svg)](https://jitpack.io/#jahirfiquitiva/FABsMenu)

## How to implement
You can use it in your layout, just like this:

```xml
<RelativeLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:fab="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <!-- Menu Layout is needed for the overlay to work -->
    <jahirfiquitiva.libs.fabsmenu.FABsMenuLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            fab:fabs_menu_overlayColor="#4d000000"
            fab:fabs_menu_clickableOverlay="true"
            tools:layout_behavior="@string/fabs_menu_layout_behavior">

        <!-- FABs Menu is the main view. It will contain all the items FABs and it create the menu fab itself -->
        <jahirfiquitiva.libs.fabsmenu.FABsMenu
                android:id="@+id/fabs_menu"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="bottom|end"
                android:clipChildren="false"
                fab:fab_menuMargins="16dp"
                fab:fab_moreButtonPlusIcon="@drawable/ic_plus"
                fab:fab_moreButtonBackgroundColor="@color/pink"
                fab:fab_moreButtonRippleColor="@color/pink_pressed"
                fab:fab_moreButtonSize="normal"
                fab:fab_labelsPosition="left"
                fab:fab_expandDirection="up">

            <!-- This is the item that appears at the top of the menu -->
            <jahirfiquitiva.libs.fabsmenu.TitleFAB
                    android:id="@+id/to_remove"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    fab:srcCompat="@drawable/ic_share"
                    fab:fabSize="mini"
                    fab:backgroundTint="@color/colorAccent"
                    fab:rippleColor="@color/colorAccent"
                    fab:fab_title="This is a custom title"
                    fab:fab_title_backgroundColor="@color/colorAccent"
                    fab:fab_title_textColor="@android:color/white"/>

            <jahirfiquitiva.libs.fabsmenu.TitleFAB
                    android:id="@+id/clickable_title"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    fab:srcCompat="@drawable/ic_pencil"
                    fab:fab_title="Clickable title"
                    fab:fab_enableTitleClick="true"
                    fab:fab_title_textColor="@color/colorAccent"
                    fab:fabSize="mini"
                    fab:backgroundTint="@color/colorAccent"
                    fab:rippleColor="@color/colorAccent"/>

            <jahirfiquitiva.libs.fabsmenu.TitleFAB
                    android:id="@+id/mini_fab"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    fab:srcCompat="@drawable/ic_heart"
                    fab:fab_title="Mini fab with long text"
                    fab:fabSize="mini"
                    fab:backgroundTint="@color/blue_semi"
                    fab:rippleColor="@color/blue_semi_pressed"/>

            <!-- This item will appear just above the menu FAB -->
            <jahirfiquitiva.libs.fabsmenu.TitleFAB
                    android:id="@+id/green_fab"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    fab:srcCompat="@drawable/ic_person"
                    fab:fab_title="Fab with even longer text that might not even fit in all the screens"
                    fab:fabSize="normal"
                    fab:backgroundTint="@color/green"
                    fab:rippleColor="@color/green_pressed"/>

        </jahirfiquitiva.libs.fabsmenu.FABsMenu>

    </jahirfiquitiva.libs.fabsmenu.FABsMenuLayout>

</RelativeLayout>
```

And call the methods in java code:
```java
final FABsMenu menu = findViewById(R.id.fabs_menu);
menu.setMenuUpdateListener(new FABsMenuListener() {
    // You don't need to override all methods. Just the ones you want.

    @Override
    public void onMenuClicked(FABsMenu fabsMenu) {
        super.onMenuClicked(fabsMenu); // Default implementation opens the menu on click
        showToast("You pressed the menu!");
    }

    @Override
    public void onMenuCollapsed(FABsMenu fabsMenu) {
        super.onMenuCollapsed(fabsMenu);
        showToast("The menu has been collapsed!");
    }

    @Override
    public void onMenuExpanded(FABsMenu fabsMenu) {
        super.onMenuExpanded(fabsMenu);
        showToast("The menu has been expanded!");
    }
});
```

You can also add or remove buttons programmatically:
```java
// Removes a button
TitleFAB toRemove = findViewById(R.id.to_remove);
menu.removeButton(toRemove);

// Adds a button to the bottom
TitleFAB toAdd = new TitleFAB(this);
toAdd.setTitle("A new added fab");
toAdd.setBackgroundColor(Color.parseColor("#ff5722"));
menu.addButton(toAdd);
```

And you can also attach the `FABsMenu` to a `RecyclerView` so it gets hidden on scroll, like this:
```java
RecyclerView rv = findViewById(R.id.rv);
FABsMenu menu = findViewById(R.id.fabs_menu);
menu.attachToRecyclerView(rv);
```


## Attributes explanation

1. FABsMenuLayout attributes:
	* `fabs_menu_overlayColor` --> Set the menu overlay color (Defaults to `#4d000000`)
	* `fabs_menu_clickableOverlay` --> Specify whether the overlay is clickable or not (Defaults to `true`)

2. FABsMenu attributes
	* `fab_menuMargins` --> The margins of the menu (Defaults to `16dp`)
	* `fab_menuTopMargin` --> The top margin of the menu (Defaults to `fab_menuMargins`)
	* `fab_menuBottomMargin` --> The bottom margin of the menu (Defaults to `fab_menuMargins`)
	* `fab_menuRightMargin` --> The right margin of the menu (Defaults to `fab_menuMargins`)
	* `fab_menuLeftMargin` --> The left margin of the menu (Defaults to `fab_menuMargins`)
	* `fab_moreButtonRippleColor` --> The menu fab ripples color
	* `fab_moreButtonBackgroundColor` --> The menu fab background color
	* `fab_moreButtonSize` --> Specify the size. Choose between `mini` and `normal` (Defaults to `normal`)
	* `fab_moreButtonPlusIcon` --> The reference to the plus icon drawable (Defaults to `null`)
	* `fab_labelsPosition` --> Where to show the labels. Choose between `left` and `right` (Defaults to `left`)
	* `fab_expandDirection` --> The direction the menu should expand to. Choose between `up`, `down`, `left` and `right`. (Defaults to `up`)
	
3. TitleFAB attributes:
	* `fab_title` --> A string that will be shown as the fab label (Defaults to `null`)
	* `fab_title_backgroundColor` --> The color of the label background (Defaults to `#fff`)
	* `fab_title_textColor` --> The color of the label text (Defaults to `#000`)
	* `fab_title_cornerRadius` --> The dimension of the label corners radius (Defaults to `2dp`)
	* `fab_title_textPadding` --> The dimension of the text padding (Defaults to `8dp`)
	* `fab_enableTitleClick` --> Specify whether the label click should fire the fab click too (Defaults to `false`)


## Important notes

1. The FABs are based on the ones from Design Support libraries, so you can use these customization attributes:
	```xml
		fab:fabSize="mini"
		fab:backgroundTint="@color/blue_semi"
		fab:rippleColor="@color/blue_semi_pressed"
	```
	
2. For the FABsMenu, the previous attributes will not work.
3. For the FABsMenu, use `fab_menuMargins` instead of the normal `android:layout_margin` attribute. This will prevent FABs elevation being cropped.
4. As stated in guidelines, a FABsMenu should not have more than 6 items. If you use more than 6, you will get an `IllegalArgumentException`. Also, remember FABs menu should have **at least** 3 items too.
5. For now, the icon you set for FABsMenu will always rotate, so be sure you set an icon that looks good in both states (normal and rotated).

---

## Apps using FABsMenu

* Yours? Fill [an issue](https://github.com/jahirfiquitiva/FABsMenu/issues/new) and let me know ;)

---

# Developed by

### [Jahir Fiquitiva](https://www.jahirfiquitiva.com/)

[![Follow on GitHub](https://img.shields.io/github/followers/jahirfiquitiva.svg?style=social&label=Follow)](https://github.com/jahirfiquitiva)
[![Twitter Follow](https://img.shields.io/twitter/follow/jahirfiquitiva.svg?style=social)](https://twitter.com/jahirfiquitiva)

If you found this app/library helpful and want to thank me, you can:

<a target="_blank" href="http://www.jahirfiquitiva.com/support/">
<img src="http://www.jahirfiquitiva.com/share/support_my_work.svg?maxAge=432000" width="200"/>
</a>

**Thanks in advance!** :pray:

---

# Credits

This project is based on Future Simple's [android-floating-action-button](https://github.com/futuresimple/android-floating-action-button) project, which is also based on [FloatingActionButton](https://github.com/makovkastar/FloatingActionButton) library by [Oleksandr Melnykov](https://github.com/makovkastar).


# License


    Copyright (c) 2018 Jahir Fiquitiva

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


# Original license
	

    Copyright (c) 2014 Jerzy Chalupski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
