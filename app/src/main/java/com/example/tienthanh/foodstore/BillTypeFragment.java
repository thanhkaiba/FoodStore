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



public class BillTypeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bill_type, container, false);
        BillTypeFragment.SectionsPagerAdapter adapter = new BillTypeFragment.SectionsPagerAdapter(getChildFragmentManager());
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
                    bundle.putInt(BillListFragment.TYPE, Bill.SELL);
                    break;
                case 1:
                    bundle.putInt(BillListFragment.TYPE, Bill.RECEIPT);
                    break;
                case 2:
                    bundle.putInt(BillListFragment.TYPE, Bill.ISSUE);
                    break;


            }
            BillListFragment billListFragment =   new BillListFragment();
            billListFragment.setArguments(bundle);
            return billListFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "SELL";
                case 1:
                    return "RECEIPT";
                case 2:
                    return "ISSUE";
            }
            return null;
        }
    }


}
