package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity implements NavigationHost{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();

            // Ocultar la barra de navegación cuando se inicia en el fragmento de Login
            bottomNavigationView.setVisibility(View.GONE);
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.standings) {
                    fragment = new HomeFragment();
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
            }
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
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

}