package com.bobao.identifypro.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.manager.UserBaseInfoManager;
import com.bobao.identifypro.manager.WXDealManager;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.ui.dialog.ProgressDialog;
import com.bobao.identifypro.utils.AppUtils;
import com.bobao.identifypro.utils.CacheCleanUtil;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class SettingActivity extends BaseActivity {

    private TextView mUserUpdate;
    private TextView mUserClear;
    private View mUserQuit;
    private RelativeLayout mContactRl;
    private RelativeLayout mUpdateRl;
    private TextView mShowUpdateTv;
    private RelativeLayout mClearRl;
    private RelativeLayout mEvaluateRl;

    private String localAppVersionCode;
    private ProgressDialog mProgressDialog;

    private CommonDialog mCommonDialog;

    private View.OnClickListener mLogoutCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCommonDialog.dismiss();
        }
    };
    private View.OnClickListener mLogoutSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCommonDialog.dismiss();
            UmengUtils.onEvent(mContext, EventEnum.User_Setting_Quit_Success);
            UserInfoUtils.logOut(mContext);
            UserBaseInfoManager.getsInstance().logout(mContext);
            WXDealManager.getInstance().clearWXDealInfo();
            Intent exitIntent = new Intent(mContext, MainActivity.class);
            startActivity(exitIntent);
        }
    };
    private View.OnClickListener mClearCacheSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCommonDialog.dismiss();
            try {
                String size = CacheCleanUtil.getTotalCacheSize(mContext);
                if ("0K".equals(size)) {
                    DialogUtils.showShortPromptToast(mContext, StringUtils.getString(getString(R.string.cache_no_need_clean)));
                    return;
                }
                CacheCleanUtil.clearAllCache(mContext);
                mUserClear.setText(CacheCleanUtil.getTotalCacheSize(mContext));
                DialogUtils.showShortPromptToast(mContext, StringUtils.getString(getString(R.string.cache_clean_result)));
                UmengUtils.onEvent(mContext, EventEnum.User_Setting_Clear_Success);
            } catch (Exception e) {
                DialogUtils.showShortPromptToast(mContext, StringUtils.getString());
            }
        }
    };

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.set));
        setOnClickListener(backView);
    }


    @Override
    protected void initContent() {
        mClearRl = (RelativeLayout) findViewById(R.id.rl_user_clear);
        mContactRl = (RelativeLayout) findViewById(R.id.rl_contact_us);
        mUpdateRl = (RelativeLayout) findViewById(R.id.rl_user_update);
        mEvaluateRl = (RelativeLayout) findViewById(R.id.rl_user_evaluate);
        mUserClear = (TextView) findViewById(R.id.tv_user_clear_cache);
        mUserUpdate = (TextView) findViewById(R.id.tv_user_update);
        mShowUpdateTv = (TextView) findViewById(R.id.tv_bubble_update);
        mShowUpdateTv.setVisibility(UserInfoUtils.getAppIsUpdate(mContext) ? View.VISIBLE : View.GONE);
        mUserQuit = findViewById(R.id.btn_user_quit_account);
        try {
            String size = CacheCleanUtil.getTotalCacheSize(mContext);
            mUserClear.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        localAppVersionCode = AppUtils.getAppVersionName(mContext);
        mUserUpdate.setText(StringUtils.getString(getString(R.string.current_version), localAppVersionCode));
        setOnClickListener(mClearRl, mContactRl, mUpdateRl, mUserQuit, mEvaluateRl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_user_clear://清除缓存
                UmengUtils.onEvent(mContext, EventEnum.User_Setting_Clear);
                showLogoutDialog(R.string.submit_clear_cache, mClearCacheSubmitListener, mLogoutCancelListener);
                break;
            case R.id.rl_user_update://版本更新
                UmengUtils.onEvent(mContext, EventEnum.User_Setting_Update);
                if (mProgressDialog == null) {
                    mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.update_loading_dialog_title));
                }
                checkVersion();
                break;
            case R.id.btn_user_quit_account://退出
                UmengUtils.onEvent(mContext, EventEnum.User_Setting_Quit_Account);
                showLogoutDialog(R.string.submit_quit, mLogoutSubmitListener, mLogoutCancelListener);
                break;
            case R.id.rl_contact_us://联系我们
                jump(mContext, ContactUsActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.User_Contact_Us);
                break;
            case R.id.rl_user_evaluate://评价我们
                DialogUtils.showRateDialog(mContext);
                break;
            default:
                break;
        }
    }

    /**
     * 退出提示对话框
     */
    private void showLogoutDialog(int contentStrId, View.OnClickListener submitListener, View.OnClickListener cancelListener) {
        mCommonDialog = DialogUtils.showCommonDialog(mContext, getString(R.string.prompt), getString(contentStrId),
                getString(R.string.negative), getString(R.string.positive), submitListener, cancelListener);
    }

    /**
     * 版本检测
     */
    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
                if (updateStatus == IntentConstant.UMENG_UPDATE_UNLATEST && updateInfo != null) {
                    int target_size = String2Double2M(updateInfo.target_size);
                    String newVersion = String.format("%s\n%s", new Object[]{"最新版本: v" + updateInfo.version, "新版本大小: " + target_size + "M"});
                    String content = updateInfo.updateLog;
                    DialogUtils.showUpdateDialog(mContext, updateInfo, newVersion, content);
                    UmengUtils.onEvent(mContext, EventEnum.MyUpdateAlertDialogCreate);
                } else if (updateStatus == IntentConstant.UMENG_UPDATE_LATEST) {
                    DialogUtils.showShortPromptToast(mContext, R.string.update_latest);
                }
                // case 0: // has update
                // case 1: // has no update
                // case 2: // none wifi
                // case 3: // time out
            }
        });
        UmengUpdateAgent.update(this);
    }

    private int String2Double2M(String target_size) {
        return (int) Double.parseDouble(target_size) / (1024 * 1024);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {

    }
}
