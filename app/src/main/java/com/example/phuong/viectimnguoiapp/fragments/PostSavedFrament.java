package com.example.phuong.viectimnguoiapp.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.DetailNewActivity_;
import com.example.phuong.viectimnguoiapp.adapters.NewSaveAdapter;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.NewSave;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by asiantech on 24/04/2017.
 */
@EFragment(R.layout.fragment_post_saved)
public class PostSavedFrament extends BaseFragment implements NewSaveAdapter.onItemClickListener {
    private RealmHelper mData;
    @ViewById(R.id.recyclerViewPostSaved)
    protected RecyclerView mRecyclerViewPostSaved;
    @ViewById(R.id.tvNotifyNoData)
    TextView mTvNotify;

    private NewSaveAdapter mAdapter;
    private List<NewSave> mNewSaves;

    @Override
    void inits() {
        mData = new RealmHelper(getActivity());

        mNewSaves = mData.getNewSaves();

        if(mNewSaves.size()==0){
            mTvNotify.setVisibility(View.VISIBLE);
        }else{
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mAdapter = new NewSaveAdapter(mNewSaves, getActivity(), this);
            mRecyclerViewPostSaved.setLayoutManager(layoutManager);
            mRecyclerViewPostSaved.setAdapter(mAdapter);
        }

    }

    @Override
    public void itemClickListener(int position) {
        DetailNewActivity_.intent(getActivity()).mId(mNewSaves.get(position).getId()).start();
        getActivity().overridePendingTransition(R.anim.anim_slide_bottom_top, R.anim.anim_nothing);
    }
}
