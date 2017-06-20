package com.bobao.identifypro.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.HeadImageUploaderResponse;
import com.bobao.identifypro.domain.UserPrivateInfo;
import com.bobao.identifypro.utils.BitmapUtils;
import com.bobao.identifypro.utils.CropFileUtils;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.FileUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.exception.HttpException;

import java.io.File;

public class UserPrivateInfoActivity extends BaseActivity {

    private SimpleDraweeView mPortraitView;
    private View mChangePortraitView;

    private TextView mNickNameEditView;
    private View mChangeNickNameView;

    private TextView mUserNameView;
    private TextView mEditUserNameView;

    private TextView mPhoneEditView;
    private View mChangePhoneView;

    private View mChangePasswordView;

    public final static String USER_NICKNAME = "user_nickname";

    private boolean mIsPhoneCheckedFlag;

    private Dialog mDialog;

    private String mSaveHeadImg;//个人头像保存的路径
    private String mHeadImg;//个人头像的拍照保存路径
    private Uri mHeadUri;//个人头像的Uir

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_private_userinfo;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.edit_introduce);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mPortraitView = (SimpleDraweeView) findViewById(R.id.img_head);
        mChangePortraitView = findViewById(R.id.tl_change_head_img);

        mNickNameEditView = (TextView) findViewById(R.id.et_nick_name);
        mChangeNickNameView = findViewById(R.id.rl_edit_nickname);

        mUserNameView = (TextView) findViewById(R.id.tv_username);
        mEditUserNameView = (TextView) findViewById(R.id.et_username);

        mPhoneEditView = (TextView) findViewById(R.id.et_phone);
        mChangePhoneView = findViewById(R.id.rl_edit_phone_entry);

        mChangePasswordView = findViewById(R.id.rl_change_password);

        if (UserInfoUtils.getSocialLoginFlg(this)) {
            mChangePasswordView.setVisibility(View.GONE);
        } else {
            mChangePasswordView.setVisibility(View.VISIBLE);
        }
        onScroll();
        setOnClickListener(mChangePasswordView, mChangePortraitView, mChangeNickNameView, mChangePhoneView);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
    }

    @Override
    protected void refreshData() {
        String cacheHeadImgUrl = UserInfoUtils.getCacheHeadImagePath(mContext);
        if (!TextUtils.isEmpty(cacheHeadImgUrl)) {
            mPortraitView.setImageURI(Uri.parse(cacheHeadImgUrl));
        }
        getData();
    }

    @Override
    public void onClick(View view) {
        final String mobile = mPhoneEditView.getText().toString().trim();
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tl_change_head_img:
                mSaveHeadImg = String.format("head_img_upload_%s.png", IntentConstant.APP_THUMBNAIL_NAME);
                mHeadImg = String.format("headImg_%s.jpg", IntentConstant.APP_THUMBNAIL_NAME);
                mHeadUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), mHeadImg));
                uploadHeadPhoto();
                break;
            case R.id.rl_edit_nickname:
                intent = new Intent(mContext, EditNickNameActivity.class);
                intent.putExtra(USER_NICKNAME, mNickNameEditView.getText().toString().trim());
                jump(intent);
                break;
            case R.id.rl_edit_phone_entry:
                intent = new Intent(mContext, VerifyOldPhoneActivity.class);
                intent.putExtra(IntentConstant.PHONE_NUMBER, mobile);
                intent.putExtra(IntentConstant.ACTIVITY_TITLE, getString(R.string.phone_certification));
                intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_EDIT_PHONE);
                jump(intent, IntentConstant.RequestCodeCheckPhoneNumber);
                break;
            case R.id.rl_change_password:
                intent = new Intent(mContext, VerifyOldPhoneActivity.class);
                intent.putExtra(IntentConstant.PHONE_NUMBER, mobile);
                intent.putExtra(IntentConstant.ACTIVITY_TITLE, getString(R.string.verify_phone_number));
                intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_EDIT_PASSWORD);
                jump(intent);
                break;
            default:
                break;
        }
    }

    private void getData() {
        NetUtils.getInstance(false).get(mContext, NetConstant.getUserPrivateInfoParams(mContext), new UserPrivateInfoListener(mContext));
    }


    private void uploadHeadPhoto() {
        mDialog = DialogUtils.showPickPhotoDialog(mContext, R.layout.dialog_select_head_bg, R.id.ll_select_camera, R.id.ll_select_photo, mTakePhotoListener, mPickPhotoListener);
    }

    private View.OnClickListener mTakePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mHeadUri);
            jump(intent, IntentConstant.REQUESTCODE_CAMERA);
        }
    };

    private View.OnClickListener mPickPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            pickIntent.putExtra(MediaStore.EXTRA_OUTPUT, mHeadUri);
            jump(pickIntent, IntentConstant.REQUESTCODE_PICK);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentConstant.REQUESTCODE_CAMERA:
                    startPhotoZoom(mHeadUri);
                    break;
                case IntentConstant.REQUESTCODE_PICK:
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    String path = CropFileUtils.getSmartFilePath(mContext, data.getData());
                    boolean result = CropFileUtils.copyFile(path, mHeadUri.getPath());
                    if (!result) {
                        return;
                    }
                    startPhotoZoom(mHeadUri);
                    break;
                case IntentConstant.REQUESTCODE_CUTTING:
                    setPicToView();
                    break;
                case IntentConstant.RequestCodeCheckPhoneNumber:
                    mIsPhoneCheckedFlag = data.getBooleanExtra(IntentConstant.CHECK_PHONE_FLAG, false);
                    if (mIsPhoneCheckedFlag) {
                        mPhoneEditView.setText(UserInfoUtils.getPhone(mContext));
                        DialogUtils.showShortPromptToast(mContext, R.string.check_phone_success);
                    } else {
                        DialogUtils.showShortPromptToast(mContext, R.string.check_phone_failed);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * save the picture data
     */
    private void setPicToView() {
        Bitmap photo = BitmapUtils.decodeUriAsBitmap(this, mHeadUri);
        if (photo != null) {
            Bitmap roundPhoto = BitmapUtils.getBitmap(photo, photo.getWidth(), photo.getHeight());
            FileUtils.saveBitmapToFile(roundPhoto, Environment.getExternalStorageDirectory() + File.separator + mSaveHeadImg);
            startUpLoadHeadImg();
        }
    }

    private void startUpLoadHeadImg() {
        File headImg = new File(Environment.getExternalStorageDirectory(), mSaveHeadImg);
        NetUtils.getInstance(false).get(mContext, NetConstant.getUpLoadHeadImgParams(mContext, headImg), new UpLoadHeadImgListener(mContext));
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHeadUri);
        startActivityForResult(intent, IntentConstant.REQUESTCODE_CUTTING);
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

    private class UserPrivateInfoListener extends NetUtils.Callback<UserPrivateInfo> {

        public UserPrivateInfoListener(Context context) {
            super(context, UserPrivateInfo.class);
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.load_user_info_failed);
        }

        @Override
        public void onNetSuccess(UserPrivateInfo response) {
            if (response == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.load_user_info_failed);
                return;
            }

            if (!TextUtils.isEmpty(response.getData().getHead_img())) {
                mPortraitView.setImageURI(Uri.parse(response.getData().getHead_img()));
            }
            mEditUserNameView.setText(TextUtils.isEmpty(response.getData().getUser_name()) ? UserInfoUtils.getUserName(mContext) : response.getData().getUser_name());
            mNickNameEditView.setText(TextUtils.isEmpty(response.getData().getNikename()) ? UserInfoUtils.getUserNickName(mContext) : response.getData().getNikename());
            String num = response.getData().getMobile();
            mPhoneEditView.setText(TextUtils.isEmpty(num) ? UserInfoUtils.getPhone(mContext) : num);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.load_user_info_failed);
        }
    }

    private class UpLoadHeadImgListener extends NetUtils.Callback<HeadImageUploaderResponse> {

        public UpLoadHeadImgListener(Context context) {
            super(context, HeadImageUploaderResponse.class);
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.upload_head_image_failed);
        }

        @Override
        public void onNetSuccess(HeadImageUploaderResponse response) {
            if (response == null || response.getError()) {
                return;
            }
            String imgUrl = response.getMessage();
            if (!TextUtils.isEmpty(imgUrl)) {
                UserInfoUtils.saveCacheHeadImagePath(UserPrivateInfoActivity.this, imgUrl);
                mPortraitView.setImageURI(Uri.parse(imgUrl));
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.upload_head_image_failed);
        }
    }
}
