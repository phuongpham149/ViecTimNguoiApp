package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
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
        holder.mTvDate.setText(item.getDate().toString());
        holder.mTvTitle.setText(item.getTitle());
        holder.mTvDetail.setText(item.getDetail());

        holder.mImgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save new
            }
        });

        holder.mRlNewItem.setOnClickListener(new View.OnClickListener() {
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

        TextView mTvDate;
        TextView mTvTitle;
        TextView mTvDetail;
        CheckBox mImgContact;
        RelativeLayout mRlNewItem;

        public NewsHolder(View itemView) {
            super(itemView);
            mTvDate = (TextView) itemView.findViewById(R.id.tvDateNew);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitleNew);
            mTvDetail = (TextView) itemView.findViewById(R.id.tvDetailNew);
            mImgContact = (CheckBox) itemView.findViewById(R.id.imgNewContact);
            mRlNewItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
        }
    }
}
