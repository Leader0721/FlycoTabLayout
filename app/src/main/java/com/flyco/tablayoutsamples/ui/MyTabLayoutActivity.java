package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.flyco.tablayoutsamples.R;

import java.util.ArrayList;

public class MyTabLayoutActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private RecyclerView tablayout;
    private View selectedView;
    private ViewPager mViewPager;
    private int itemWidth = 0;
    private static final String TAG = "MyTabLayoutActivity";
    private final String[] mTitles = {

            "国内","北京", "短视频", "全球","国外", "国内","北京"
    };
    private MyAdapter rvAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tab_layout);
        tablayout = (RecyclerView) findViewById(R.id.tab_layout);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tablayout.setLayoutManager(linearLayoutManager);


        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        itemWidth = width/4;
        rvAdapter = new MyAdapter();
        tablayout.setAdapter(rvAdapter);

        selectedView = findViewById(R.id.current_bg);


        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }

        mViewPager = (ViewPager) findViewById(R.id.content_layout);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        tablayout.scrollTo(Integer.MAX_VALUE/2,0);
        mViewPager.setCurrentItem(3);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if(position == 0){
            position = mTitles.length-2;
        }else if(position == mTitles.length-1){
           position = 1;
        }
        mViewPager.setCurrentItem(position,false);
        rvAdapter.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        public int currentItem = 0;
        private String[] mTitleData = {

                "国内","北京", "短视频", "全球","国外", "国内","北京"
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tab,parent,false);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params = new ViewGroup.LayoutParams(itemWidth,params.height);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            position = position % mTitleData.length;


            if(currentItem == position){
                Log.e(TAG, "onBindViewHolder: "+position);
                holder.itemView.setBackgroundResource(R.drawable.item_bg);
                holder.titleView.setTextColor(Color.WHITE);
            }else{
                holder.itemView.setBackground(null);
                holder.titleView.setTextColor(Color.BLACK);
            }

            holder.titleView.setText(mTitles[position]);
        }

        @Override
        public int getItemCount() {
            return  Integer.MAX_VALUE;
        }

        public void setCurrentItem(int i){

            this.currentItem = i;
            notifyDataSetChanged();
            int firstposition = linearLayoutManager.findFirstVisibleItemPosition();
            int currentPosition = firstposition % mTitles.length;
            int sclloX = 0;
            int dex = Math.abs(currentItem-currentPosition);
            switch (dex){
                case 0:
                    sclloX = itemWidth*3/2;
                    break;
                case 1:
                    sclloX = itemWidth*1/2;
                    break;
                case 2:
                    sclloX = itemWidth*-1/2;
                    break;
                case 3:
                    sclloX = itemWidth*-3/2;
                    break;
                case 4:
                    sclloX = itemWidth*5/2;
                    break;
            }
            tablayout.scrollTo(sclloX,0);
        }

        public int getCurrentItem(){
            return currentItem;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView titleView;

            public ViewHolder(View itemView) {
                super(itemView);
                titleView = (TextView) itemView.findViewById(R.id.tv_tab_title);
            }
        }
    }
}