package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.MenuItem;
import com.example.phuong.viectimnguoiapp.utils.Constant;

import java.util.List;

/**
 * Created by phuong on 20/02/2017.
 */

public class SettingMenuAdapter extends RecyclerView.Adapter {
    private static final int MENU_HEADER = 0;
    private static final int MENU_ITEM_INFORMATION = 1;
    private static final int MENU_FOOTER = 2;
    protected final Context mContext;
    private final List mItems;
    SharedPreferences userLogin;
    private itemClick mListener;

    public SettingMenuAdapter(List mItems, Context mContext,itemClick listener) {
        this.mItems = mItems;
        this.mContext = mContext;
        mListener = listener;
        userLogin = mContext.getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MENU_HEADER:
                View header = inflater.inflate(R.layout.menu_header, parent, false);
                viewHolder = new HeaderHolder(header);
                break;
            case MENU_FOOTER:
                View footer = inflater.inflate(R.layout.menu_footer, parent, false);
                viewHolder = new FooterHolder(footer);
                break;
            case MENU_ITEM_INFORMATION:
                View itemInfor = inflater.inflate(R.layout.menu_item, parent, false);
                viewHolder = new ItemHolder(itemInfor);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case MENU_HEADER:
                String username = userLogin.getString(Constant.NAME_USER_LOGIN, "");
                HeaderHolder mHeader = (HeaderHolder) holder;
                mHeader.mTvUsername.setText(username);
                break;
            case MENU_ITEM_INFORMATION:
                MenuItem menuItem = (MenuItem) mItems.get(position);
                ItemHolder mItem = (ItemHolder) holder;
                mItem.mTvMenuItemName.setText(menuItem.getTitle());
                mItem.mImgIcon.setImageResource(menuItem.getIcon());

                mItem.mRlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemClickListener(position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mItems == null) ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof MenuItem) {
            return MENU_ITEM_INFORMATION;
        }
        if (mItems.get(position) instanceof String) {
            return MENU_HEADER;
        }
        if (mItems.get(position) instanceof Integer) {
            return MENU_FOOTER;
        }
        return -1;
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView mTvUsername;

        public HeaderHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        TextView mTvNameApp;

        public FooterHolder(View itemView) {
            super(itemView);
            mTvNameApp = (TextView) itemView.findViewById(R.id.tvNameApp);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView mTvMenuItemName;
        ImageView mImgIcon;
        RelativeLayout mRlItem;

        public ItemHolder(View itemView) {
            super(itemView);
            mTvMenuItemName = (TextView) itemView.findViewById(R.id.tvMenuItemName);
            mImgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            mRlItem = (RelativeLayout) itemView.findViewById(R.id.rlMenuItem);
        }
    }

    public interface itemClick{
        void  onItemClickListener(int position);
    }
}
