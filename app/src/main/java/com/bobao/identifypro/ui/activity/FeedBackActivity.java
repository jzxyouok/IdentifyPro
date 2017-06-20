package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.FeedBackResponse;
import com.bobao.identifypro.domain.NormalMessageResponse;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.exception.HttpException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by you on 2015/6/3.
 */
public class FeedBackActivity extends BaseActivity {
    private ListView mFeedBackLv;
    private List<FeedBackResponse.DataEntity> mData = new LinkedList<>();
    private final int SUB_TYPE_TIME = 0;
    private final int SUB_TYPE_MSG = 1;
    private UserFeedBackAdapter mAdapter;
    private TextView mSendBtn;
    private EditText mSendMsgEt;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_feedback_layout;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.user_feedback);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mFeedBackLv = (ListView) findViewById(R.id.lv_price_content);
        mSendBtn = (TextView) findViewById(R.id.tv_sendmsg);
        mSendMsgEt = (EditText) findViewById(R.id.et_message);
        mAdapter = new UserFeedBackAdapter();
        mFeedBackLv.setAdapter(mAdapter);
        mSendMsgEt.addTextChangedListener(new Watcher());
        setOnClickListener(mSendBtn);
    }

    private void startSendUserMsg(final int pos, String content) {
        updateListViewPartly(pos, STATE.LOADING);
        mStatesMap.put(pos, STATE.LOADING);
        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getSendFeedBackParams(mContext, content), new SendUserMsgListener(mContext, pos));
    }

    /**
     * 用户发送消息+失败重发测试
     */
    enum STATE {
        LOADING, FINISH, FAIL
    }

    private HashMap<Integer, STATE> mStatesMap = new HashMap<>();

    private void updateListViewPartly(int position, STATE state) {
        Log.e("UpdatePartly", "update" + position);
        int firstVisiblePosition = mFeedBackLv.getFirstVisiblePosition();
        int lastVisiblePosition = mFeedBackLv.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = mFeedBackLv.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof ViewHolder) {
                ViewHolder vh = (ViewHolder) view.getTag();
                TextView tv = vh.tv_send_state_test;
                if (state == STATE.LOADING) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("正在加载");
                } else if (state == STATE.FINISH) {
                    tv.setText("加载完成");
                    tv.setVisibility(View.GONE);
                } else if (state == STATE.FAIL) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("加载失败");
                }
            }
        }
    }


    private void startUserFeedBackRequest() {
        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getUserFeedBackParams(mContext), new UserFeedBackListener(mContext));
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {
        startUserFeedBackRequest();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_sendmsg:
                sendMessage();
            default:
                super.onClick(view);
                break;
        }
    }

    private void sendMessage() {
        // 收起软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        String content = mSendMsgEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            DialogUtils.showShortPromptToast(this, "发送内容不能为空,请重新发送");
            return;
        }
        mSendMsgEt.setText("");
        FeedBackResponse.DataEntity dataEntity = new FeedBackResponse.DataEntity();
        dataEntity.setContent(content);
        dataEntity.setSend_type(1);
        dataEntity.setBelong("0");
        if (mData == null) {
            mData = new LinkedList<>();
        }
        mData.add(dataEntity);
        mFeedBackLv.smoothScrollToPosition(mData.size() - 1);
        startSendUserMsg(mData.size() - 1, content);
        mAdapter.notifyDataSetChanged();
    }

    private class UserFeedBackAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData == null ? null : mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(viewGroup.getContext(), R.layout.list_user_feedback, null);
                holder.tv_user_feedback_time = (TextView) view.findViewById(R.id.tv_user_feedback_time);
                holder.ll_server_msg_container = (LinearLayout) view.findViewById(R.id.ll_server_msg_container);
                holder.tv_feedback_from_server = (TextView) view.findViewById(R.id.tv_feedback_from_server);
                holder.ll_user_msg_container = (LinearLayout) view.findViewById(R.id.ll_user_msg_container);
                holder.tv_feedback_from_user = (TextView) view.findViewById(R.id.tv_feedback_from_user);
                holder.portrait = (SimpleDraweeView) view.findViewById(R.id.sdv_portrait);
                //User消息发送状态提示
                holder.tv_send_state_test = (TextView) view.findViewById(R.id.tv_send_state_test);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            FeedBackResponse.DataEntity dataEntity = mData.get(i);
            if (SUB_TYPE_TIME == dataEntity.getSend_type()) {
                holder.tv_user_feedback_time.setVisibility(View.VISIBLE);
                holder.ll_server_msg_container.setVisibility(View.GONE);
                holder.ll_user_msg_container.setVisibility(View.GONE);
                holder.tv_user_feedback_time.setText(dataEntity.getAddtime());
            } else {
                holder.ll_user_msg_container.setTag(i);
                holder.tv_user_feedback_time.setVisibility(View.GONE);
                String sender = dataEntity.getBelong();
                int belong = Integer.parseInt(sender);
                if (belong > 0) {
                    holder.ll_server_msg_container.setVisibility(View.VISIBLE);
                    holder.ll_user_msg_container.setVisibility(View.GONE);
                    holder.tv_feedback_from_server.setText(dataEntity.getContent());
                } else {
                    holder.ll_user_msg_container.setVisibility(View.VISIBLE);
                    holder.ll_server_msg_container.setVisibility(View.GONE);
                    holder.tv_feedback_from_user.setText(dataEntity.getContent());
                    holder.portrait.setImageURI(Uri.parse(UserInfoUtils.getCacheHeadImagePath(viewGroup.getContext())));
                }

                if (mStatesMap.get(i) == STATE.LOADING) {
                    holder.tv_send_state_test.setVisibility(View.VISIBLE);
                    holder.tv_send_state_test.setText("正在加载");
                } else if (mStatesMap.get(i) == STATE.FINISH) {
                    holder.tv_send_state_test.setVisibility(View.GONE);
                    holder.tv_send_state_test.setText("");
                } else if (mStatesMap.get(i) == STATE.FAIL) {
                    holder.tv_send_state_test.setVisibility(View.VISIBLE);
                    holder.tv_send_state_test.setText("加载失败");
                    holder.ll_user_msg_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO
                            int clickPoS = (int) view.getTag();
                            FeedBackResponse.DataEntity data = mData.get(clickPoS);
                            if (mStatesMap.get(clickPoS) == STATE.FAIL) {
                                //如果失败点击重新发送
                                startSendUserMsg(clickPoS, data.getContent());
                            }
                        }
                    });
                }
            }
            return view;
        }
    }

    private class ViewHolder {
        TextView tv_user_feedback_time;
        LinearLayout ll_server_msg_container;
        TextView tv_feedback_from_server;
        LinearLayout ll_user_msg_container;
        TextView tv_feedback_from_user;
        TextView tv_send_state_test;
        SimpleDraweeView portrait;
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

    private class UserFeedBackListener extends NetUtils.Callback<FeedBackResponse> {

        public UserFeedBackListener(Context context) {
            super(context, FeedBackResponse.class);
        }

        @Override
        public void onNetSuccess(FeedBackResponse response) {
            if (response == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.load_info_failed);
                return;
            }
            if (response.getError()) {
            } else {
                mData = response.getData();
                if (mData != null) {
                    mAdapter.notifyDataSetChanged();
                    for (int i = 0; i < mData.size(); i++) {
                        mStatesMap.put(i, STATE.FINISH);
                    }
                    mFeedBackLv.setSelection(mData.size() - 1);
                }
            }
        }
    }

    private class SendUserMsgListener extends NetUtils.Callback<NormalMessageResponse> {
        private int mPos;

        public SendUserMsgListener(Context context, int pos) {
            super(context, NormalMessageResponse.class);
            mPos = pos;
        }

        @Override
        public void onConvertFailed(String json) {
            updateListViewPartly(mPos, STATE.FAIL);
            mStatesMap.put(mPos, STATE.FAIL);
            DialogUtils.showShortPromptToast(mContext, R.string.send_msg_fail);
        }

        @Override
        public void onNetSuccess(NormalMessageResponse response) {
            if (response == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.feedback_content_empty);
                return;
            }
            updateListViewPartly(mPos, STATE.FINISH);
            mStatesMap.put(mPos, STATE.FINISH);
            DialogUtils.showShortPromptToast(mContext, R.string.send_msg_success);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            updateListViewPartly(mPos, STATE.FAIL);
            mStatesMap.put(mPos, STATE.FAIL);
            DialogUtils.showShortPromptToast(mContext, R.string.send_msg_fail);
        }
    }

    /**
     * 对3个输入框进行监听
     */
    private class Watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String message = mSendMsgEt.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                mSendBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                mSendBtn.setBackgroundResource(R.drawable.selector_auth_code_btn);
                mSendBtn.setEnabled(true);
            } else {
                mSendBtn.setTextColor(ContextCompat.getColor(mContext, R.color.black3));
                mSendBtn.setBackgroundResource(R.drawable.bg_content_corner_3_gray9);
                mSendBtn.setEnabled(false);
            }
        }
    }
}
