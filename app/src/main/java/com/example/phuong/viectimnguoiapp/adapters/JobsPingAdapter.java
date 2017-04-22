package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.HistoryPingDetailActivity_;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;

import java.util.List;

/**
 * Created by asiantech on 27/03/2017.
 */
public class JobsPingAdapter extends RecyclerView.Adapter<JobsPingAdapter.ItemHolder> {
    private List<HistoryPing> mHistoryPings;
    private Context mContext;

    public JobsPingAdapter(List<HistoryPing> mHistoryPings, Context mContext) {
        this.mHistoryPings = mHistoryPings;
        this.mContext = mContext;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_ping_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final HistoryPing historyPing = mHistoryPings.get(position);
        holder.mTvTitle.setText(historyPing.getTitlePost());
        holder.mTvTimeCreate.setText(historyPing.getTimeDeadline());
        holder.mTvAddress.setText(historyPing.getAddress() + ", " + historyPing.getNameDistrict());
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryPingDetailActivity_.intent(mContext).mIdPost(historyPing.getIdPost()).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryPings == null ? 0 : mHistoryPings.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        RelativeLayout mLlItem;
        TextView mTvTimeCreate;
        TextView mTvAddress;

        public ItemHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mTvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            mLlItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
            mTvTimeCreate = (TextView) itemView.findViewById(R.id.tvTimeCreate);

        }
    }
}
