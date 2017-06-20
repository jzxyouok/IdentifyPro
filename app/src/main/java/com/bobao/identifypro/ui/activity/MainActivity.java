package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.UserNoChargeResponse;
import com.bobao.identifypro.ui.fragment.BaseFragment;
import com.bobao.identifypro.ui.fragment.DiscoveryFragment;
import com.bobao.identifypro.ui.fragment.HomeFragment;
import com.bobao.identifypro.ui.fragment.UserFragment;
import com.bobao.identifypro.utils.MPermissionUtil;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;

/**
 * Created by star on 15/5/29.
 */
public class MainActivity extends BaseActivity {
    // 当前fragment的id
    private int currentFragmentId;

    private HomeFragment mHomeFragment;
    private DiscoveryFragment mDiscoveryFragment;
    private UserFragment mUserFragment;
    private BaseFragment[] mFragments;
    // 当前fragment
    private BaseFragment mCurrentFragment;
    // 上一个fragment
    private BaseFragment mPriorFragment;
    // 是否需要改变fragment
    private boolean isNeedChangeFragment;
    // footer
    private TextView mHomeView;
    private TextView mDiscoveryView;
    private TextView mUserView;
    private TextView mUnChargeCountBubbleTv;

    private boolean mIsFromNotification;
    private int mIdentifyPageIndex;

    @Override
    protected void getIntentData() {
        currentFragmentId = getIntent().getIntExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_home);
        mIsFromNotification = getIntent().getBooleanExtra(IntentConstant.IS_FROM_NOTIFICATION, false);
        if (mIsFromNotification) {
            mIdentifyPageIndex = getIntent().getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
            Intent intent = new Intent(this, UserAppointmentExpertsActivity.class);
            intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, mIdentifyPageIndex);
            jump(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int exitCode = intent.getIntExtra(IntentConstant.APP_EXIT_KEY, 0);
        if (exitCode == IntentConstant.APP_EXIT_CODE) {
            finish();
        }
        mIsFromNotification = intent.getBooleanExtra(IntentConstant.IS_FROM_NOTIFICATION, false);
        if (mIsFromNotification) {
            mIdentifyPageIndex = intent.getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
            Intent jumpIntent = new Intent(mContext, UserAppointmentExpertsActivity.class);
            jumpIntent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, mIdentifyPageIndex);
            jump(jumpIntent);
        }

        currentFragmentId = getIntent().getIntExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_home);
        if (currentFragmentId != 0) {
            changeFragment(currentFragmentId);
        }
    }

    @Override
    protected void initData() {
        isNeedChangeFragment = true;
        isNeedDoubleClick = true;
        setSwipeBackEnable(false);
        MPermissionUtil.requestPermissionCamera(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.PERMISSION_REQ_CODE_READ_CAMERA) {
            MPermissionUtil.requestPermissionStorage(this);
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initContent() {
        mHomeView = (TextView) findViewById(R.id.tv_home);
        mDiscoveryView = (TextView) findViewById(R.id.tv_discovery);
        mUserView = (TextView) findViewById(R.id.tv_user);
        mUnChargeCountBubbleTv = (TextView) findViewById(R.id.tv_bubble_user_info);

        setOnClickListener(mHomeView, mDiscoveryView, mUserView);
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
        mHomeFragment = new HomeFragment();
        mDiscoveryFragment = new DiscoveryFragment();
        mUserFragment = new UserFragment();
        mFragments = new BaseFragment[]{mHomeFragment, mDiscoveryFragment, mUserFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, mHomeFragment)
                .add(R.id.fl_container, mDiscoveryFragment)
                .add(R.id.fl_container, mUserFragment)
                .hide(mDiscoveryFragment)
                .hide(mUserFragment)
                .show(mHomeFragment)
                .commitAllowingStateLoss();
        mPriorFragment = mHomeFragment;
        changeNavigationIconState(true, false, false);
    }

    @Override
    protected void refreshData() {
        if (UserInfoUtils.checkUserLogin(mContext)) {
            NetUtils.getInstance(false).get(mContext, NetConstant.getUnPayCountParams(mContext), new UserUnChargeCountListener(mContext));
        } else {
            mUnChargeCountBubbleTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home:
            case R.id.tv_discovery:
            case R.id.tv_user:
                changeFragment(view.getId());
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    private void changeFragment(int id) {
        switch (id) {
            case R.id.tv_home:
                if (mCurrentFragment == null || !(mCurrentFragment instanceof HomeFragment)) {
                    mCurrentFragment = mFragments[0];
                    isNeedChangeFragment = true;
                    changeNavigationIconState(true, false, false);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_Home);
                }
                break;
            case R.id.tv_discovery:
                if (!(mCurrentFragment instanceof DiscoveryFragment)) {
                    mCurrentFragment = mFragments[1];
                    isNeedChangeFragment = true;
                    changeNavigationIconState(false, true, false);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_Identify);
                }
                break;
            case R.id.tv_user:
                if (!(mCurrentFragment instanceof UserFragment)) {
                    mCurrentFragment = mFragments[2];
                    isNeedChangeFragment = true;
                    changeNavigationIconState(false, false, true);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_User);
                }
                break;
            default:
                break;
        }
        if (isNeedChangeFragment) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mPriorFragment);
            if (!mCurrentFragment.isAdded()) {
                fragmentTransaction.add(R.id.fl_container, mCurrentFragment);
            }
            fragmentTransaction.show(mCurrentFragment).commitAllowingStateLoss();
            if(mCurrentFragment instanceof HomeFragment){
                mHomeFragment.mServiceViewPager.getViewPager().setCurrentItem(0);
            }
        }
        mPriorFragment = mCurrentFragment;
        isNeedChangeFragment = false;
    }

    private class UserUnChargeCountListener extends NetUtils.Callback<UserNoChargeResponse> {

        public UserUnChargeCountListener(Context context) {
            super(context, UserNoChargeResponse.class);
        }

        @Override
        public void onNetSuccess(UserNoChargeResponse response) {
            if (response != null && !response.getError() && response.getData() > 0) {
                mUnChargeCountBubbleTv.setVisibility(View.VISIBLE);
                int count = response.getData();
                if (count > 99) {
                    count = 99;
                }
                mUnChargeCountBubbleTv.setText(String.valueOf(count));
            } else {
                mUnChargeCountBubbleTv.setVisibility(View.GONE);
            }
        }
    }

    private void changeNavigationIconState(boolean home, boolean discovery, boolean user) {
        mHomeView.setSelected(home);
        mDiscoveryView.setSelected(discovery);
        mUserView.setSelected(user);
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