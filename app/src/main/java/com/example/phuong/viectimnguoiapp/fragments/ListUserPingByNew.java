package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.PingJobAdapter;
import com.example.phuong.viectimnguoiapp.objects.Ping;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asiantech on 27/04/2017.
 */
@EFragment(R.layout.fragment_list_user_ping_by_new)
public class ListUserPingByNew extends BaseFragment implements PingJobAdapter.onItemClickListener {
    @FragmentArg
    String idPost;

    @ViewById(R.id.recyclerViewUserPing)
    RecyclerView mRecyclerViewJobPings;

    @ViewById(R.id.prograssBarLoading)
    ProgressBar mProgressBarLoading;

    private static final String TAG = "ListUserPingByNew";

    private PingJobAdapter mAdapter;
    private List<Ping> mPings;
    private SharedPreferences mSharedPreferencesUserLogin;

    @Override
    void inits() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        mSharedPreferencesUserLogin = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        getDataPing();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PingJobAdapter(mPings, getContext(), this);
        mRecyclerViewJobPings.setLayoutManager(layoutManager);
        mRecyclerViewJobPings.setAdapter(mAdapter);
    }

    public void getDataPing() {
        Firebase mFirebasePing = new Firebase("https://viectimnguoi-469e6.firebaseio.com/pings/" + mSharedPreferencesUserLogin.getString(Constant.ID_USER_LOGIN, "") + "/" + idPost);
        mPings = new ArrayList<>();
        mFirebasePing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map map = data.getValue(Map.class);
                    Ping ping = new Ping();
                    ping.setUsername(map.get("username").toString());
                    ping.setMessage(map.get("message").toString());
                    ping.setPrice(map.get("price").toString());
                    mPings.add(ping);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void itemClickListener(int position) {

    }
}
