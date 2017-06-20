package com.bobao.identifypro.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.ui.dialog.ChooseCollectionDialog;
import com.bobao.identifypro.ui.dialog.ChooseDialog;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.ui.dialog.DebugDialog;
import com.bobao.identifypro.ui.dialog.EditInfoDialog;
import com.bobao.identifypro.ui.dialog.ExitDialog;
import com.bobao.identifypro.ui.dialog.InviteCodeDialog;
import com.bobao.identifypro.ui.dialog.NetworkUnConnectedDialog;
import com.bobao.identifypro.ui.dialog.ProgressDialog;
import com.bobao.identifypro.ui.dialog.RateDialog;
import com.bobao.identifypro.ui.dialog.RefundDetailDialog;
import com.bobao.identifypro.ui.dialog.ShowConfirmDialog;
import com.bobao.identifypro.ui.dialog.UpdateDialog;
import com.umeng.update.UpdateResponse;

public class DialogUtils {
    private static Toast mShortPromptToast;
    private static Toast mLongPromptToast;

    public static void showShortPromptToast(Context context, int resid) {
        if (mShortPromptToast == null) {
            mShortPromptToast = Toast.makeText(context, resid, Toast.LENGTH_SHORT);
        }
        mShortPromptToast.setText(resid);
        mShortPromptToast.show();
    }

    public static void showShortPromptToast(Context context, String res) {
        if (mShortPromptToast == null) {
            mShortPromptToast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        }
        mShortPromptToast.setText(res);
        mShortPromptToast.show();
    }

    public static void showLongPromptToast(Context context, String... res) {
        StringBuilder content = new StringBuilder();
        for (String string : res) {
            content.append(string);
        }
        if (mLongPromptToast == null) {
            mLongPromptToast = Toast.makeText(context, content.toString(), Toast.LENGTH_SHORT);
        }
        mLongPromptToast.setText(content.toString());
        mLongPromptToast.show();
    }

    public static void showLongPromptToast(Context context, int resid) {
        if (mLongPromptToast == null) {
            mLongPromptToast = Toast.makeText(context, resid, Toast.LENGTH_LONG);
        }
        mLongPromptToast.setText(resid);
        mLongPromptToast.show();
    }

    /**
     * 弹出显示进度的dialog
     */
    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出显示进度的dialog
     */
    public static ProgressDialog showProgressDialog(Context context, String title) {
        ProgressDialog dialog = new ProgressDialog(context, title);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出调试信息对话框
     */
    public static DebugDialog showDebugInfoDialog(Context context, String info) {
        DebugDialog dialog = new DebugDialog(context, info);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出调试信息对话框
     */
    public static DebugDialog showDebugInfoDialog(Context context) {
        DebugDialog dialog = new DebugDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出选择藏品的dialog
     */
    public static ChooseCollectionDialog showChooseCollectionDialog(Context context, String serviceType, String serviceTypeName) {
        ChooseCollectionDialog dialog = new ChooseCollectionDialog(context, serviceType, serviceTypeName);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出输入对话框
     */
    public static EditInfoDialog showEditDialog(Context context, EditInfoDialog.OnConfirmListener listener, int titleRes,
                                                int hintRes) {
        EditInfoDialog dialog = new EditInfoDialog(context, listener, titleRes, hintRes);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出无网是设置网络的dialog
     */
    public static NetworkUnConnectedDialog showNetworkUnConnectedDialog(Context context, View.OnClickListener onClickListener) {
        NetworkUnConnectedDialog dialog = new NetworkUnConnectedDialog(context, onClickListener);
        dialog.show();
        return dialog;
    }

    @NonNull
    public static void showConfirmDialog(Context context, int strTitleResId, int strMsgResId, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitleResId).setMessage(strMsgResId)
                .setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, null).show();
    }

    @NonNull
    public static void showConfirmDialog(Context context, String strTitle, String strMsg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitle).setMessage(strMsg)
                .setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, null).show();
    }

    @NonNull
    public static void showConfirmDialog(Context context, int strTitleResId, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitleResId).setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, null).show();
    }

    /**
     * 弹出选择钱币对话框
     */
    public static ChooseDialog showChooseDialog(Context context) {
        ChooseDialog dialog = new ChooseDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 选择照片对话框
     */
    @NonNull
    public static Dialog showPickPhotoDialog(Context context, int layoutId, int selectCameraId, int selectPhotoId,
                                             View.OnClickListener takePhotoListener,
                                             View.OnClickListener pickPhotoListener) {
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(true);
        dialog.show();

        View mSelectCamera = view.findViewById(selectCameraId);
        View mSelectPhoto = view.findViewById(selectPhotoId);
        View mCancel = view.findViewById(R.id.ll_select_cancel);

        if (mSelectCamera != null) {
            mSelectCamera.setOnClickListener(takePhotoListener);
        }
        if (mSelectPhoto != null) {
            mSelectPhoto.setOnClickListener(pickPhotoListener);
        }
        if (mCancel != null) {
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        return dialog;
    }

    @NonNull
    public static CommonDialog showCommonDialog(Context context, String strTitle, String strContent, String strNeg, String strPos,
                                                View.OnClickListener mSubmitListener, View.OnClickListener mCancelListener) {
        CommonDialog dialog = new CommonDialog(context, strTitle, strContent, strNeg, strPos, mSubmitListener, mCancelListener);
        dialog.show();
        return dialog;
    }

    @NonNull
    public static ExitDialog showExitDialog(Context context, String strTitle, String strContent, String strNeg, String strPos,
                                            View.OnClickListener mSubmitListener, View.OnClickListener mCancelListener) {
        ExitDialog dialog = new ExitDialog(context, strTitle, strNeg, strPos, mSubmitListener, mCancelListener);
        dialog.show();
        return dialog;
    }

    @NonNull
    public static void showNOCancelConfirmDialog(Context context, String strTitle, String strMsg, final DialogInterface.OnClickListener listener) {
        if (listener != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(strTitle).setMessage(strMsg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.positive, listener).show();
        }
    }

    @NonNull
    public static ShowConfirmDialog showConfirmDialog(Context context, String strTitle, String strContent) {
        ShowConfirmDialog dialog = new ShowConfirmDialog(context, strTitle, strContent);
        dialog.show();
        return dialog;
    }

    /**
     * 检查版本更新
     */
    @NonNull
    public static UpdateDialog showUpdateDialog(Context context, UpdateResponse updateInfo, String newVersion, String content) {
        UpdateDialog dialog = new UpdateDialog(context, updateInfo, newVersion, content);
        dialog.show();
        return dialog;
    }

    /**
     * 评价
     */
    @NonNull
    public static RateDialog showRateDialog(Context context) {
        RateDialog dialog = new RateDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 查看退款
     */
    public static RefundDetailDialog showRefundDetailDialog(Context context, String content) {
        RefundDetailDialog refundDetailDialog = new RefundDetailDialog(context, content);
        refundDetailDialog.show();
        return refundDetailDialog;
    }

    /**
     * 发送邀请码成功
     */
    public static InviteCodeDialog showInviteCodeDialog(Context context) {
        InviteCodeDialog inviteCodeDialog = new InviteCodeDialog(context);
        inviteCodeDialog.show();
        return inviteCodeDialog;
    }
}
