package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private static final int MENU_ITEM_SEPARATOR = 2;
    private static final int MENU_ITEM_EMPTY = 3;
    private static final int MENU_FOOTER = 4;
    protected final Context mContext;
    private final List mItems;
    SharedPreferences userLogin;

    public SettingMenuAdapter(List mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
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
            case MENU_ITEM_EMPTY:
                View itemEmpty = inflater.inflate(R.layout.menu_item, parent, false);
                viewHolder = new ItemEmptyHolder(itemEmpty);
                break;
            case MENU_ITEM_INFORMATION:
                View itemInfor = inflater.inflate(R.layout.menu_item, parent, false);
                viewHolder = new ItemHolder(itemInfor);
                break;
            case MENU_ITEM_SEPARATOR:
                View itemSeparator = inflater.inflate(R.layout.menu_item_separator, parent, false);
                viewHolder = new ItemSeparatorHolder(itemSeparator);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MENU_HEADER:
                String username = userLogin.getString(Constant.NAME_USER_LOGIN, "Phuong");
                HeaderHolder mHeader = (HeaderHolder) holder;
                mHeader.mTvUsername.setText(username);
                break;
            case MENU_ITEM_INFORMATION:
                MenuItem menuItem = (MenuItem) mItems.get(position);
                ItemHolder mItem = (ItemHolder) holder;
                mItem.mTvMenuItemName.setText(menuItem.getTitle());
                if (position == (mItems.size() - 2)) {
                    mItem.mViewBottom.setVisibility(View.VISIBLE);
                }
                break;
            case MENU_ITEM_SEPARATOR:
                MenuItem menuItem1 = (MenuItem) mItems.get(position);
                ItemSeparatorHolder mItemSeparator = (ItemSeparatorHolder) holder;
                mItemSeparator.mTvSeparator.setText(menuItem1.getTitle());
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
            if (((MenuItem) mItems.get(position)).getClazz() == null) {
                if (position == 4 || position == 7) {
                    return MENU_ITEM_EMPTY;
                } else {
                    return MENU_ITEM_SEPARATOR;
                }
            } else {
                return MENU_ITEM_INFORMATION;
            }
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
        TextView mTvTitle;
        TextView mTvUsername;

        public HeaderHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
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
        ImageView mImgRight;
        View mViewTop;
        View mViewBottom;

        public ItemHolder(View itemView) {
            super(itemView);
            mTvMenuItemName = (TextView) itemView.findViewById(R.id.tvMenuItemName);
            mImgRight = (ImageView) itemView.findViewById(R.id.imgRight);
            mViewTop = (View) itemView.findViewById(R.id.viewLineTop);
            mViewBottom = (View) itemView.findViewById(R.id.viewLineBottom);
            mViewBottom.setVisibility(View.GONE);
        }
    }

    public class ItemEmptyHolder extends RecyclerView.ViewHolder {
        TextView mTvMenuItemName;
        ImageView mImgRight;
        View mViewTop;
        View mViewBottom;

        public ItemEmptyHolder(View itemView) {
            super(itemView);
            mTvMenuItemName = (TextView) itemView.findViewById(R.id.tvMenuItemName);
            mImgRight = (ImageView) itemView.findViewById(R.id.imgRight);
            mViewTop = (View) itemView.findViewById(R.id.viewLineTop);
            mViewBottom = (View) itemView.findViewById(R.id.viewLineBottom);
            mImgRight.setVisibility(View.GONE);
        }
    }

    public class ItemSeparatorHolder extends RecyclerView.ViewHolder {
        TextView mTvSeparator;

        public ItemSeparatorHolder(View itemView) {
            super(itemView);
            mTvSeparator = (TextView) itemView.findViewById(R.id.tvSeparator);
        }
    }
}
