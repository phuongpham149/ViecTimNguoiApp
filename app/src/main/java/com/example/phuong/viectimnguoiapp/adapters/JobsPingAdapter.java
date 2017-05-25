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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.HistoryPingDetailActivity_;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;
import com.example.phuong.viectimnguoiapp.objects.NotifyReport;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

                //show dialog
                showDialogConfirmReport(mContext,historyPing);
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

    public void showDialogConfirmReport(Context context, final HistoryPing ping) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) dialog.findViewById(R.id.tvBtnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        final EditText tvContent = (EditText) dialog.findViewById(R.id.edtPrice);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvContent.getText().equals("")) {
                    Toast.makeText(mContext, "Vui lòng nhập lí do báo vi phạm", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference pingRef = FirebaseDatabase.getInstance().getReference("/pings/" + ping.getIdUser() + "/" + ping.getIdPost() + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/report/");
                    Ping pingReport = new Ping();
                    pingReport.setReport("true");
                    pingRef.setValue(pingReport.getReport());

                    dialog.dismiss();
                    ping.setReport("true");
                    notifyDataSetChanged();

                    //tru diem
                    subPoint(ping.getIdUser());
                    notifyReport(ping.getIdUser(),tvContent.getText().toString());
                    Toast.makeText(mContext,"Bạn đã báo cáo vi phạm người này",Toast.LENGTH_SHORT).show();
                }
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

    public void notifyReport(String idUser,String msg) {
        DatabaseReference notifyReport = FirebaseDatabase.getInstance().getReference(mContext.getString(R.string.child_notify_report));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        NotifyReport report = new NotifyReport();
        report.setIdUserReport(FirebaseAuth.getInstance().getCurrentUser().getUid());
        report.setTime(dateFormat.format(date));
        report.setUsernameReport(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        report.setMessage(msg);
        notifyReport.child(idUser).push().setValue(report);
    }

    public void subPoint(final String idUser) {
        DatabaseReference user = FirebaseDatabase.getInstance().getReference(mContext.getString(R.string.child_user));
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();
                    HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                    if (map.get("id").toString().equals(idUser)) {
                        User userReported = new User();
                        userReported.setPoint(String.valueOf(Integer.parseInt(map.get("point").toString()) - 2));

                        setPointForReport(key, userReported.getPoint());
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setPointForReport(String keyUser, String subPoint) {
        DatabaseReference dataUser = FirebaseDatabase.getInstance().getReference("/users/" + keyUser + "/point/");
        dataUser.setValue(subPoint);
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
