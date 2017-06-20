package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.facebook.drawee.view.SimpleDraweeView;

public class ServiceProcessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private int[] mImgs = new int[]{};
    private String[] mProcess = new String[]{};
    private String mServiceTypeName;

    public ServiceProcessAdapter(Context mContext,String mServiceTypeName) {
        this.mContext = mContext;
        this.mServiceTypeName = mServiceTypeName;
        mImgs = AppConstant.SERVICE_PROCESS_IMG;
        mProcess = mContext.getResources().getStringArray(R.array.service_process);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(View.inflate(mContext, R.layout.list_item_service_process, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentHolder contentHolder = (ContentHolder) holder;
        if (mImgs.length > 0 && mProcess.length > 0) {
            contentHolder.sdv_service_type.setImageResource(mImgs[position]);
            if (position == 3 && mServiceTypeName.equals(AppConstant.SERVICE_TYPE_NAME[1])){
                contentHolder.tv_service_process.setText(R.string.identify_type_video);
                return;
            }
            contentHolder.tv_service_process.setText(mProcess[position]);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mProcess.length;
    }

    private class ContentHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView sdv_service_type;
        private TextView tv_service_process;

        public ContentHolder(View itemView) {
            super(itemView);
            sdv_service_type = (SimpleDraweeView) itemView.findViewById(R.id.sdv_service_type);
            tv_service_process = (TextView) itemView.findViewById(R.id.tv_service_process);
        }
    }
}
