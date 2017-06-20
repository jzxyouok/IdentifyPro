package com.bobao.identifypro.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.dialog.EditInfoDialog;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;

/**
 * 用户充值额度选择页面
 */
public class UserRechargeSelectActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private float[] RECHARGE_VALUES = {
            100, 200, 300, 500, 1000
    };
    private int[] RECHARGE_IMG_IDS = {
            R.drawable.recharge_100,
            R.drawable.recharge_200,
            R.drawable.recharge_300,
            R.drawable.recharge_500,
            R.drawable.recharge_1000
    };
    private final int TYPE_NORMAL = 0;
    private final int TYPE_OTHER = 1;
    private UserRechargeValuesAdapter mAdapter;
    private int mSelectedPos = -1;
    private float mRechargeValue;
    private String mUserId;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra(IntentConstant.USER_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_recharge_select;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.user_recharge);
        setOnClickListener(backView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected void initContent() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_recharge_values);
        mAdapter = new UserRechargeValuesAdapter();
        GridLayoutManager gm = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gm);
        mRecyclerView.setAdapter(mAdapter);
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

    private class UserRechargeValuesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recharge_value_select, null);

            return new ValueViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ValueViewHolder vHolder = (ValueViewHolder) holder;
            if (getItemViewType(position) == TYPE_NORMAL) {
                vHolder.img_recharge_value.setImageResource(RECHARGE_IMG_IDS[position]);
            } else {
                vHolder.img_recharge_value.setImageResource(R.drawable.recharge_other);
            }

            vHolder.img_recharge_value.setTag(position);
            vHolder.img_recharge_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = (int) view.getTag();
                    mSelectedPos = i;
                    if (getItemViewType(mSelectedPos) == TYPE_OTHER) {
                        DialogUtils.showEditDialog(mContext, new OnConfirmClickListener(), R.string.edit_info_title, R.string.edit_info_hint);
                        mSelectedPos = -1;
                        mRechargeValue = 0;
                    } else {
                        mRechargeValue = RECHARGE_VALUES[mSelectedPos];
                        if (mRechargeValue == 0) {
                            DialogUtils.showShortPromptToast(mContext, R.string.set_recharge_money);
                            return;
                        } else {
                            Intent intent = new Intent(UserRechargeSelectActivity.this, UserRechargeActivity.class);
                            intent.putExtra(IntentConstant.USER_ID, mUserId);
                            intent.putExtra(IntentConstant.USER_RECHARG_VALUE, StringUtils.getString(mRechargeValue));
                            jump(intent);
                            finish();
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return RECHARGE_VALUES.length + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == RECHARGE_VALUES.length) {
                return TYPE_OTHER;
            }
            return TYPE_NORMAL;
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

    private class ValueViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_recharge_value;

        public ValueViewHolder(View itemView) {
            super(itemView);
            img_recharge_value = (ImageView) itemView.findViewById(R.id.img_recharge_value);
        }
    }

    private class OnConfirmClickListener implements EditInfoDialog.OnConfirmListener {

        @Override
        public void onConfirm(String text) {
            try {
                mRechargeValue = Float.parseFloat(text.trim());
            } catch (NumberFormatException e) {
                DialogUtils.showShortPromptToast(mContext, R.string.input_correct_money);
                mRechargeValue = 0f;
            }

            if (mRechargeValue == 0) {
                DialogUtils.showShortPromptToast(mContext, R.string.set_recharge_money);
            } else {
                Intent intent = new Intent(UserRechargeSelectActivity.this, UserRechargeActivity.class);
                intent.putExtra(IntentConstant.USER_ID, mUserId);
                intent.putExtra(IntentConstant.USER_RECHARG_VALUE, StringUtils.getString(mRechargeValue));
                startActivity(intent);
                finish();
            }
        }
    }
}
