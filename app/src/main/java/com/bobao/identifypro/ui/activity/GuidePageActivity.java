package com.bobao.identifypro.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bobao.identifypro.R;
import com.bobao.identifypro.manager.IdentifyProUserInfoManager;
import com.bobao.identifypro.ui.adapter.NavigationPagerAdapter;
import com.bobao.identifypro.utils.AnimatorUtils;
import com.bobao.identifypro.utils.UmengUtils;


/**
 * Created by you on 2015/6/1.
 */
public class GuidePageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private View mAdvanceView;
    private ViewPager mViewPager;
    private NavigationPagerAdapter mAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected void initData() {
        isNeedDoubleClick = true;
        setSwipeBackEnable(false);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_guide_page_layout;
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initContent() {
        mViewPager = (ViewPager) findViewById(R.id.vp_navigation);
        mAdapter = new NavigationPagerAdapter(mContext);
        mAdapter.setOnClickListener(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        mAdvanceView = findViewById(R.id.btn_navigation_advance);
        AnimatorUtils.startAdvanceAnimator(mAdvanceView, 1000);
        setOnClickListener(mAdvanceView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_navigation_start:
                mHandler.post(new AutoStart(new Intent(mContext, MainActivity.class)));
                break;
            case R.id.rl_splash:
                mHandler.post(new AutoStart(new Intent(mContext, MainActivity.class)));
                break;
            case R.id.btn_navigation_advance:
                mViewPager.setCurrentItem(mAdapter.getCount() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    protected void refreshData() {
        isNeedDoubleClick = true;
        setSwipeBackEnable(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == mAdapter.getCount() - 1) {
            mAdvanceView.setVisibility(View.INVISIBLE);
        } else {
            mAdvanceView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 启动activity
     */
    private class AutoStart implements Runnable {
        private Intent intent;

        private AutoStart(Intent intent) {
            this.intent = intent;
        }

        @Override
        public void run() {
            jump(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
            IdentifyProUserInfoManager.setSplashShow(mContext, true);
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
