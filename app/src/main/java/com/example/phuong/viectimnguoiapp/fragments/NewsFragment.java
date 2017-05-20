package com.example.phuong.viectimnguoiapp.fragments;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.DetailNewActivity_;
import com.example.phuong.viectimnguoiapp.activities.LoginActivity_;
import com.example.phuong.viectimnguoiapp.adapters.NewsAdapter;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.example.phuong.viectimnguoiapp.utils.SharedPreferencesUtils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by phuong on 21/02/2017.
 */
@EFragment(R.layout.fragment_news)
public class NewsFragment extends BaseFragment {
    @ViewById(R.id.recyclerViewNews)
    RecyclerView mRecyclerViewNews;
    @ViewById(R.id.progressBarLoadingNews)
    ProgressBar mProgressBarLoadingNews;
    @FragmentArg
    public boolean isLogout = false;

    private NewsAdapter mAdapter;
    private List<NewItem> mNews;
    private DatabaseReference mFirebase;

    private String mSettingJobs;
    private String mSettingAddress;
    private RealmHelper mData;

    @Override
    void inits() {
        if (isLogout) {
            showDialogLogout();
        } else {
            getDataNews();
        }
    }

    public void getDataNews() {
        mProgressBarLoadingNews.setVisibility(View.VISIBLE);
        mFirebase = FirebaseDatabase.getInstance().getReference("/posts");

        mNews = new ArrayList<>();
        initsData();
    }

    public void showDialogLogout() {
        final Dialog dialog = new Dialog(getActivity());
        mData = new RealmHelper(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.clearData();
                SharedPreferencesUtils.getInstance().deleteAll(getContext());
                FirebaseAuth.getInstance().signOut();
                logoutFacebook();
                LoginActivity_.intent(getActivity()).start();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getDataNews();
            }
        });

        dialog.show();
    }

    public void initsData() {
        mSettingJobs = SharedPreferencesUtils.getInstance().getSettingJob(getContext());
        mSettingAddress = SharedPreferencesUtils.getInstance().getSettingAddress(getContext());

        Log.d("tag1", mSettingJobs + " b " + mSettingAddress);
        mData = new RealmHelper(getActivity());
        if (Network.checkNetWork(getActivity())) {
            mFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (TextUtils.isEmpty(mSettingJobs) && TextUtils.isEmpty(mSettingAddress)) {
                        for (DataSnapshot obj : dataSnapshot.getChildren()) {

                            NewItem newItem;
                            HashMap<String, Object> data = (HashMap<String, Object>) obj.getValue();
                            try {
                                if (Common.compareDate(data.get("timeDeadline").toString())) {
                                    newItem = new NewItem();
                                    newItem.setId(data.get("id").toString());
                                    newItem.setIdUser(data.get("idUser").toString());
                                    newItem.setIdCat(data.get("idCat").toString());
                                    newItem.setIdDistrict(data.get("idDistrict").toString());
                                    newItem.setAddress(data.get("address").toString());
                                    newItem.setTimeDeadline(data.get("timeDeadline").toString());
                                    newItem.setTimeCreated(data.get("timeCreated").toString());
                                    newItem.setNote(data.get("note").toString());
                                    mData.addNew(newItem);
                                    mNews.add(newItem);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if ((TextUtils.isEmpty(mSettingJobs) && !TextUtils.isEmpty(mSettingAddress))) {
                        for (DataSnapshot obj : dataSnapshot.getChildren()) {
                            HashMap<String, Object> data = (HashMap<String, Object>) obj.getValue();
                            if (mSettingAddress.contains(data.get("idDistrict").toString())) {
                                NewItem newItem;
                                try {
                                    if (Common.compareDate(data.get("timeDeadline").toString())) {
                                        newItem = new NewItem();
                                        newItem.setId(data.get("id").toString());
                                        newItem.setIdUser(data.get("idUser").toString());
                                        newItem.setIdCat(data.get("idCat").toString());
                                        newItem.setIdDistrict(data.get("idDistrict").toString());
                                        newItem.setAddress(data.get("address").toString());
                                        newItem.setTimeDeadline(data.get("timeDeadline").toString());
                                        newItem.setTimeCreated(data.get("timeCreated").toString());
                                        newItem.setNote(data.get("note").toString());
                                        mData.addNew(newItem);
                                        mNews.add(newItem);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if ((!TextUtils.isEmpty(mSettingJobs) && TextUtils.isEmpty(mSettingAddress))) {
                        for (DataSnapshot obj : dataSnapshot.getChildren()) {
                            HashMap<String, Object> data = (HashMap<String, Object>) obj.getValue();
                            if (mSettingJobs.contains(data.get("idCat").toString())) {
                                NewItem newItem;
                                try {
                                    if (Common.compareDate(data.get("timeDeadline").toString())) {
                                        newItem = new NewItem();
                                        newItem.setId(data.get("id").toString());
                                        newItem.setIdUser(data.get("idUser").toString());
                                        newItem.setIdCat(data.get("idCat").toString());
                                        newItem.setIdDistrict(data.get("idDistrict").toString());
                                        newItem.setAddress(data.get("address").toString());
                                        newItem.setTimeDeadline(data.get("timeDeadline").toString());
                                        newItem.setTimeCreated(data.get("timeCreated").toString());
                                        newItem.setNote(data.get("note").toString());
                                        mData.addNew(newItem);
                                        mNews.add(newItem);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if ((!TextUtils.isEmpty(mSettingJobs) && !TextUtils.isEmpty(mSettingAddress))) {
                        for (DataSnapshot obj : dataSnapshot.getChildren()) {
                            HashMap<String, Object> data = (HashMap<String, Object>) obj.getValue();
                            if (mSettingAddress.contains(data.get("idDistrict").toString()) && mSettingJobs.contains(data.get("idCat").toString())) {
                                NewItem newItem;
                                try {
                                    if (Common.compareDate(data.get("timeDeadline").toString())) {
                                        newItem = new NewItem();
                                        newItem.setId(data.get("id").toString());
                                        newItem.setIdUser(data.get("idUser").toString());
                                        newItem.setIdCat(data.get("idCat").toString());
                                        newItem.setIdDistrict(data.get("idDistrict").toString());
                                        newItem.setAddress(data.get("address").toString());
                                        newItem.setTimeDeadline(data.get("timeDeadline").toString());
                                        newItem.setTimeCreated(data.get("timeCreated").toString());
                                        newItem.setNote(data.get("note").toString());
                                        mData.addNew(newItem);
                                        mNews.add(newItem);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else

        {
            mNews = mData.getNews();
        }

        Handler handler = new Handler();
        handler.postDelayed(new

                                    Runnable() {
                                        @Override
                                        public void run() {
                                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                            mRecyclerViewNews.setLayoutManager(layoutManager);
                                            mAdapter = new NewsAdapter(mNews, getContext(), new NewsAdapter.onItemClickListener() {
                                                @Override
                                                public void itemClickListener(int position) {
                                                    DetailNewActivity_.intent(getActivity()).mId(mNews.get(position).getId()).start();
                                                    getActivity().overridePendingTransition(R.anim.anim_slide_bottom_top, R.anim.anim_nothing);
                                                }
                                            });
                                            mRecyclerViewNews.setAdapter(mAdapter);
                                            mAdapter.notifyDataSetChanged();
                                            mProgressBarLoadingNews.setVisibility(View.GONE);
                                        }
                                    }

                , 3000);
    }

    private void logoutFacebook() {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }
}
