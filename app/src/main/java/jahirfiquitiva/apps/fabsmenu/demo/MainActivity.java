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

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        
        setContentView(R.layout.activity_main);
        
        Button snackButton = findViewById(R.id.snack_button);
        snackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(R.id.coordinator), "Testing, testing, 1, 2, 3",
                              Snackbar.LENGTH_LONG).show();
            }
        });
        
        final FABsMenu menu = findViewById(R.id.fabs_menu);
        menu.setMenuListener(new FABsMenuListener() {
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
        
        // Removes a button
        TitleFAB toRemove = findViewById(R.id.to_remove);
        menu.removeButton(toRemove);
        
        // Adds a button to the bottom
        TitleFAB toAdd = new TitleFAB(this);
        toAdd.setTitle("A new added fab");
        toAdd.setBackgroundColor(Color.parseColor("#ff5722"));
        menu.addButton(toAdd);
    }
    
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}