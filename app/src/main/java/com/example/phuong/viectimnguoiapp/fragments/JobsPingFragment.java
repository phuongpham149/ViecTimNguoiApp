package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
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
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asiantech on 27/03/2017.
 */
@EFragment(R.layout.fragment_jobs_ping)
public class JobsPingFragment extends BaseFragment {

    @ViewById(R.id.recyclerViewJobsPing)
    RecyclerView mRecyclerViewJobsPing;
    @ViewById(R.id.prograssBarLoading)
    ProgressBar mProgressBarLoading;
    @ViewById(R.id.tvNotifyNoData)
    TextView mTvNotifyNoData;

    private List<HistoryPing> mHistoryPings;
    private Firebase mFirebaseHistoryPings;
    private SharedPreferences mSharedPreferencesUser;

    private Firebase mFirebasePost;
    private Firebase mFirebaseUser;
    private JobsPingAdapter mAdapter;
    private RealmHelper mData;

    @Override
    void inits() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        mHistoryPings = new ArrayList<>();
        mSharedPreferencesUser = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);

        mData = new RealmHelper(getActivity());
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if (Network.checkNetWork(getActivity(), Constant.TYPE_NETWORK) || Network.checkNetWork(getActivity(), Constant.TYPE_WIFI)) {
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
            }, 2000);
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
        mFirebaseHistoryPings = new Firebase("https://viectimnguoi-469e6.firebaseio.com/historyPingByUser");

        mFirebaseHistoryPings.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (mSharedPreferencesUser.getString(Constant.ID_USER_LOGIN, "").equals(dataSnapshot.getKey())) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        HistoryPing historyPing = d.getValue(HistoryPing.class);
                        mHistoryPings.add(historyPing);
                    }
                    getDataPost();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getDataPost() {
        mFirebasePost = new Firebase("https://viectimnguoi-469e6.firebaseio.com/posts");
        mFirebasePost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                for (HistoryPing historyPing : mHistoryPings) {
                    if (historyPing.getIdPost().equals(map.get("id").toString())) {
                        historyPing.setTitlePost(mData.getCategoryJobItem(map.get("idCat").toString()).first().getName());
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

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getNameOwner() {
        mFirebaseUser = new Firebase("https://viectimnguoi-469e6.firebaseio.com/users");
        mFirebaseUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                for (HistoryPing historyPing : mHistoryPings) {
                    if (historyPing.getUserOwner().equals(map.get("id").toString())) {
                        historyPing.setUserOwner(map.get("username").toString());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


}
