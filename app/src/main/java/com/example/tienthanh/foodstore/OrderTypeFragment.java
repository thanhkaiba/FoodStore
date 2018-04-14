package com.example.tienthanh.foodstore;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class OrderTypeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_type, container, false);
        OrderTypeFragment.SectionsPagerAdapter adapter = new OrderTypeFragment.SectionsPagerAdapter(getChildFragmentManager());
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
                    bundle.putInt(OrderListFragment.TYPE, OrderListFragment.UNFINISHED);
                    break;
                case 1:
                    bundle.putInt(OrderListFragment.TYPE, OrderListFragment.FINISHED);
                    break;

            }
            OrderListFragment orderListFragment =   new OrderListFragment();
            orderListFragment.setArguments(bundle);
            return orderListFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "UNFINISHED";
                case 1:
                    return "FINISHED";
            }
            return null;
        }
    }


}
