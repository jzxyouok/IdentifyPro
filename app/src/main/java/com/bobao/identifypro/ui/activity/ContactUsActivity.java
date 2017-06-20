package com.bobao.identifypro.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.UmengUtils;

/**
 * Created by you on 2015/6/16.
 */
public class ContactUsActivity extends BaseActivity {
    TextView mContactUsTelView;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.contact_us);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mContactUsTelView = (TextView) findViewById(R.id.tv_contact_us_tel);
        setOnClickListener(mContactUsTelView);
        onScroll();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_contact_us_tel:
                ActivityUtils.makeCallIntent(mContext, mContactUsTelView.getText().toString().trim().substring(3, mContactUsTelView.getText().toString().trim().length()));
                break;
            default:
                break;
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
