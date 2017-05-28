package com.example.phuong.viectimnguoiapp.adapters;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
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
import com.example.phuong.viectimnguoiapp.activities.ProfileUserActivity_;
import com.example.phuong.viectimnguoiapp.objects.NotifyReport;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.example.phuong.viectimnguoiapp.objects.User;
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
import java.util.Random;

/**
 * Created by phuong on 27/04/2017.
 */

public class PingJobAdapter extends RecyclerView.Adapter<PingJobAdapter.NewsHolder> {
    private List<Ping> pings;
    private Context mContext;
    private int[] mIcons = {R.drawable.ic_user_blue, R.drawable.ic_user_gray, R.drawable.ic_user_green, R.drawable.ic_user_yellow};
    private DatabaseReference user;

    private onItemClickListener mListener;

    private boolean isContact = false;

    public PingJobAdapter(List<Ping> pings, Context mContext, onItemClickListener listener) {
        this.pings = pings;
        this.mContext = mContext;
        mListener = listener;
        user = FirebaseDatabase.getInstance().getReference("/users");
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ping_job, parent, false);
        return new NewsHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final NewsHolder holder, final int position) {
        final Ping item = pings.get(position);
        holder.mTvName.setText(item.getUsername());
        holder.mTvPrice.setText(item.getPrice());
        int randomAvarta = (new Random().nextInt(4));
        holder.mImgAvarta.setImageResource(mIcons[randomAvarta]);
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.itemClickListener(position);
            }
        });
        if (item.getConfirm().equals("true")) {
            holder.mRlItem.setBackgroundColor(Color.parseColor("#33377EB0"));
        }

        if (item.getChoice().equals("true")) {
            holder.mChkContact.setChecked(true);
        } else {
            holder.mChkContact.setChecked(false);
        }

        holder.mChkContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getChoice().equals("false")) {
                    Handler handler = new Handler();
                    isContact = isContactWithUser(item);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isContact) {
                                Toast.makeText(mContext, "Bạn đã xác nhận với người khác", Toast.LENGTH_SHORT).show();
                                holder.mChkContact.setChecked(false);
                                return;
                            } else {
                                updateChoice(item, "true");
                            }
                        }
                    }, 2000);
                } else {
                    if (item.getConfirm().equals("true")) {
                        Toast.makeText(mContext, "Bạn không thay đổi được xác nhận việc làm.", Toast.LENGTH_SHORT).show();
                        holder.mChkContact.setChecked(true);
                    } else {
                        updateChoice(item, "false");
                    }
                }
            }
        });

        if (item.getReport().equals("true")) {
            holder.mImgReport.setVisibility(View.GONE);
        }

        holder.mImgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog
                showDialogConfirmReport(mContext, pings.get(position));
            }
        });

        holder.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileUserActivity_.intent(mContext).idUser(pings.get(position).getIdUser()).start();
            }
        });
    }

    public void updateChoice(Ping ping, String choice) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("/pings/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + ping.getIdPost() + "/" + ping.getIdUser());
        Ping pingChoice = new Ping();
        pingChoice.setChoice(choice);
        pingChoice.setUsername(ping.getUsername());
        pingChoice.setMessage(ping.getMessage());
        pingChoice.setPrice(ping.getPrice());
        pingChoice.setReport(ping.getReport());
        pingChoice.setConfirm(ping.getConfirm());
        data.setValue(pingChoice);
    }

    public boolean isContactWithUser(Ping ping) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("/pings/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + ping.getIdPost());
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                    if (map.get("choice").toString().equals("true")) {
                        isContact = true;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return isContact;
    }

    public void showDialogConfirmReport(Context context, final Ping ping) {
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
                    DatabaseReference pingRef = FirebaseDatabase.getInstance().getReference("/pings/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + ping.getIdPost() + "/" + ping.getIdUser() + "/report/");
                    Ping pingReport = new Ping();
                    pingReport.setReport("true");
                    pingRef.setValue(pingReport.getReport());

                    dialog.dismiss();
                    ping.setReport("true");
                    notifyDataSetChanged();

                    //tru diem
                    subPoint(ping.getIdUser());
                    notifyReport(ping.getIdUser(), tvContent.getText().toString());
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

    public void notifyReport(String idUser, String msg) {
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

    @Override
    public int getItemCount() {
        return (pings == null) ? 0 : pings.size();
    }

    public interface onItemClickListener {
        void itemClickListener(int position);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        TextView mTvPrice;
        ImageView mImgAvarta;
        ImageView mImgReport;
        CheckBox mChkContact;
        RelativeLayout mRlItem;

        public NewsHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            mImgAvarta = (ImageView) itemView.findViewById(R.id.imgAvartar);
            mRlItem = (RelativeLayout) itemView.findViewById(R.id.rlNewItem);
            mImgReport = (ImageView) itemView.findViewById(R.id.imgReport);
            mChkContact = (CheckBox) itemView.findViewById(R.id.chkContact);
        }
    }
}
