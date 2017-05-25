package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Random;

/**
 * Created by phuong on 27/04/2017.
 */

public class HistoryPingJobAdapter extends RecyclerView.Adapter<HistoryPingJobAdapter.NewsHolder> {
    private List<Ping> pings;
    private Context mContext;
    private int[] mIcons = {R.drawable.ic_user_blue, R.drawable.ic_user_gray, R.drawable.ic_user_green, R.drawable.ic_user_yellow};
    private DatabaseReference user;

    private boolean isContact = false;

    public HistoryPingJobAdapter(List<Ping> pings, Context mContext) {
        this.pings = pings;
        this.mContext = mContext;
        user = FirebaseDatabase.getInstance().getReference("/users");
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_ping_job, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsHolder holder, final int position) {
        final Ping item = pings.get(position);
        holder.mTvName.setText(item.getUsername());
        holder.mTvPrice.setText(item.getPrice());
        int randomAvarta = (new Random().nextInt(4));
        holder.mImgAvarta.setImageResource(mIcons[randomAvarta]);
        if (item.getChoice().equals("true")) {
            holder.mChkContact.setChecked(true);
        } else {
            holder.mChkContact.setChecked(false);
            holder.mChkContact.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (pings == null) ? 0 : pings.size();
    }



    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        TextView mTvPrice;
        ImageView mImgAvarta;
        CheckBox mChkContact;
        RelativeLayout mRlItem;

        public NewsHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            mImgAvarta = (ImageView) itemView.findViewById(R.id.imgAvartar);
            mRlItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
            mChkContact = (CheckBox) itemView.findViewById(R.id.chkContact);
        }
    }
}
