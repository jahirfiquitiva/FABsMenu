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
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FABsMenu menu = findViewById(R.id.fabs_menu);
        menu.setOnFABsMenuUpdateListener(new FABsMenu.OnFABsMenuUpdateListener() {
            @Override
            public void onMenuClicked() {
                showToast("You pressed the menu!");
            }

            @Override
            public void onMenuExpanded() {
                showToast("The menu has been expanded!");
            }

            @Override
            public void onMenuCollapsed() {
                showToast("The menu has been collapsed!");
            }
        });

        TitleFAB clickableTitle = findViewById(R.id.clickable_title);
        clickableTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You pressed the red fab or its title");
            }
        });

        TitleFAB mini = findViewById(R.id.mini_fab);
        mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You pressed the mini fab!");
            }
        });

        TitleFAB green = findViewById(R.id.green_fab);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("You pressed the green fab");
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}