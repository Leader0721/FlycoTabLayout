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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.tablayoutsamples.R;

import java.util.ArrayList;
import java.util.List;

public class MyTabLayoutActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private RecyclerView tablayout;
    private View selectedView;
    private ViewPager mViewPager;
    private int itemWidth = 0;
    private static final String TAG = "MyTabLayoutActivity";
    private final String[] mTitleData = {
            "北京", "短视频", "全球","国外", "国内"
    };
    private  String[] mTitles;
    private MyAdapter rvAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private RecyclerView bgLayout;
    private LinearLayoutManager bgLinerLayoutManager;
    private BgAdapter bgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tab_layout);
        initData();
        tablayout = (RecyclerView) findViewById(R.id.tab_layout);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tablayout.setLayoutManager(linearLayoutManager);

        bgLayout = (RecyclerView) findViewById(R.id.rv_bg);
        bgLinerLayoutManager = new LinearLayoutManager(this);
        bgLinerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        bgLayout.setLayoutManager(bgLinerLayoutManager);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        itemWidth = width/5;
        rvAdapter = new MyAdapter();
        tablayout.setAdapter(rvAdapter);

        bgAdapter = new BgAdapter(mTitleData);
        bgLayout.setAdapter(bgAdapter);

        selectedView = findViewById(R.id.current_bg);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) selectedView.getLayoutParams();
        params = new FrameLayout.LayoutParams(itemWidth,params.height);
        params.gravity = Gravity.CENTER;
        selectedView.setLayoutParams(params);



        mViewPager = (ViewPager) findViewById(R.id.content_layout);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        tablayout.scrollToPosition(250);
        mViewPager.setCurrentItem(3);

    }

    /**
     * 实例化数据
     * {"国内", "北京", "短视频", "全球","国外", "国内", "北京"}
     */
    private void initData() {
        int length = mTitleData.length;
        mTitles = new String[length+2];
        mTitles[0] = mTitleData[length-1];
        mTitles[length+1] = mTitleData[0];
        for (int i = 0; i < length; i++) {
            mTitles[i+1] = mTitleData[i];
        }

        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tablayout.scrollTo(100,0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(positionOffset==0){
            selectedView.setVisibility(View.GONE);
            bgLayout.scrollBy((int) (itemWidth*positionOffset)*itemWidth,0);
            bgLayout.setVisibility(View.GONE);
            tablayout.setVisibility(View.VISIBLE);

        }else{
            tablayout.setVisibility(View.GONE);
            selectedView.setVisibility(View.VISIBLE);
            bgLayout.scrollBy((int) (itemWidth*positionOffset)*itemWidth,0);
            bgLayout.setVisibility(View.VISIBLE);
        }

        Log.e(TAG, "onPageScrolled: positionOffset——"+positionOffset+"positionOffsetPixels——"+positionOffsetPixels);

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

    class MyAdapter extends RecyclerView.Adapter<ViewHolder>{

        private int currentItem = 0;


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
            if(position == 0){
                position = 5;
            }
            final int tempposition = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mViewPager.setCurrentItem(tempposition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return  500;
        }

        public void setCurrentItem(int i){
            if(i == 5){
                i =0;
            }
            this.currentItem = i;
            bgAdapter.setCurrentItem(i);
            notifyDataSetChanged();

            int total = 250 + i;
            linearLayoutManager.scrollToPositionWithOffset(total,itemWidth*2);
            bgLinerLayoutManager.scrollToPositionWithOffset(i+4,itemWidth*2);

        }

        public int getCurrentItem(){
            return currentItem;
        }

    }

    class BgAdapter extends RecyclerView.Adapter<ViewHolder>{

        private List<String> mData = new ArrayList<>();
        private String currentItem = "北京";

        /**
         *
         * @param data  数组长度 >= 5
         *    "北京", "短视频", "全球","国外", "国内"
         * "北京", "短视频", "全球","国外","北京", "短视频", "全球","国外", "国内", "短视频", "全球","国外", "国内"
         */
        public BgAdapter(String[] data){
            int length = data.length;
            for (int i = 0; i < length; i++) {
                mData.add(data[i]);
            }
            mData.add(data[length-4]);
            mData.add(data[length-3]);
            mData.add(data[length-2]);
            mData.add(data[length-1]);
            mData.add(0,data[3]);
            mData.add(0,data[2]);
            mData.add(0,data[1]);
            mData.add(0,data[0]);

        }

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
            if(mData.get(position).equals(currentItem)){
                holder.titleView.setTextColor(Color.WHITE);
            }else{
                holder.titleView.setTextColor(Color.BLACK);
            }

            holder.titleView.setText(mData.get(position));

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void setCurrentItem(int i){
            this.currentItem = mTitleData[i];
            notifyDataSetChanged();
        }

    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.tv_tab_title);
        }
    }
}
