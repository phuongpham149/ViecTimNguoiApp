package com.example.phuong.viectimnguoiapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.DetailNewActivity_;
import com.example.phuong.viectimnguoiapp.activities.LoginActivity1_;
import com.example.phuong.viectimnguoiapp.adapters.NewsAdapter;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
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

import java.util.ArrayList;
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
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private RealmHelper mData;

    @Override
    void inits() {
        mSharedPreferences = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

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

        mSettingJobs = mSharedPreferences.getString(Constant.SETTING_JOB, "");
        mSettingAddress = mSharedPreferences.getString(Constant.SETTING_ADDRESS, "");
    }

    public void showDialogLogout() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.putString(Constant.IS_USER_LOGIN, "false");
                mEditor.apply();
                //TODO logout here, you must clear data current user befor logout
                FirebaseAuth.getInstance().signOut();
                logoutFacebook();
                LoginActivity1_.intent(getActivity()).start();
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
        mData = new RealmHelper(getActivity());
        if (Network.checkNetWork(getActivity(), Constant.TYPE_NETWORK) || Network.checkNetWork(getActivity(), Constant.TYPE_WIFI)) {
            mFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot obj : dataSnapshot.getChildren()) {
                        NewItem newItem = obj.getValue(NewItem.class);
                        if (newItem.getStatus() == Integer.parseInt(Constant.STATUS_APPROVAL)) {
                            if (!mSettingAddress.equals("") && !mSettingJobs.equals("") && mSettingJobs.contains(String.valueOf(newItem.getIdCat())) && mSettingAddress.contains(String.valueOf(newItem.getIdDistrict()))) {
                                mNews.add(newItem);
                                mData.addNew(newItem);
                            } else if (!mSettingJobs.equals("") && mSettingJobs.contains(String.valueOf(newItem.getIdCat()))) {
                                mNews.add(newItem);
                                mData.addNew(newItem);
                            } else if (!mSettingAddress.equals("") && mSettingAddress.contains(String.valueOf(newItem.getIdDistrict()))) {
                                mNews.add(newItem);
                                mData.addNew(newItem);
                            } else {
                                mNews.add(newItem);
                                mData.addNew(newItem);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            mNews = mData.getNews();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
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
        }, 3000);
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
