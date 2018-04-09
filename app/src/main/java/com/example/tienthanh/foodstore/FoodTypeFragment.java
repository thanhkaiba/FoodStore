package com.example.tienthanh.foodstore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;


public class FoodTypeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_type, container, false);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager pager =  root.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabLayout tabLayout = root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        return root;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();

            switch (position) {
                case 0:
                   bundle.putString(FoodListFragment.TYPE, FoodListFragment.FRUIT);
                   break;
                case 1:
                    bundle.putString(FoodListFragment.TYPE, FoodListFragment.MEAT);
                    break;
                case 2:
                    bundle.putString(FoodListFragment.TYPE, FoodListFragment.VEGETABLE);
                    break;
                case 3:
                    bundle.putString(FoodListFragment.TYPE, FoodListFragment.SEAFOOD);
                    break;
                case 4:
                    bundle.putString(FoodListFragment.TYPE, FoodListFragment.NUTS_GRAINS_BEANS);
                    break;
            }
            FoodListFragment foodListFragment =   new FoodListFragment();
            foodListFragment.setArguments(bundle);
            return foodListFragment;
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "FRUIT";
                case 1:
                    return "MEAT";
                case 2:
                    return "VEGETABLE";
                case 3:
                    return "SEAFOOD";
                case 4:
                    return "NUTS, GRAINS & BEANS";
            }
            return null;
        }
    }

}

