package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.NewsAdapter;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asiantech on 26/04/2017.
 */
@EFragment(R.layout.fragment_my_job)
public class MyJobFragment extends BaseFragment implements NewsAdapter.onItemClickListener {
    private static final String TAG = MyJobFragment.class.getSimpleName();
    @ViewById(R.id.recyclerViewMyJob)
    protected RecyclerView mRecyclerViewMyJob;
    @ViewById(R.id.prograssBarLoading)
    protected ProgressBar mProgressBarLoading;
    @ViewById(R.id.tvNoPost)
    protected TextView mTvNoPost;

    private Firebase mFirebasePing;
    private SharedPreferences mSharedPreferencesUserLogin;

    private List<String> mNewContact = new ArrayList<>();
    private List<NewItem> mNewItems = new ArrayList<>();
    private boolean isPostUser = false;

    private NewsAdapter mAdapter;

    @Override
    void inits() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        mSharedPreferencesUserLogin = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        mFirebasePing = new Firebase("https://viectimnguoi-469e6.firebaseio.com/pings/");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        getDataPostByUser(mSharedPreferencesUserLogin.getString(Constant.ID_USER_LOGIN, ""));
        mAdapter = new NewsAdapter(mNewItems, getActivity(), this);
        mRecyclerViewMyJob.setLayoutManager(layoutManager);
        mRecyclerViewMyJob.setAdapter(mAdapter);

        checkUserPost(mSharedPreferencesUserLogin.getString(Constant.ID_USER_LOGIN, ""));
    }

    public void checkUserPost(final String idUser) {
        mFirebasePing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(idUser)) {
                        isPostUser = true;
                        getData();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void getDataPostByUser(final String idUser) {
        Firebase firebasePost = new Firebase("https://viectimnguoi-469e6.firebaseio.com/posts/");
        firebasePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    NewItem newItem = data.getValue(NewItem.class);
                    if (idUser.equals(newItem.getIdUser())) {
                        mNewItems.add(newItem);
                    }
                }
                mAdapter.notifyDataSetChanged();
                mProgressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getData() {
        mFirebasePing.child(mSharedPreferencesUserLogin.getString(Constant.ID_USER_LOGIN, "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mNewContact.add(data.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void itemClickListener(int position) {
        if (isPostUser) {
            for (String id : mNewContact) {
                if (mNewItems.get(position).getId().equals(id)) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, ListUserPingByNew_.builder().idPost(mNewItems.get(position).getId()).build()).commit();
                    return;
                }
            }
            Common.createDialog(getActivity(), "Chưa có tương tác nào");
        } else {
            Common.createDialog(getActivity(), "Chưa có tương tác nào");
        }
    }
}
