/**
 *
 */

package com.bobao.identifypro.ui.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.manager.UMengShareManager;
import com.bobao.identifypro.utils.AnimatorUtils;
import com.bobao.identifypro.utils.BitmapUtils;
import com.bobao.identifypro.utils.FastBlur;
import com.bobao.identifypro.utils.SizeUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;

/**
 *
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

    //    private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
    private Activity mActivity;
    private View mMaskView;
    private LinearLayout mCollectLinearLayout;
    private ImageView mCollectImageView;
    private View mAttentionAnimatorView;
    private TextView mCollectTextView;

    private String mShareType;//用于友盟统计:资讯分享 专家分享
    private String mId;//资讯ID 专家ID
    private HashMap<String, String> mUmengMap = new HashMap<>();
    private boolean mIsCollected;

    public CustomShareBoard(Activity activity) {
        super(activity);
        this.mActivity = activity;
        initView(activity);
    }

    public void setShareContent(String content, String imgPath, String targetUrl) {
        UMengShareManager.getInstance(mActivity).setShareContent(content, imgPath, targetUrl);
    }

    public void setShareContent(String title, String content, String imgPath, String targetUrl) {
        UMengShareManager.getInstance(mActivity).setShareContent(title, content, imgPath, targetUrl);
    }

    public void setShareType(String type) {
        mShareType = type;
        mUmengMap.put(UmengConstants.KEY_SHARE_TYPE, mShareType);
    }

    public void setId(String id) {
        mId = id;
        mUmengMap.put(UmengConstants.KEY_SHARE_CONTENT_ID, mId);
    }

    public void setCollectListener(View.OnClickListener listener) {
        mCollectLinearLayout.setOnClickListener(listener);
    }

    public void initmIsCollected(boolean isCollected) {
        mAttentionAnimatorView.setVisibility(isCollected ? View.VISIBLE : View.GONE);
        mCollectImageView.setVisibility(isCollected ? View.VISIBLE : View.GONE);
        mCollectTextView.setText(mIsCollected ? R.string.have_collected : R.string.collection);
    }

    public void setmIsCollected(boolean isCollected) {
        mIsCollected = isCollected;
        mAttentionAnimatorView.setVisibility(View.VISIBLE);
        AnimatorUtils.startStarAnimator(mAttentionAnimatorView, 600, isCollected, mCollectionAnimatorListenerAdapter);
    }

    private AnimatorListenerAdapter mCollectionAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mAttentionAnimatorView.setVisibility(mIsCollected ? View.VISIBLE : View.GONE);
            mCollectImageView.setVisibility(mIsCollected ? View.VISIBLE : View.GONE);
            mCollectTextView.setText(mIsCollected ? R.string.have_collected : R.string.collection);
        }
    };

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_share_board, null);
        mCollectImageView = (ImageView) rootView.findViewById(R.id.img_collect);
        mAttentionAnimatorView = rootView.findViewById(R.id.img_animator_collect);
        mAttentionAnimatorView.setVisibility(View.GONE);
        mCollectTextView = (TextView) rootView.findViewById(R.id.tv_collect);
        mCollectLinearLayout = (LinearLayout) rootView.findViewById(R.id.ll_collect);
        rootView.findViewById(R.id.ll_share_wechat).setOnClickListener(this);
        rootView.findViewById(R.id.ll_share_moments).setOnClickListener(this);
        rootView.findViewById(R.id.ll_share_qq).setOnClickListener(this);
        rootView.findViewById(R.id.ll_share_qq_zone).setOnClickListener(this);
        rootView.findViewById(R.id.ll_share_weibo).setOnClickListener(this);
        rootView.findViewById(R.id.tv_share_cancel).setOnClickListener(this);
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);

        mMaskView = rootView.findViewById(R.id.mask_view);
        mMaskView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return true;
            }
        });
    }

    public void applayBlur() {
        Bitmap bkg = BitmapUtils.getContentViewShot(mActivity);
        float scaleFactor = 2;
        float radius = 10;
        Bitmap overlay = Bitmap.createBitmap((int) (SizeUtils.getScreenWidth(mActivity) / scaleFactor),
                (int) (SizeUtils.getScreenHeight(mActivity) / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlurJniArray(overlay, (int) radius, true);
        mMaskView.setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), overlay));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_share_wechat:
                UMengShareManager.getInstance(mActivity).doShare(SHARE_MEDIA.WEIXIN);
                UmengUtils.onEvent(mActivity, EventEnum.UmengShareBoardWX, mUmengMap);
                break;
            case R.id.ll_share_moments:
                UMengShareManager.getInstance(mActivity).doShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                UmengUtils.onEvent(mActivity, EventEnum.UmengShareBoardCircle, mUmengMap);
                break;
            case R.id.ll_share_qq:
                UMengShareManager.getInstance(mActivity).doShare(SHARE_MEDIA.QQ);
                UmengUtils.onEvent(mActivity, EventEnum.UmengShareBoardQQ, mUmengMap);
                break;
            case R.id.ll_share_qq_zone:
                UMengShareManager.getInstance(mActivity).doShare(SHARE_MEDIA.QZONE);
                UmengUtils.onEvent(mActivity, EventEnum.UmengShareBoardQZone, mUmengMap);
                break;
            case R.id.ll_share_weibo:
                UMengShareManager.getInstance(mActivity).sinaAuth();
                UmengUtils.onEvent(mActivity, EventEnum.UmengShareBoardSina, mUmengMap);
                break;
            default:
                break;
        }
        dismiss();
    }
}
