package com.example.phuong.viectimnguoiapp.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.ProfileUserActivity_;
import com.example.phuong.viectimnguoiapp.adapters.NotifyReportAdapter;
import com.example.phuong.viectimnguoiapp.objects.NotifyReport;
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
 * Created by asiantech on 21/05/2017.
 */
@EFragment(R.layout.fragment_notify_report)
public class NotifyReportFragment extends BaseFragment implements NotifyReportAdapter.onItemClickListener {
    @ViewById(R.id.recyclerViewNotify)
    RecyclerView mRecyclerViewNotify;
    @ViewById(R.id.progressBarLoading)
    ProgressBar mProgressBarLoading;
    @ViewById(R.id.tvNotifyNoData)
    TextView mTvNotifyNoData;

    private List<NotifyReport> mNotifyReports = new ArrayList<>();
    private NotifyReportAdapter mAdapter;

    @Override
    void inits() {
        getNotify();
        Log.d("tag2", FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void getNotify() {
        DatabaseReference databaseReferenceNotify = FirebaseDatabase.getInstance().getReference(getString(R.string.child_notify_report));
        databaseReferenceNotify.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                    Log.d("tag2", "a " + map.toString());
                    NotifyReport notifyReport = new NotifyReport();
                    notifyReport.setIdUserReport(map.get("idUserReport").toString());
                    notifyReport.setMessage(map.get("message").toString());
                    notifyReport.setTime(map.get("timeReport").toString());
                    notifyReport.setUsernameReport(map.get("nameUserReport").toString());
                    mNotifyReports.add(notifyReport);
                }
                updateView(mNotifyReports);
                mProgressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateView(List<NotifyReport> list) {
        if (list.size() == 0) {
            mTvNotifyNoData.setVisibility(View.VISIBLE);
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerViewNotify.setLayoutManager(layoutManager);
            mAdapter = new NotifyReportAdapter(list, getContext(), this);
            mRecyclerViewNotify.setAdapter(mAdapter);
        }
    }

    @Override
    public void itemClickListener(int position) {
        ProfileUserActivity_.intent(this).idUser(mNotifyReports.get(position).getIdUserReport()).start();
    }
}
