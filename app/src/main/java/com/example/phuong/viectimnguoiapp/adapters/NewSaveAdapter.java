package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.NewSave;

import java.util.List;

/**
 * Created by phuong on 21/02/2017.
 */

public class NewSaveAdapter extends RecyclerView.Adapter<NewSaveAdapter.NewsHolder> {
    private List<NewSave> mNews;
    private Context mContext;
    private RealmHelper mData;

    private onItemClickListener mListener;

    public NewSaveAdapter(List<NewSave> mNews, Context mContext, onItemClickListener listener) {
        this.mNews = mNews;
        this.mContext = mContext;
        mListener = listener;
        mData = new RealmHelper(mContext);
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_save_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {
        final NewSave item = mNews.get(position);
        holder.mTvName.setText(mContext.getResources().getStringArray(R.array.name_category)[Integer.parseInt(item.getIdCat()) - 1]);
        holder.mTvTime.setText(item.getTimeCreated());
        holder.mTvAddress.setText(item.getAddress() + ", " + mContext.getResources().getStringArray(R.array.name_district)[Integer.parseInt(item.getIdDistrict()) - 1]);
        holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Right
        holder.mSwipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.mSwipeLayout.findViewById(R.id.menu_wrapper));

        holder.mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.deleteNewSave(mNews.get(position).getId());
                notifyDataSetChanged();
            }
        });
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
        RelativeLayout mRlItem;
        ImageView mImgDelete;
        SwipeLayout mSwipeLayout;

        public NewsHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
            mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            mImgDelete = (ImageView) itemView.findViewById(R.id.imgDel);
            mRlItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
        }
    }
}
