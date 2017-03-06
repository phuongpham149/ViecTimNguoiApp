package com.example.phuong.viectimnguoiapp.fragments;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.SettingMenuAdapter;
import com.example.phuong.viectimnguoiapp.objects.MenuItem;
import com.example.phuong.viectimnguoiapp.utils.Helpers;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 20/02/2017.
 */
@EFragment(R.layout.fragment_setting_menu)
public class SettingMenuFragment extends BaseFragment implements DrawerLayout.DrawerListener {
    @ViewById(R.id.recyclerViewMenu)
    RecyclerView mRecyclerViewMenu;
    private List<Object> mItems;
    private SettingMenuAdapter mAdapter;

    @Override
    void inits() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewMenu.setLayoutManager(layoutManager);
        initsData();
        mAdapter = new SettingMenuAdapter(mItems, getContext());
        mRecyclerViewMenu.setAdapter(mAdapter);
    }

    public void initsData() {
        mItems = new ArrayList<>();
        mItems.add(new String("header"));
        mItems.add(new MenuItem(R.drawable.ic_home, "Việc làm"));
        mItems.add(new MenuItem(R.drawable.ic_user_infor, "Thông tin cá nhân"));
        mItems.add(new MenuItem(R.drawable.ic_save, "Việc làm đã lưu"));
        mItems.add(new MenuItem(R.drawable.ic_note, "Việc đã tương tác"));
        mItems.add(new MenuItem(R.drawable.ic_settings, "Cài đặt"));
        mItems.add(new MenuItem(R.drawable.ic_feedback, "Phản hồi"));
        mItems.add(new MenuItem(R.drawable.ic_logout, "Đăng xuất"));
        mItems.add(new Integer(1));
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        Helpers.hideSoftKeyboard(getActivity(), getActivity().getCurrentFocus());
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
