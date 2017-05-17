package com.example.phuong.viectimnguoiapp.fragments;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.JobsPingAdapter;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asiantech on 27/03/2017.
 */
@EFragment(R.layout.fragment_jobs_ping)
public class JobsPingFragment extends BaseFragment {
    private static final String TAG = JobsPingFragment.class.getSimpleName();
    @ViewById(R.id.recyclerViewJobsPing)
    RecyclerView mRecyclerViewJobsPing;
    @ViewById(R.id.prograssBarLoading)
    ProgressBar mProgressBarLoading;
    @ViewById(R.id.tvNotifyNoData)
    TextView mTvNotifyNoData;

    private List<HistoryPing> mHistoryPings;
    private DatabaseReference mFirebaseHistoryPings;

    private DatabaseReference mFirebasePost;
    private DatabaseReference mFirebaseUser;
    private JobsPingAdapter mAdapter;
    private RealmHelper mData;

    private List<StringBuilder> mURLs = new ArrayList<>();
    private int i = 0;

    @Override
    void inits() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        mHistoryPings = new ArrayList<>();

        mData = new RealmHelper(getActivity());
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if (Network.checkNetWork(getActivity())) {
            getDataHistoryPing();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new JobsPingAdapter(mHistoryPings, getActivity());
                    mRecyclerViewJobsPing.setLayoutManager(layoutManager);
                    mRecyclerViewJobsPing.setAdapter(mAdapter);
                    if (mHistoryPings.size() > 0) {
                        for (HistoryPing historyPing : mHistoryPings) {
                            mData.addHistoryPing(historyPing);
                        }
                    } else {
                        mTvNotifyNoData.setVisibility(View.VISIBLE);
                    }
                    mProgressBarLoading.setVisibility(View.GONE);
                }
            }, 5000);
        } else {
            mHistoryPings = mData.getHistoryPings();
            if (mHistoryPings.size() == 0) {
                mTvNotifyNoData.setVisibility(View.VISIBLE);
                mProgressBarLoading.setVisibility(View.GONE);
            }
            mAdapter = new JobsPingAdapter(mHistoryPings, getActivity());
            mRecyclerViewJobsPing.setLayoutManager(layoutManager);
            mRecyclerViewJobsPing.setAdapter(mAdapter);
            mProgressBarLoading.setVisibility(View.GONE);
        }

    }

    public void getDataHistoryPing() {
        mFirebaseHistoryPings = FirebaseDatabase.getInstance().getReference("/historyPingByUser/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        mFirebaseHistoryPings.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) d.getValue();
                    HistoryPing historyPing = new HistoryPing();
                    historyPing.setIdPost(data.get("idPost").toString());
                    historyPing.setPrice(data.get("price").toString());
                    mHistoryPings.add(historyPing);
                    getDataPost();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void getDataPost() {
        mFirebasePost = FirebaseDatabase.getInstance().getReference("/posts");
        mFirebasePost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot subSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) subSnapshot.getValue();
                    for (HistoryPing historyPing : mHistoryPings) {
                        if (historyPing.getIdPost().equals(map.get("id").toString())) {
                            historyPing.setTitlePost(mData.getCategoryJobItem(map.get("idCat").toString()).getName());
                            historyPing.setTimeDeadline(map.get("timeDeadline").toString());
                            historyPing.setUserOwner(map.get("idUser").toString());
                            historyPing.setNote(map.get("note").toString());
                            historyPing.setAddress(map.get("address").toString());
                            historyPing.setIdUser(map.get("idUser").toString());
                            historyPing.setNameDistrict(mData.getDistrictItem(map.get("idDistrict").toString()).first().getName());
                        }
                    }
                    getNameOwner();
                }
                getContactByOwner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getContactByOwner() {
        DatabaseReference ping;
        getListURL(mHistoryPings);
        for (; i < mHistoryPings.size(); i++) {
            ping = FirebaseDatabase.getInstance().getReference(mURLs.get(i).toString());
            final int position = i;
            ping.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        mHistoryPings.get(position).setChoice(map.get("choice").toString());
                        mHistoryPings.get(position).setConfirm(map.get("confirm").toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void getNameOwner() {
        mFirebaseUser = FirebaseDatabase.getInstance().getReference("/users");
        mFirebaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                    for (HistoryPing historyPing : mHistoryPings) {
                        if (historyPing.getUserOwner().equals(map.get("id").toString())) {
                            historyPing.setUserOwner(map.get("username").toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void getListURL(List<HistoryPing> historyPings) {
        for (HistoryPing historyPing : historyPings) {
            StringBuilder url = new StringBuilder("/pings/");
            url.append(historyPing.getUserOwner());
            url.append("/");
            url.append(historyPing.getIdPost());
            url.append("/");
            url.append(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mURLs.add(url);
        }
    }


}
