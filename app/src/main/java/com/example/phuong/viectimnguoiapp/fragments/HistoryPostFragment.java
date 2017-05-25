package com.example.phuong.viectimnguoiapp.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.HistoryListUserPingActivity_;
import com.example.phuong.viectimnguoiapp.adapters.NewsAdapter;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asiantech on 23/05/2017.
 */
@EFragment(R.layout.fragment_my_job)
public class HistoryPostFragment extends BaseFragment implements NewsAdapter.onItemClickListener{
    @ViewById(R.id.recyclerViewMyJob)
    protected RecyclerView mRecyclerViewMyJob;
    @ViewById(R.id.prograssBarLoading)
    protected ProgressBar mProgressBarLoading;
    @ViewById(R.id.tvNoPost)
    protected TextView mTvNoPost;

    private DatabaseReference mFirebasePing;

    private List<String> mNewContact = new ArrayList<>();
    private List<NewItem> mNewItems = new ArrayList<>();
    private boolean isPostUser = false;

    private NewsAdapter mAdapter;

    @Override
    void inits() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        mFirebasePing = FirebaseDatabase.getInstance().getReference("/pings/");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        getDataPostByUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAdapter = new NewsAdapter(mNewItems, getActivity(), this);
        mRecyclerViewMyJob.setLayoutManager(layoutManager);
        mRecyclerViewMyJob.setAdapter(mAdapter);

        checkUserPost(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDataPostByUser(final String idUser) {
        DatabaseReference firebasePost = FirebaseDatabase.getInstance().getReference("/posts/");
        firebasePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    NewItem newItem;
                    HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                    if (idUser.equals(map.get("idUser").toString())) {
                        try {
                            if (!Common.compareDate(map.get("timeDeadline").toString())) {
                                newItem = new NewItem();
                                newItem.setId(map.get("id").toString());
                                newItem.setIdUser(map.get("idUser").toString());
                                newItem.setIdCat(map.get("idCat").toString());
                                newItem.setIdDistrict(map.get("idDistrict").toString());
                                newItem.setAddress(map.get("address").toString());
                                newItem.setTimeDeadline(map.get("timeDeadline").toString());
                                newItem.setTimeCreated(map.get("timeCreated").toString());
                                newItem.setNote(map.get("note").toString());
                                mNewItems.add(newItem);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                mProgressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getData() {
        mFirebasePing.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mNewContact.add(data.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    @Override
    public void itemClickListener(int position) {
        if (isPostUser) {
            for (String id : mNewContact) {
                if (mNewItems.get(position).getId().equals(id)) {
                    HistoryListUserPingActivity_.intent(this).idPost(mNewItems.get(position).getId()).start();
                    return;
                }
            }
            Common.createDialog(getActivity(), "Chưa có tương tác nào");
        } else {
            Common.createDialog(getActivity(), "Chưa có tương tác nào");
        }
    }
}
