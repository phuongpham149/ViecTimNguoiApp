package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.NotifyReport;

import java.util.List;
import java.util.Random;

/**
 * Created by phuong on 22/02/2017.
 */

public class NotifyReportAdapter extends RecyclerView.Adapter<NotifyReportAdapter.ContactHolder> {
    private List<NotifyReport> mNotifyReports;
    private Context mContext;
    private onItemClickListener mListener;
    private int[] mIcons = {R.drawable.ic_user_blue, R.drawable.ic_user_gray, R.drawable.ic_user_green, R.drawable.ic_user_yellow};

    public NotifyReportAdapter(List<NotifyReport> notifys, Context mContext, onItemClickListener listener) {
        this.mNotifyReports = notifys;
        this.mContext = mContext;
        mListener = listener;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, final int position) {
        holder.mTvUserContact.setText(mNotifyReports.get(position).getUsernameReport());
        holder.mTvTime.setText(mNotifyReports.get(position).getTime());
        holder.mTvMessage.setText(mNotifyReports.get(position).getMessage());
        int randomAvarta = (new Random().nextInt(4));
        holder.mImgAvarta.setImageResource(mIcons[randomAvarta]);
        holder.mLlContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.itemClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mNotifyReports == null) ? 0 : mNotifyReports.size();
    }

    public interface onItemClickListener {
        void itemClickListener(int position);
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        ImageView mImgAvarta;
        TextView mTvUserContact;
        TextView mTvMessage;
        TextView mTvTime;
        LinearLayout mLlContactItem;

        public ContactHolder(View itemView) {
            super(itemView);
            mTvUserContact = (TextView) itemView.findViewById(R.id.tvUserContact);
            mTvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
            mImgAvarta = (ImageView) itemView.findViewById(R.id.imgAvartar);
            mLlContactItem = (LinearLayout) itemView.findViewById(R.id.llContactItem);
        }
    }
}
