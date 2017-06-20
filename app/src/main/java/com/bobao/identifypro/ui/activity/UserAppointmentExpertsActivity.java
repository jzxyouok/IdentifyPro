package com.bobao.identifypro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.fragment.UserAppointmentExpertsFragment;
import com.bobao.identifypro.ui.widget.indicator.SimpleIndicator;
import com.bobao.identifypro.utils.UmengUtils;

/**
 * 我的预约专家页面
 */
public class UserAppointmentExpertsActivity extends FragmentActivity {
    private ViewPager mFragmentsContainer;
    private FragmentManager mChildFragmentManager;
    private UserIdentifyFragmentPagerAdapter mUserIdentifyFragmentPagerAdapter;
    //    private TabPageIndicator mVpi;
    private SimpleIndicator mVpi;
    private int mPageIndex;

    //    type:1未支付 2已支付未鉴定 3已鉴定
    private String[] TITLES = {"待支付", "进行中", "已完成"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sub);
        initView();
    }

    protected void initView() {
        mFragmentsContainer = (ViewPager) findViewById(R.id.vp_container);
        mChildFragmentManager = getSupportFragmentManager();
        mUserIdentifyFragmentPagerAdapter = new UserIdentifyFragmentPagerAdapter(mChildFragmentManager);
        mFragmentsContainer.setAdapter(mUserIdentifyFragmentPagerAdapter);
        mVpi = (SimpleIndicator) findViewById(R.id.vpi_identify);
        mVpi.setViewPager(mFragmentsContainer);
        mVpi.setTabTitles(TITLES);
        Intent intent = getIntent();
        mPageIndex = intent.getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, 0);
        mFragmentsContainer.setCurrentItem(mPageIndex);
        initTile();
    }

    private void initTile() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.my_appointment_expert));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class UserIdentifyFragmentPagerAdapter extends FragmentPagerAdapter {

        public UserIdentifyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return UserAppointmentExpertsFragment.creatUserIdentifyFragment(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 这里Destroy的是Fragment的视图层次，并不是Destroy Fragment对象
            super.destroyItem(container, position, object);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }
}
