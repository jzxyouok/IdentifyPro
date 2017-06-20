package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.application.IdentifyProApplication;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.NormalMessageResponse;
import com.bobao.identifypro.domain.PriceQueryContentResponse;
import com.bobao.identifypro.ui.adapter.EvaluateExpertAdapter;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by you on 2015/6/17.
 */
public class PriceQueryContentActivity extends BaseActivity {
    private String mPrice;
    private String mReport;
    private String mPhotoUrl;
    private String mFrom;
    private String mTo;
    private String mGoodsId;

    private String mAppointmentId;
    private String mHonor;
    private String mExpertName;

    private ListView mListView;
    private List<PriceQueryContentResponse.DataEntity> mData;
    private final int SUB_TYPE_TIME = 0;
    private final int SUB_TYPE_MSG = 1;
    private PriceQueryContentAdapter mAdapter;
    private EditText mSendEditView;
    private String mQueryName;

    private String mQueryHead;

    /**
     * 用户发送消息+失败重发测试
     */
    enum STATE {
        LOADING, FINISH, FAIL
    }

    private HashMap<Integer, STATE> mStatesMap = new HashMap<>();

    private LinearLayout mCommitToOtherLL;
    private Button mCommitEvaluate;
    private boolean mIsMyOrderFlags;

    private LinearLayout mEvaluateView;
    private RatingBar mExpertScore;
    private EditText mEvaluateEt;

    private static final String EVALUATE_TO_EXPERT = "3";
    private String mQueryExpertId;

    private RecyclerView mRecyclerView;
    private EvaluateExpertAdapter mEvaluateAdapter;

    @Override
    protected void getIntentData() {
        mIsMyOrderFlags = (boolean) IdentifyProApplication.getIntentData(IntentConstant.IS_MY_ORDER_FLAGS);
        mQueryExpertId = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_EXPERT_ID);
        mPrice = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOODS_PRICE);
        mReport = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_REPORT);
        if (!TextUtils.isEmpty(mReport)) {
            mReport = StringUtils.trimHTMLTag(mReport);
        }
        mPhotoUrl = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOODS_PHOTO);
        mFrom = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOODS_FROM);
        mTo = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOODS_TO);
        mGoodsId = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOODS_ID);
        mQueryName = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOOD_NAME);
        mQueryHead = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOODS_HEAD);

        mAppointmentId = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_GOODS_ID);
        mHonor = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_EXPERT_HONOR);
        mExpertName = (String) IdentifyProApplication.getIntentData(IntentConstant.QUERY_EXPERT_NAME);

        // 及时回收
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_EXPERT_ID);
        IdentifyProApplication.removeIntentData(IntentConstant.IS_MY_ORDER_FLAGS);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOODS_STATE);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOODS_PRICE);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_REPORT);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOODS_PHOTO);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOODS_FROM);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOODS_TO);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOODS_ID);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOODS_HEAD);
        IdentifyProApplication.removeIntentData(IntentConstant.QUERY_GOOD_NAME);
    }

    @Override
    protected void initData() {
//        mFrom = UserInfoUtils.getUserId(mContext);
        mData = new LinkedList<>();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_price_query_content;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(mIsMyOrderFlags ? R.string.evaluate_to_expert : R.string.enquiry);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        SimpleDraweeView photoView = (SimpleDraweeView) findViewById(R.id.sdv_picture);
        TextView priceView = (TextView) findViewById(R.id.tv_order_price);
        TextView reportView = (TextView) findViewById(R.id.tv_content);
        TextView nameView = (TextView) findViewById(R.id.tv_expert_name);

        if (!TextUtils.isEmpty(mPhotoUrl)) {
            photoView.setImageURI(Uri.parse(mPhotoUrl));
        }
        priceView.setText(StringUtils.getString(getString(R.string.identify_id), mAppointmentId));
        reportView.setText(mHonor);
        nameView.setText(mExpertName);

        mListView = (ListView) findViewById(R.id.lv_price_content);
        mSendEditView = (EditText) findViewById(R.id.et_message);
        View sendView = findViewById(R.id.iv_send);
        View mOrder = findViewById(R.id.ll_order);

        mExpertScore = (RatingBar) findViewById(R.id.rb_label);
        mEvaluateEt = (EditText) findViewById(R.id.evaluate_et);
        mEvaluateView = (LinearLayout) findViewById(R.id.ll_evaluate_to_expert);
        mCommitToOtherLL = (LinearLayout) findViewById(R.id.commit_to_other);
        mCommitEvaluate = (Button) findViewById(R.id.commit_evaluate_btn);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_evaluate_tag);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mCommitToOtherLL.setVisibility(mIsMyOrderFlags ? View.GONE : View.VISIBLE);
        mCommitEvaluate.setVisibility(mIsMyOrderFlags ? View.VISIBLE : View.GONE);

        mEvaluateView.setVisibility(mIsMyOrderFlags ? View.VISIBLE : View.GONE);
        mListView.setVisibility(mIsMyOrderFlags ? View.GONE : View.VISIBLE);
        mSendEditView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mSendEditView.setSingleLine(false);
        mSendEditView.setHorizontallyScrolling(false);
        setOnClickListener(sendView, mOrder, mCommitEvaluate);

        mEvaluateEt.addTextChangedListener(new Watcher());
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
        mAdapter = new PriceQueryContentAdapter();
        mEvaluateAdapter = new EvaluateExpertAdapter(mContext, mEvaluateEt);
        mListView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mEvaluateAdapter);
        startPriceQueryRequest();
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
            case R.id.iv_send:
                String content = mSendEditView.getText().toString().trim();
                mSendEditView.setText("");
                if (!TextUtils.isEmpty(content)) {
                    PriceQueryContentResponse.DataEntity dataEntity = new PriceQueryContentResponse.DataEntity();
                    dataEntity.setContent(content);
                    dataEntity.setType(1);
                    dataEntity.setIsme(1);
                    mData.add(dataEntity);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(mData.size() - 1);
                    startSendUserMsg(mData.size() - 1, content);
                } else {
                    DialogUtils.showShortPromptToast(mContext, "发送内容不能为空,请重新发送");
                }
                break;
            case R.id.ll_order:
