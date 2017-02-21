package com.example.phuong.viectimnguoiapp.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.MainTabPager;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by phuong on 21/02/2017.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment {
    @ViewById(R.id.viewPagerMain)
    ViewPager mViewPagerMain;
    @ViewById(R.id.tabs)
    TabLayout mTab;

    @Override
    void inits() {
        setUpViewPager(mViewPagerMain);
        mTab.setupWithViewPager(mViewPagerMain);
        createTabIcons();
    }

    public void setUpViewPager(ViewPager viewPager) {
        MainTabPager adapter = new MainTabPager(getActivity().getSupportFragmentManager());
        adapter.addFragment(NewsFragment_.builder().build(), "Việc làm");
        adapter.addFragment(InforUserFragment_.builder().build(), "Lưu bài");
        adapter.addFragment(InforUserFragment_.builder().build(), "Cài đặt");
        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {

        TextView tabNews = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tablayout_textview, null);
        tabNews.setText("Việc làm");
        tabNews.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bg_tab_news, 0, 0);
        mTab.getTabAt(0).setCustomView(tabNews);

        TextView tabSave = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tablayout_textview, null);
        tabSave.setText("Lưu Bài");
        tabSave.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bg_tab_save, 0, 0);
        mTab.getTabAt(1).setCustomView(tabSave);

        TextView tabSetting = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tablayout_textview, null);
        tabSetting.setText("Cài đặt");
        tabSetting.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bg_tab_setting, 0, 0);
        mTab.getTabAt(2).setCustomView(tabSetting);

    }
}
