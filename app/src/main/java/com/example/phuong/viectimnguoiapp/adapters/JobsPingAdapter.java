package com.example.phuong.viectimnguoiapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.HistoryPingDetailActivity_;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        if(historyPing.getChoice().equals("true")){
            holder.mChkConfirm.setVisibility(View.VISIBLE);
        }
        else{
            holder.mChkConfirm.setVisibility(View.GONE);
        }

        holder.mChkConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(historyPing.getConfirm().equals("true")){
                    holder.mChkConfirm.setChecked(true);
                } else{
                    //day du lieu
                    showDialogConfirm(mContext,historyPing);
                    // cong diem
                }

            }
        });
    }

    public void showDialogConfirm(Context context, final HistoryPing historyPing){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) dialog.findViewById(R.id.tvBtnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView mtvContent = (TextView) dialog.findViewById(R.id.tvContent);
        mtvContent.setText("Xác nhận làm việc này ?");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pingRef = FirebaseDatabase.getInstance().getReference("/pings/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + historyPing.getIdPost() + "/" + historyPing.getIdUser() + "/confirm/");
                Ping pingReport = new Ping();
                pingReport.setConfirm("true");
                pingRef.setValue(pingReport.getReport());

                dialog.dismiss();
                historyPing.setConfirm("true");
                notifyDataSetChanged();

                //tru diem
                //subPoint(ping.getIdUser());

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

        public ItemHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mTvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            mLlItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
            mTvTimeCreate = (TextView) itemView.findViewById(R.id.tvTimeCreate);
            mChkConfirm = (CheckBox) itemView.findViewById(R.id.chkConfirm);
        }
    }
}
