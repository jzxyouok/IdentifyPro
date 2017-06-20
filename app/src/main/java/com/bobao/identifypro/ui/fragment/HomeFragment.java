package com.bobao.identifypro.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.HomeBannerResponse;
import com.bobao.identifypro.ui.activity.ExpertDetailActivity;
import com.bobao.identifypro.ui.activity.ServiceNoteActivity;
import com.bobao.identifypro.ui.widget.BannerSliderView;
import com.bobao.identifypro.ui.widget.sticker.SimpleIndicator;
import com.bobao.identifypro.ui.widget.sticker.StickHeaderViewPager;
import com.bobao.identifypro.utils.FileUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends BaseFragment implements StickHeaderViewPager.OnStickerScrollListener {
    private final static int BANNER_SCROLL_TIME = 5000;
    private RelativeLayout mHeaderView;

    public StickHeaderViewPager mServiceViewPager;

    private SliderLayout mSliderLayout;
    private List<BannerSliderView> mSlidingViewList;

    private List<HomeBannerResponse.DataEntity> mBannerData;
    private Bitmap mBannerBitmap;
    private String mSaveBannerImg;//个人头像保存的路径

    private SimpleIndicator mVpi;
    private View mTitleView;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (mBannerBitmap != null) {
                FileUtils.saveBitmapToFile(mBannerBitmap, StringUtils.getString(Environment.getExternalStorageDirectory(), File.separator, mSaveBannerImg));
            }
            File bannerFile = new File(Environment.getExternalStorageDirectory(), mSaveBannerImg);
            if (bannerFile.exists()) {
                mHeaderView.setBackgroundDrawable(Drawable.createFromPath(bannerFile.getAbsolutePath()));
            } else {
                mHeaderView.setBackgroundResource(R.drawable.icon_default);
            }
        }
    };

    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
        mSlidingViewList = new ArrayList<>();
        mBannerData = new ArrayList<>();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_home;
    }

    protected void initTitle() {
        mTitleView = mRootView.findViewById(R.id.layout_title);
        mTitleView.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void initContent() {
        initTitle();
        // 渲染banner
        mHeaderView = (RelativeLayout) mRootView.findViewById(R.id.rl_home_header);
        mSliderLayout = (SliderLayout) mHeaderView.findViewById(R.id.slidder_layout);

        mServiceViewPager = (StickHeaderViewPager) mRootView.findViewById(R.id.sticker_viewpager);
        mVpi = (SimpleIndicator) mRootView.findViewById(R.id.sticker_vpi);

        mSliderLayout.setSwipeView(mServiceViewPager);
        //设置viewpager
        attachServiceViewPager();
    }

    @Override
    protected void loadData() {
        // 请求banner数据
        mHandlerList.add(NetUtils.getInstance(false).get(getActivity(), NetConstant.getBannerListParams(getActivity()), new BannerListListener(getActivity())));
    }

    @Override
    protected void attachData() {
    }

    private void attachServiceViewPager() {
        //设置viewpager
        mServiceViewPager.getViewPager().setOffscreenPageLimit(2);
        StickHeaderViewPager.StickHeaderViewPagerBuilder.stickTo(mServiceViewPager)
                .setFragmentManager(getActivity().getSupportFragmentManager())
                .addScrollFragments(
                        StickerServiceFragment.getInstance(AppConstant.HOME_SERVICE_TYPE[0], AppConstant.HOME_SERVICE_TYPE_NAME[0]),
                        StickerCustomServiceFragment.getInstance(AppConstant.HOME_SERVICE_TYPE[1], AppConstant.HOME_SERVICE_TYPE_NAME[1]))
                .notifyData();
        mServiceViewPager.setOnStickerScrollListener(this);
        mVpi.setViewPager(mServiceViewPager.getViewPager());
        mServiceViewPager.getViewPager().setCurrentItem(0);
    }

    /**
     * 默认是滚动header，直到vpi位置,而这里需要滚动到标题下面为止,并且要处理标题的渐变,所以重写了滚动监听
     *
     * @param headView             sickerHeaderView
     * @param scrollY              RecyclerView的滚动距离
     * @param minHeaderTranslation 默认指定滚动的范围
     */
    @Override
    public void onStickerScroll(View headView, int scrollY, int minHeaderTranslation) {
        int titleHeight = mTitleView.getHeight();
        float translationY = Math.max(-scrollY, minHeaderTranslation + titleHeight);
        headView.setTranslationY(translationY);

        //背景色渐变处理
//        int fullColor = getResources().getColor(R.color.yellow0);
//        float percent = 1.0f * translationY / (minHeaderTranslation + titleHeight);
//        int alpha = (int) (Color.alpha(fullColor) * percent);
//        int red = Color.red(fullColor);
//        int green = Color.green(fullColor);
//        int blue = Color.blue(fullColor);
//        mTitleView.setBackgroundColor(Color.argb(alpha, red, green, blue));
    }

    private class BannerListListener extends NetUtils.Callback<HomeBannerResponse> {
        public BannerListListener(Context context) {
            super(context, HomeBannerResponse.class);
        }

        @Override
        public void onNetSuccess(HomeBannerResponse response) {
            if (getActivity() != null && !response.getError() && response.getData() != null && response.getData().size() > 0) {
                mBannerData = response.getData();
                if (!TextUtils.isEmpty(mBannerData.get(0).getImage())) {
                    mSaveBannerImg = String.format("home_banner_%s.png", IntentConstant.APP_THUMBNAIL_NAME);
                    if (!TextUtils.equals(mBannerData.get(0).getImage(), UserInfoUtils.getBannerImagePath(mContext))) {
                        UserInfoUtils.saveBannerImagePath(mContext, mBannerData.get(0).getImage());
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mBannerBitmap = Picasso.with(getActivity()).load(UserInfoUtils.getBannerImagePath(mContext)).get();
                                mHandler.sendEmptyMessage(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                if (mSliderLayout != null) {
                    updateAutoCycleBanner();
                    mSliderLayout.startAutoCycle(BANNER_SCROLL_TIME, BANNER_SCROLL_TIME, true);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onPageStart("HomeFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSliderLayout != null)
            mSliderLayout.stopAutoCycle();
        UmengUtils.onPageEnd("HomeFragment");
    }

    @Override
    public void onDestroyView() {
        if (mSliderLayout != null) {
            mSliderLayout.removeAllSliders();
            mSliderLayout.stopAutoCycle();
            mSliderLayout = null;
        }
        mSlidingViewList.clear();
        super.onDestroyView();
    }

    private void updateAutoCycleBanner() {
        if (mSliderLayout == null)
            return;
        mSliderLayout.removeAllSliders();
        mSliderLayout.stopAutoCycle();
        initSlidingItemViews();
        mSliderLayout.addAllSlider(mSlidingViewList);
        mSliderLayout.startAutoCycle(BANNER_SCROLL_TIME, BANNER_SCROLL_TIME, true);
    }

    private void initSlidingItemViews() {
        if (mSliderLayout == null)
            return;
        if (mBannerData == null || mBannerData.size() == 0) {
            return;
        }
        mSlidingViewList.clear();
        for (final HomeBannerResponse.DataEntity dataEntity : mBannerData) {
            BannerSliderView sliderView = new BannerSliderView(getActivity());
            sliderView.setUrl(dataEntity.getImage());
            sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Intent intent;
                    HashMap<String, String> hashMap = new HashMap<>();
                    switch (dataEntity.getType()) {
                        case "service":
                            if (!TextUtils.isEmpty(dataEntity.getId())) {
                                intent = new Intent(mContext, ServiceNoteActivity.class);
                                intent.putExtra(IntentConstant.SERVICE_TYPE, dataEntity.getId());
                                intent.putExtra(IntentConstant.SERVICE_TYPE_NAME, AppConstant.SERVICE_TYPE_NAME[Integer.valueOf(dataEntity.getId()) - 1]);
                                jump(intent);
                                hashMap.clear();
                                hashMap.put(UmengConstants.KEY_BANNER_TYPE_EXPERT, dataEntity.getId());
                                UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick, hashMap);
                            }
                            break;
                        case "expert":
                            intent = new Intent(mContext, ExpertDetailActivity.class);
                            intent.putExtra(IntentConstant.EXPERT_ID, dataEntity.getId());
                            jump(intent);
                            hashMap.clear();
                            hashMap.put(UmengConstants.KEY_BANNER_TYPE_EXPERT, dataEntity.getId());
                            UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick, hashMap);
                            break;
                        default:
                            break;
                    }
                }
            });
            mSlidingViewList.add(sliderView);
        }
    }
}
