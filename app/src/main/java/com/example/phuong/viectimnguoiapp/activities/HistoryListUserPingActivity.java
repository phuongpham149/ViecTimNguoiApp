package com.example.phuong.viectimnguoiapp.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.HistoryPingJobAdapter;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asiantech on 23/05/2017.
 */
@EActivity(R.layout.fragment_list_user_ping_by_new)
public class HistoryListUserPingActivity extends BaseActivity {
    @Extra
    String idPost;

    @ViewById(R.id.recyclerViewUserPing)
    RecyclerView mRecyclerViewJobPings;

    @ViewById(R.id.prograssBarLoading)
    ProgressBar mProgressBarLoading;

    private static final String TAG = "ListUserPingByNew";

    private HistoryPingJobAdapter mAdapter;
    private List<Ping> mPings;
    @Override
    void inits() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        getDataPing();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new HistoryPingJobAdapter(mPings, this);
        mRecyclerViewJobPings.setLayoutManager(layoutManager);
        mRecyclerViewJobPings.setAdapter(mAdapter);
    }

    public void getDataPing() {
        DatabaseReference mFirebasePing = FirebaseDatabase.getInstance().getReference("/pings/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + idPost);
        mPings = new ArrayList<>();
        mFirebasePing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String idPost = dataSnapshot.getKey();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String,Object> map = (HashMap<String, Object>) data.getValue();
                    Ping ping = new Ping();
                    ping.setIdPost(idPost);
                    ping.setUsername(map.get("username").toString());
                    ping.setMessage(map.get("message").toString());
                    ping.setPrice(map.get("price").toString());
                    ping.setIdUser(data.getKey());
                    ping.setChoice(map.get("choice").toString());
                    ping.setReport(map.get("report").toString());
                    mPings.add(ping);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBarLoading.setVisibility(View.GONE);
            }

        });

    }
}
