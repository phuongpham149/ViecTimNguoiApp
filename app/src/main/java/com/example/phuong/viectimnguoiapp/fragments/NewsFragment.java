package com.example.phuong.viectimnguoiapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.DetailNewActivity_;
import com.example.phuong.viectimnguoiapp.adapters.NewsAdapter;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 21/02/2017.
 */
@EFragment(R.layout.fragment_news)
public class NewsFragment extends BaseFragment implements NewsAdapter.onItemClickListener {
    @ViewById(R.id.recyclerViewNews)
    RecyclerView mRecyclerViewNews;

    private NewsAdapter mAdapter;
    private List<NewItem> mNews;
    private Firebase mFirebase;
    private ProgressDialog mProgressDialogLoading;

    private String mSettingJobs;
    private String mSettingAddress;
    private SharedPreferences mSharedPreferences;

    @Override
    void inits() {

        mProgressDialogLoading = new ProgressDialog(getActivity());
        mProgressDialogLoading.setMessage("Loading...");
        mProgressDialogLoading.show();

        Firebase.setAndroidContext(getActivity());
        mFirebase = new Firebase("https://viectimnguoi-469e6.firebaseio.com/posts");

        mNews = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewNews.setLayoutManager(layoutManager);
        mAdapter = new NewsAdapter(mNews, getContext(), this);

        initsData();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewNews.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mProgressDialogLoading.dismiss();
            }
        }, 3000);

        mSharedPreferences = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, Context.MODE_PRIVATE);
        mSettingJobs = mSharedPreferences.getString(Constant.SETTING_JOB, "");
        mSettingAddress = mSharedPreferences.getString(Constant.SETTING_ADDRESS, "");
    }

    public void initsData() {

        if (Network.checkNetWork(getActivity(), Constant.TYPE_NETWORK) || Network.checkNetWork(getActivity(), Constant.TYPE_WIFI)) {
            mFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        NewItem newItem = obj.getValue(NewItem.class);
                        if (newItem.getStatus() == Integer.parseInt(Constant.STATUS_APPROVAL) && mSettingJobs.contains(String.valueOf(newItem.getIdCat())) && mSettingAddress.contains(String.valueOf(newItem.getIdDistrict()))) {
                            mNews.add(newItem);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError error) {
                }
            });
        } else {
            Common.createDialog(getActivity(), "Vui lòng kiểm tra kết nối mạng", "", false, mProgressDialogLoading);
        }
    }

    @Override
    public void itemClickListener(int position) {
        DetailNewActivity_.intent(this).mNewItem(mNews.get(position)).start();
        getActivity().overridePendingTransition(R.anim.anim_slide_bottom_top, R.anim.anim_nothing);
    }
}
