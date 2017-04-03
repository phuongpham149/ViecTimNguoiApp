package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.JobsPingAdapter;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;
import com.example.phuong.viectimnguoiapp.utils.Constant;
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

    private List<HistoryPing> mHistoryPings;
    private Firebase mFirebaseHistoryPings;
    private SharedPreferences mSharedPreferencesUser;
    private Firebase mFirebasePost;
    private Firebase mFirebaseCategory;
    private Firebase mFirebaseUser;
    private Firebase mFirebaseDistrict;
    private JobsPingAdapter mAdapter;

    @Override
    void inits() {
        mHistoryPings = new ArrayList<>();
        mSharedPreferencesUser = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        getDataHistoryPing();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                mAdapter = new JobsPingAdapter(mHistoryPings, getActivity());
                mRecyclerViewJobsPing.setLayoutManager(layoutManager);
                mRecyclerViewJobsPing.setAdapter(mAdapter);
            }
        }, 1000);
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
                        historyPing.setTitlePost(map.get("idCat").toString());
                        historyPing.setTimeCreated(map.get("timeCreated").toString());
                        historyPing.setUserOwner(map.get("idUser").toString());
                        historyPing.setNote(map.get("note").toString());
                        historyPing.setAddress(map.get("address").toString());
                        historyPing.setNameDistrict(map.get("idDistrict").toString());
                    }
                }
                getTitlePost();
                getNameOwner();
                getNameDistrict();
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

    public void getNameDistrict() {
        mFirebaseDistrict = new Firebase("https://viectimnguoi-469e6.firebaseio.com/districts");
        mFirebaseDistrict.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                for (HistoryPing historyPing : mHistoryPings) {
                    if (historyPing.getNameDistrict().equals(map.get("id").toString())) {
                        historyPing.setNameDistrict(map.get("name").toString());
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

    public void getTitlePost() {
        mFirebaseCategory = new Firebase("https://viectimnguoi-469e6.firebaseio.com/categoryJobs");
        mFirebaseCategory.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                for (HistoryPing historyPing : mHistoryPings) {
                    if (map.get("id").toString().equals(historyPing.getTitlePost())) {
                        historyPing.setTitlePost(map.get("name").toString());
                    }
                }
                Log.d("tag1233", mHistoryPings.toString());
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
