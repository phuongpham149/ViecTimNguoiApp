package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        holder.mTvTimeCreate.setText(historyPing.getTimeCreated());
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryPingDetailActivity_.intent(mContext).mHistoryPing(historyPing).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryPings == null ? 0 : mHistoryPings.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        LinearLayout mLlItem;
        TextView mTvTimeCreate;

        public ItemHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mLlItem = (LinearLayout) itemView.findViewById(R.id.llHistoryPing);
            mTvTimeCreate = (TextView) itemView.findViewById(R.id.tvTimeCreate);

        }
    }
}
