package com.example.myapplication;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.homescreen.HomeFragment;
import com.example.myapplication.standingsscreen.StandingsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements NavigationHost{

    BottomNavigationView bottomNavigationView;
    MaterialToolbar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        topbar = findViewById(R.id.topbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();

            // Ocultar la barra de navegación cuando se inicia en el fragmento de Login
            //bottomNavigationView.setVisibility(View.GONE);
            //topbar.setVisibility(View.GONE);
        }

        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.bottom_nav_item_color);

        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setItemTextColor(colorStateList);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.standings) {
                fragment = new StandingsFragment();
            } else if (itemId == R.id.matches) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.profile) {
                fragment = new RegisterFragment();
            }

            if (fragment != null) {
                navigateTo(fragment, false);
                return true;
            }
            return false;
        });
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
        updateBottomNavigationVisibility(fragment);
    }

    public boolean isLoginOrRegisterFragment(Fragment fragment) {
        return fragment instanceof LoginFragment || fragment instanceof RegisterFragment;
    }

    private void updateBottomNavigationVisibility(Fragment fragment) {
        if (isLoginOrRegisterFragment(fragment)) {
            bottomNavigationView.setVisibility(View.GONE);
            topbar.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            topbar.setVisibility(View.VISIBLE);
        }
    }

}