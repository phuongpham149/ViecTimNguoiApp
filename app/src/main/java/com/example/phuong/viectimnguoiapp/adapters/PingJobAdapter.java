package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.Ping;

import java.util.List;
import java.util.Random;

/**
 * Created by phuong on 27/04/2017.
 */

public class PingJobAdapter extends RecyclerView.Adapter<PingJobAdapter.NewsHolder> {
    private List<Ping> pings;
    private Context mContext;
    private int[] mIcons = {R.drawable.ic_user_blue, R.drawable.ic_user_gray, R.drawable.ic_user_green, R.drawable.ic_user_yellow};


    private onItemClickListener mListener;

    public PingJobAdapter(List<Ping> pings, Context mContext, onItemClickListener listener) {
        this.pings = pings;
        this.mContext = mContext;
        mListener = listener;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ping_job, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {
        final Ping item = pings.get(position);
        holder.mTvName.setText(item.getUsername());
        holder.mTvPrice.setText(item.getPrice());
        int randomAvarta = (new Random().nextInt(4));
        holder.mImgAvarta.setImageResource(mIcons[randomAvarta]);

        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.itemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (pings == null) ? 0 : pings.size();
    }

    public interface onItemClickListener {
        void itemClickListener(int position);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        TextView mTvPrice;
        ImageView mImgAvarta;
        RelativeLayout mRlItem;

        public NewsHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            mImgAvarta = (ImageView) itemView.findViewById(R.id.imgAvartar);
            mRlItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
        }
    }
}
