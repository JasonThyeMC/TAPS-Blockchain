package com.TAPS.blockchain.app;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class transactionListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        toolbar.setTitle("Activity");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        viewPager.setAdapter(new transactionListView.SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                default:
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 1);
                    transactionDataFrag fragment = new transactionDataFrag();
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("title", 2);
                    transactionDataFrag fragment1 = new transactionDataFrag();
                    fragment1.setArguments(bundle1);
                    return fragment1;
                case 2:
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("title", 3);
                    transactionDataFrag fragment2 = new transactionDataFrag();
                    fragment2.setArguments(bundle2);
                    return fragment2;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                default:
                    return "All";
                case 1:
                    return "Money In";
                case 2:
                    return "Money Out";
            }
        }
    }
}