//                //询价跳转到订单详情
//                Intent intent = new Intent(mContext, OrderDetailActivity.class);
//                intent.putExtra(IntentConstant.ORDER_ID, mGoodsId);
//                ActivityUtils.jump(mContext, intent);
                break;
            case R.id.commit_evaluate_btn:
                //提交对专家的评价
                commit();
            default:
                super.onClick(view);
                break;
        }
    }


    private void commit() {
        //判断用户是否登录
        if (!UserInfoUtils.checkUserLogin(mContext)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            jump(intent);
        } else {
            // 获取评价内容
            String content = mEvaluateEt.getText().toString().trim();
            int score = mExpertScore.getProgress();
            if (!TextUtils.isEmpty(content)) {
                //评论实时刷新，需要关闭缓存功能
                HttpUtils.sHttpCache.setEnabled(HttpRequest.HttpMethod.GET, false);
                // 提交评价
                NetUtils.getInstance(false).post(mContext, NetConstant.getEvaluateParams(mContext, mAppointmentId, content, score), new CommentListener(mContext));
                HashMap<String, String> map = new HashMap<>();
                map.put(UmengConstants.KEY_ORDER_DETAIL_COMMENT_COMMIT, content);
                UmengUtils.onEvent(mContext, EventEnum.OrderDetailCommentsCommit, map);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.content_not_empty);
            }
        }
        // 收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEvaluateEt.getWindowToken(), 0);
    }

    private void startSendUserMsg(final int pos, String content) {
        try {
            content = URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        updateListViewPartly(pos, STATE.LOADING);
        mStatesMap.put(pos, STATE.LOADING);
        NetUtils.getInstance(false).get(mContext, NetConstant.getSendFeedBackParams(mContext, content, mTo, mGoodsId), new SendUserMsgListener(mContext, pos));
    }

    private void startPriceQueryRequest() {
        NetUtils.getInstance(false).get(mContext, NetConstant.getPriceQueryContentParams(mContext, mFrom, mTo, mGoodsId), new PriceQueryContentListener(mContext));
    }

    private void updateListViewPartly(int position, STATE state) {
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int lastVisiblePosition = mListView.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = mListView.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof ViewHolder) {
                ViewHolder vh = (ViewHolder) view.getTag();
                TextView tv = vh.tv_send_state_test;
                if (state == STATE.LOADING) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("发送中...");
                } else if (state == STATE.FINISH) {
                    tv.setText("发送成功");
                    tv.setVisibility(View.GONE);
                } else if (state == STATE.FAIL) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("发送失败");
                }
            }
        }
    }

    class PriceQueryContentAdapter extends BaseAdapter {

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
                view = View.inflate(mContext, R.layout.list_user_feedback, null);
                holder.portrait = (SimpleDraweeView) view.findViewById(R.id.sdv_portrait);
                holder.tv_user_feedback_time = (TextView) view.findViewById(R.id.tv_user_feedback_time);
                holder.ll_server_msg_container = (LinearLayout) view.findViewById(R.id.ll_server_msg_container);
                holder.tv_feedback_from_server = (TextView) view.findViewById(R.id.tv_feedback_from_server);
                holder.ll_user_msg_container = (LinearLayout) view.findViewById(R.id.ll_user_msg_container);
                holder.tv_feedback_from_user = (TextView) view.findViewById(R.id.tv_feedback_from_user);
                //User消息发送状态提示
                holder.tv_send_state_test = (TextView) view.findViewById(R.id.tv_send_state_test);

                holder.ask_photo = (SimpleDraweeView) view.findViewById(R.id.sdv_ask_photo);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            PriceQueryContentResponse.DataEntity dataEntity = mData.get(i);
            if (SUB_TYPE_TIME == dataEntity.getType()) {
                holder.tv_user_feedback_time.setVisibility(View.VISIBLE);
                holder.ll_server_msg_container.setVisibility(View.GONE);
                holder.ll_user_msg_container.setVisibility(View.GONE);
                holder.tv_user_feedback_time.setText(dataEntity.getAddtime());
//                holder.ask_photo.setImageURI(Uri.parse(mQueryHead));
            } else {
                holder.ll_user_msg_container.setTag(i);
                holder.tv_user_feedback_time.setVisibility(View.GONE);

                int belong = dataEntity.getIsme();
                if (belong == 0) {//对方的消息
                    holder.ll_server_msg_container.setVisibility(View.VISIBLE);
                    holder.ll_user_msg_container.setVisibility(View.GONE);
                    holder.ask_photo.setImageURI(Uri.parse(dataEntity.getHead_img()));
                    holder.tv_feedback_from_server.setText(dataEntity.getContent());
                } else {//自己发的消息
                    holder.ll_user_msg_container.setVisibility(View.VISIBLE);
                    holder.ll_server_msg_container.setVisibility(View.GONE);
                    holder.tv_feedback_from_user.setText(dataEntity.getContent());
                    holder.portrait.setImageURI(Uri.parse(UserInfoUtils.getUserHeadImage(mContext)));
//                    holder.portrait.setImageURI(Uri.parse(dataEntity.getHead_img()));
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

                            int clickPoS = (int) view.getTag();
                            PriceQueryContentResponse.DataEntity data = mData.get(clickPoS);
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
        SimpleDraweeView portrait;
        TextView tv_user_feedback_time;
        LinearLayout ll_server_msg_container;
        TextView tv_feedback_from_server;
        LinearLayout ll_user_msg_container;
        TextView tv_feedback_from_user;
        TextView tv_send_state_test;
        SimpleDraweeView ask_photo;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mEvaluateEt.setText(mEvaluateAdapter.getmChoiceStr());
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    private class PriceQueryContentListener extends NetUtils.Callback<PriceQueryContentResponse> {

        public PriceQueryContentListener(Context context) {
            super(context, PriceQueryContentResponse.class);
        }

        @Override
        public void onNetSuccess(PriceQueryContentResponse response) {
            if (response == null) {
                Toast.makeText(PriceQueryContentActivity.this, "获取鉴宝信息失败..", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.getError()) {
                //TODO 数据获取失败 data.message
            } else {
                mData = response.getData();
                mAdapter.notifyDataSetChanged();
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

    private class CommentListener extends NetUtils.Callback<NormalMessageResponse> {
        public CommentListener(Context context) {
            super(context, NormalMessageResponse.class);
        }

        @Override
        public void onNetSuccess(NormalMessageResponse response) {
            if (!response.getError()) {
                DialogUtils.showShortPromptToast(mContext, getString(R.string.comment_success));
                finish();
            }
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
            String content = mEvaluateEt.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                mCommitEvaluate.setBackgroundResource(R.drawable.bg_button_corner_4_orange_yellow);
                mCommitEvaluate.setEnabled(true);
            } else {
                mCommitEvaluate.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mCommitEvaluate.setEnabled(false);
            }
        }
    }
}
