package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flyco.tablayout.InfiniteTabLayout;
import com.flyco.tablayoutsamples.R;
import com.flyco.tablayoutsamples.utils.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

public class InfinitiTabActivity extends AppCompatActivity {

    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private final String[] mTitles = new String[]{"北京", "短视频", "全球","国外", "国内"};
    private MyPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);
        getData();
        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        InfiniteTabLayout infiniteTabLayout = (InfiniteTabLayout) findViewById(R.id.infiniteTabLayout);
        infiniteTabLayout.setViewPager(vp,mTitles);
        vp.setCurrentItem(4);
    }

    private void getData() {
        data.add(mTitles[mTitles.length-1]);
        for (int i = 0; i < mTitles.length; i++) {
            data.add(mTitles[i]);
        }
        data.add(mTitles[0]);
        for (String title : data) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position);
        }

        @Override
        public Fragment getItem(int position) {
//            if(position == data.size()-1){
//                return mFragments.get(1);
//            }else if(position == 0){
//                return mFragments.get(data.size()-2);
//            }
            return mFragments.get(position);
        }
    }
}
