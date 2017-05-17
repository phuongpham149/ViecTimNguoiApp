package com.example.phuong.viectimnguoiapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
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

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private String keyUser;
    private User UserSubPoint = new User();

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

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {
        final Ping item = pings.get(position);
        holder.mTvName.setText(item.getUsername());
        holder.mTvPrice.setText(item.getPrice());
        int randomAvarta = (new Random().nextInt(4));
        holder.mImgAvarta.setImageResource(mIcons[randomAvarta]);
        Log.d("tag12", "abc " + item.toString());
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.itemClickListener(position);
            }
        });

        if (item.getReport().equals("true")) {
            Log.d("tag12", "aaaaaaaaaaa ");
            holder.mImgReport.setVisibility(View.GONE);
        }

        holder.mImgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog
                showDialogConfirmReport(mContext, pings.get(position));
                //ok -> firebase chinh isReport true

                //goi message

            }
        });
    }

    public void showDialogConfirmReport(Context context, final Ping ping) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        tvContent.setText("Báo cáo vi phạm người này?");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pingRef = FirebaseDatabase.getInstance().getReference("/pings/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + ping.getIdPost() + "/" + ping.getIdUser());
                Ping pingReport = new Ping();
                pingReport.setChoice(ping.getChoice());
                pingReport.setMessage(ping.getMessage());
                pingReport.setPrice(ping.getPrice());
                pingReport.setUsername(ping.getUsername());
                pingReport.setReport("true");
                pingRef.setValue(pingReport);
                dialog.dismiss();
                notifyDataSetChanged();

                //tru diem
                subPoint(ping.getIdUser());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //tru diem
                        //setPointForReport();


                    }
                }, 2000);

                //goi msg
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

    public void subPoint(final String idUser) {

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();
                    Log.d("tkey", key);
                    HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                    if (map.get("id").toString().equals(idUser)) {
                        User userReported = new User();
                        userReported.setId(map.get("id").toString());
                        userReported.setAddress(map.get("address").toString());
                        userReported.setPhone(map.get("phone").toString());
                        userReported.setUsername(map.get("username").toString());
                        userReported.setEmail(map.get("email").toString());
                        Log.d("data", key + " aaaa id district " + map.get("idDistrict").toString());
                        userReported.setPoint(String.valueOf(Integer.parseInt(map.get("point").toString()) - 2));
                        userReported.setIdDistrict(Integer.parseInt(map.get("idDistrict").toString()));
                        userReported.setStatus(map.get("status").toString());


                        //user.child(key).setValue(userReported);dat
                        //setPointForReport(key,userReported);
                        keyUser = key;
                        UserSubPoint = userReported;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setPointForReport() {
        Log.d("tag123","ab "+keyUser+" user "+UserSubPoint);
        //user.child(keyUser).setValue(UserSubPoint);
        DatabaseReference dataUser = FirebaseDatabase.getInstance().getReference("/users/"+keyUser);
        dataUser.setValue(UserSubPoint);
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
