package com.example.phuong.viectimnguoiapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.HistoryPingDetailActivity_;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by asiantech on 27/03/2017.
 */
public class JobsPingAdapter extends RecyclerView.Adapter<JobsPingAdapter.ItemHolder> {
    private List<HistoryPing> mHistoryPings;
    private Context mContext;
    private DatabaseReference mUserRef;

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
    public void onBindViewHolder(final ItemHolder holder, int position) {
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
        if (historyPing.getChoice().equals("true")) {
            holder.mChkConfirm.setVisibility(View.VISIBLE);
        } else {
            holder.mChkConfirm.setVisibility(View.GONE);
        }

        holder.mChkConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (historyPing.getConfirm().equals("true")) {
                    holder.mChkConfirm.setChecked(true);
                } else {
                    //day du lieu
                    showDialogConfirm(mContext, historyPing);
                    // cong diem
                }

            }
        });

        holder.mImgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Bạn đã báo cáo vi phạm người này",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialogConfirm(Context context, final HistoryPing historyPing) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView mtvContent = (TextView) dialog.findViewById(R.id.tvContent);
        mtvContent.setText("Xác nhận làm việc này ?");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pingRef = FirebaseDatabase.getInstance().getReference("/pings/" + historyPing.getIdUser() + "/" + historyPing.getIdPost() + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/confirm/");
                Log.d("confirm", "/pings/" + historyPing.getUserOwner() + "/" + historyPing.getIdPost() + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/confirm/");
                Ping pingReport = new Ping();
                pingReport.setConfirm("true");
                pingRef.setValue(pingReport.getConfirm());

                dialog.dismiss();
                historyPing.setConfirm("true");
                notifyDataSetChanged();

                //cong diem
                getKeyUser(historyPing.getIdUser());
                getKeyUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Common.createDialog(mContext, "Xác nhận việc làm thành công");
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void getKeyUser(final String idUser) {
        mUserRef = FirebaseDatabase.getInstance().getReference("/users");
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                    if (idUser.equals(map.get("id").toString())) {
                        addPointUser(data.getKey(), map.get("point").toString());
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addPointUser(String key, String point) {
        mUserRef.child(key).child("point").setValue(String.valueOf(Integer.parseInt(point) + 1));
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
        CheckBox mChkConfirm;
        ImageView mImgReport;

        public ItemHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mTvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            mLlItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
            mTvTimeCreate = (TextView) itemView.findViewById(R.id.tvTimeCreate);
            mChkConfirm = (CheckBox) itemView.findViewById(R.id.chkConfirm);
            mImgReport = (ImageView)  itemView.findViewById(R.id.imgReport);
        }
    }
}
