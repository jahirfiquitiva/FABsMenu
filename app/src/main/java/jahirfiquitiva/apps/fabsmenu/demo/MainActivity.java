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

package jahirfiquitiva.apps.fabsmenu.demo;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.pink_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked pink Floating Action Button",
                        Toast.LENGTH_SHORT).show();
            }
        });

        TitleFAB button = findViewById(R.id.setter);
        button.setSize(TitleFAB.SIZE_MINI);
        button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.pink)));
        button.setRippleColor(ContextCompat.getColor(this, R.color.pink_pressed));
        button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fab_star));

        final View actionB = findViewById(R.id.action_b);

        TitleFAB actionC = new TitleFAB(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View
                        .GONE);
            }
        });

        final FABsMenu menuMultipleActions = findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);

        final TitleFAB removeAction = findViewById(R.id.button_remove);
        removeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FABsMenu) findViewById(R.id.multiple_actions_down)).removeButton(removeAction);
            }
        });

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(ContextCompat.getColor(this, R.color.white));
        ((TitleFAB) findViewById(R.id.setter_drawable)).setImageDrawable(drawable);

        final TitleFAB actionA = findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Action A clicked");
            }
        });

        // Test that FAMs containing FABs with visibility GONE do not cause crashes
        findViewById(R.id.button_gone).setVisibility(View.GONE);

        final TitleFAB actionEnable = findViewById(R.id.action_enable);
        actionEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuMultipleActions.setEnabled(!menuMultipleActions.isEnabled());
            }
        });

        FABsMenu rightLabels = findViewById(R.id.right_labels);
        TitleFAB addedOnce = new TitleFAB(this);
        addedOnce.setTitle("Added once");
        rightLabels.addButton(addedOnce);

        TitleFAB addedTwice = new TitleFAB(this);
        addedTwice.setTitle("Added twice");
        rightLabels.addButton(addedTwice);
        rightLabels.removeButton(addedTwice);
        rightLabels.addButton(addedTwice);
    }
}