package com.bobao.identifypro.ui.popupwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bobao.identifypro.R;
import com.bobao.identifypro.utils.BitmapUtils;
import com.bobao.identifypro.utils.FastBlur;
import com.bobao.identifypro.utils.SizeUtils;

/**
 * Created by Fang on 2016/3/21.
 */
public class TestPopupWindow extends PopupWindow{
    private View mContentView;
    private Activity mContext;
    private View mMaskView;


    public TestPopupWindow(Activity context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_share_board, null);
        // 设置SelectPicPopupWindow的View
        setContentView(mContentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
//        setTouchable(true);
//        setFocusable(true);
//        setBackgroundDrawable(new BitmapDrawable());
//        setOutsideTouchable(true);
        // 刷新状态
        update();
        setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.AnimationPreview);
        mMaskView = mContentView.findViewById(R.id.mask_view);
        mMaskView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return true;
            }
        });
    }

    public void applayBlur() {
        Bitmap bkg = BitmapUtils.getContentViewShot(mContext);
        float scaleFactor = 2;
        float radius = 10;
        Bitmap overlay = Bitmap.createBitmap((int) (SizeUtils.getScreenWidth(mContext) / scaleFactor),
                (int) (SizeUtils.getScreenHeight(mContext) / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlurJniArray(overlay, (int) radius, true);
        mMaskView.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), overlay));
    }

    /**
     * 显示popupWindow
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
