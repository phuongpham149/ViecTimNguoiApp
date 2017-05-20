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
import com.example.phuong.viectimnguoiapp.objects.NewItem;

import java.util.List;

/**
 * Created by phuong on 21/02/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private List<NewItem> mNews;
    private Context mContext;
    private int[] mImages = {R.drawable.img_sua_dien, R.drawable.img_lau_nha, R.drawable.img_giat_quan_ao, R.drawable.img_sua_ong_nuoc,R.drawable.img_don_nha_cua,R.drawable.img_ui_quan_ao};

    private onItemClickListener mListener;

    public NewsAdapter(List<NewItem> mNews, Context mContext, onItemClickListener listener) {
        this.mNews = mNews;
        this.mContext = mContext;
        mListener = listener;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {
        final NewItem item = mNews.get(position);
        holder.mTvName.setText(mContext.getResources().getStringArray(R.array.name_category)[Integer.parseInt(item.getIdCat()) - 1]);
        holder.mTvTime.setText(item.getTimeCreated());
        holder.mTvAddress.setText(item.getAddress() + ", " + mContext.getResources().getStringArray(R.array.name_district)[Integer.parseInt(item.getIdDistrict()) - 1]);
        holder.mImgCategory.setImageResource(mImages[Integer.parseInt(item.getIdCat())-1]);
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.itemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mNews == null) ? 0 : mNews.size();
    }

    public interface onItemClickListener {
        void itemClickListener(int position);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        TextView mTvAddress;
        TextView mTvTime;
        LinearLayout mRlItem;
        ImageView mImgCategory;

        public NewsHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
            mRlItem = (LinearLayout) itemView.findViewById(R.id.rlNewItem);
            mImgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
        }
    }
}
