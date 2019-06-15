package com.headyassignment.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.headyassignment.R;
import com.headyassignment.db.entity.Category;
import com.headyassignment.fragments.CategoryListFragment;
import com.headyassignment.fragments.HomeFragment;
import com.headyassignment.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    Toolbar toolbar;
    List<Category> globalCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(HomeFragment.newInstance(), false);

        globalCategories = new ArrayList<>();

        CategoryViewModel.Factory factory = new CategoryViewModel.Factory(
                getApplication(), 0); // Zero because we have to get category with zero children
        final CategoryViewModel viewModel =
                ViewModelProviders.of(this, factory).get(CategoryViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(CategoryViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    globalCategories.clear();
                    globalCategories.addAll(categories);
                    addMenuItemInNavMenuDrawer();
                } else {
                    // TODO: 14-06-2019 Empty list handling
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.

        for (Category category : globalCategories) {
            if (category.getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                replaceFragment(CategoryListFragment.newInstance(category.getId()), true);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addMenuItemInNavMenuDrawer() {
        Menu menu = navigationView.getMenu();
        menu.clear();
        Menu submenu = menu.addSubMenu("Categories");

        for (Category category : globalCategories) {
            submenu.add(category.getName());
        }
        navigationView.invalidate();
    }

    /**
     * This method is used to replace Fragments in the container when nav menus are clicked.
     *
     * @param fragment New fragment to be replaced.
     */
    public void replaceFragment(Fragment fragment, boolean addThisToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (addThisToBackStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.actMain_frameLayout, fragment)
                    .addToBackStack(fragment.getTag())
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.actMain_frameLayout, fragment)
                    .commit();
        }
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }
}
