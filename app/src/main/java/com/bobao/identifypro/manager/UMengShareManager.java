package com.bobao.identifypro.manager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.bobao.identifypro.R;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.Map;

/**
 * Created by you on 2015/6/29.
 */
public class UMengShareManager {
    public static String TYPE_SHARE_ORDER = "1";
    public static String TYPE_SHARE_EXPERT = "2";
    public static String TYPE_SHARE_INFO = "3";

    private static Context mContext;

    public static UMShareAPI mShareAPI = null;

    private String mTitle;
    private String mContent;
    private String mImgPath;
    private String mTargetUrl;

    private static UMengShareManager sInstance = new UMengShareManager();

    private OnShareCompleteLisnener mOnShareCompleteListener;

    public interface OnShareCompleteLisnener {
        void onShareComplete();
    }

    public static UMengShareManager getInstance(Context context) {
        mContext = context;
        mShareAPI = UMShareAPI.get(mContext);
        return sInstance;
    }

    public void setOnShareCompleteListener(OnShareCompleteLisnener listener) {
        mOnShareCompleteListener = listener;
    }

    /**
     * @param content 分享内容
     * @param imgPath 分享图片路径
     */
    public void setShareContent(String content, String imgPath, String targetUrl) {
        mTitle = "";
        mContent = content;
        mImgPath = imgPath;
        mTargetUrl = targetUrl;
    }

    /**
     * @param title   分享标题
     * @param content 分享内容
     * @param imgPath 分享图片路径
     */
    public void setShareContent(String title, String content, String imgPath, String targetUrl) {
        mTitle = title;
        mContent = content;
        mImgPath = imgPath;
        mTargetUrl = targetUrl;
    }

    /**
     * SINA授权与登录
     */
    public void sinaAuth() {
        mShareAPI.doOauthVerify((Activity) mContext, SHARE_MEDIA.SINA, umAuthListener);
    }

    public void doShare(SHARE_MEDIA sharePlatform) {
        performShare(sharePlatform);
    }

    private void performShare(SHARE_MEDIA platform) {
        if (SHARE_MEDIA.WEIXIN == platform && !mShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.WEIXIN)) {
            return;
        }
        if (SHARE_MEDIA.WEIXIN_CIRCLE == platform && !mShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.WEIXIN_CIRCLE) &&
                !mShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.WEIXIN)) {
            return;
        }
        if (SHARE_MEDIA.QQ == platform && !mShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.QQ)) {
            DialogUtils.showShortPromptToast(mContext, R.string.request_install_qq);
            return;
        }
        if (TextUtils.isEmpty(mImgPath)) {
            return;
        }
        File file = new File(mImgPath);
        if (!file.exists()) {
            return;
        }
        UMImage image = new UMImage(mContext, file);
        if (SHARE_MEDIA.WEIXIN_CIRCLE == platform) {
            mTitle = mContent;
        }
        System.out.print("112。。。" + mTitle +"。。。"+ image + "。。。" + mTargetUrl);
        new ShareAction((Activity) mContext).setPlatform(platform).setCallback(umShareListener)
                .withTitle(mTitle)
                .withText(mContent)
                .withMedia(image)
                .withTargetUrl(mTargetUrl)
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            DialogUtils.showShortPromptToast(mContext, StringUtils.getString(mContext.getString(R.string.share_success)));
            System.out.print("分享成功124。。。");
            if (mOnShareCompleteListener != null) {
                mOnShareCompleteListener.onShareComplete();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            DialogUtils.showShortPromptToast(mContext, StringUtils.getString(platform, mContext.getString(R.string.share_fail)));
            System.out.print("分享失败133。。。");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            DialogUtils.showShortPromptToast(mContext, StringUtils.getString(platform, mContext.getString(R.string.share_cancel)));
            System.out.print("分享取消139。。。");
        }
    };

    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_success);
            doShare(SHARE_MEDIA.SINA);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_fail);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_cancel);
        }
    };
}
