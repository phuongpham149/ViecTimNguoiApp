package com.example.phuong.viectimnguoiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;

import java.util.List;

/**
 * Created by phuong on 22/02/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private List<String> mUsers;
    private Context mContext;
    private onItemClickListener mListener;

    public ContactAdapter(List<String> mUsers, Context mContext, onItemClickListener listener) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        mListener = listener;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, final int position) {
        holder.mTvUserContact.setText(mUsers.get(position));
        holder.mLlContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.itemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mUsers == null) ? 0 : mUsers.size();
    }

    public interface onItemClickListener {
        void itemClickListener(int position);
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        TextView mTvUserContact;
        LinearLayout mLlContactItem;

        public ContactHolder(View itemView) {
            super(itemView);
            mTvUserContact = (TextView) itemView.findViewById(R.id.tvUserContact);
            mLlContactItem = (LinearLayout) itemView.findViewById(R.id.llContactItem);
        }
    }
}
