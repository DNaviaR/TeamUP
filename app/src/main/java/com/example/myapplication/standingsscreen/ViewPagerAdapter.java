package com.example.myapplication.standingsscreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull StandingsFragment fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ClasificacionFragment();
            case 1:
                return new GoleadoresFragment();
            case 2:
                return new AsistentesFragment();
            default:
                return new ClasificacionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Número de pestañas
    }
}

