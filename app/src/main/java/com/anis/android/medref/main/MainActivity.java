package com.anis.android.medref.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.anis.android.medref.AboutActivity;
import com.anis.android.medref.R;
import com.anis.android.medref.SettingsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private MaterialToolbar toolbar;
    private MainPagerAdapter adapter;
    private MenuItem searchItem;
    private final String[] titles = {"Quick Reference", "Subjects"};
    private int[] tabIcons = {
            R.drawable.prescription_svgrepo_com,
            R.drawable.ic_books
    };
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        adapter = new MainPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(titles[position]); // Set the title
            tab.setIcon(tabIcons[position]);  // Set the icon
        }).attach();


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position >= 0 && position < titles.length) {
                    toolbar.setTitle(titles[position]);
                }

                // Hide/show search based on fragment type
                Fragment fragment = adapter.getFragment(position);
                if (searchItem != null) {
                    searchItem.setVisible(fragment instanceof SearchableFragment);
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(viewPager.getCurrentItem() > 0){
                    viewPager.setCurrentItem(0);
                } else {
                    finish();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);


    }
    private void findViews() {
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tab_layout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search); // Assign it here

        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    sendQueryToFragment(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    sendQueryToFragment(newText);
                    return false;
                }
            });

            searchView.setQueryHint("Find in document");

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    for (int i = 0; i < menu.size(); i++) {
                        MenuItem menuItem = menu.getItem(i);
                        if (menuItem.getItemId() != R.id.action_search) {
                            menuItem.setVisible(false);
                        }
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    supportInvalidateOptionsMenu();
                    return true;
                }
            });
        }

        // Initial show/hide when menu is first created
        Fragment currentFragment = adapter.getFragment(viewPager.getCurrentItem());
        searchItem.setVisible(currentFragment instanceof SearchableFragment);

        return true;
    }


    private void sendQueryToFragment(String query) {
        int position = viewPager.getCurrentItem();
        Fragment fragment = adapter.getFragment(position);

        if (fragment instanceof SearchableFragment) {
            ((SearchableFragment) fragment).onSearchQueryChanged(query);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSettings();
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }



    private void startSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

}