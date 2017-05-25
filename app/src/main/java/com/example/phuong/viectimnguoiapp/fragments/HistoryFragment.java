package com.example.phuong.viectimnguoiapp.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.HistoryAdapter;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by asiantech on 23/05/2017.
 */
@EFragment(R.layout.fragment_history)
public class HistoryFragment extends BaseFragment {
    @ViewById(R.id.tabs)
    TabLayout mTab;
    @ViewById(R.id.viewpager)
    ViewPager mVpHistory;

    @Override
    void inits() {
        setupViewPager(mVpHistory);
        mTab.setupWithViewPager(mVpHistory);
    }

    private void setupViewPager(ViewPager viewPager) {
        HistoryAdapter adapter = new HistoryAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(HistoryPingFragment_.builder().build(), "Đặt chỗ");
        adapter.addFragment(HistoryPostFragment_.builder().build(), "Việc làm");
        viewPager.setAdapter(adapter);
    }
}
