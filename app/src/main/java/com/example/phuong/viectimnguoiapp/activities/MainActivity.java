package com.example.phuong.viectimnguoiapp.activities;

import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.SettingMenuAdapter;
import com.example.phuong.viectimnguoiapp.fragments.CreateNewFragment_;
import com.example.phuong.viectimnguoiapp.fragments.NewsFragment_;
import com.example.phuong.viectimnguoiapp.objects.MenuItem;
import com.example.phuong.viectimnguoiapp.utils.ScreenUtil;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 20/02/2017.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements SettingMenuAdapter.itemClick{
    public ActionBarDrawerToggle mDrawerToggle;
    @ViewById(R.id.toolBar)
    protected Toolbar mToolbar;
    @ViewById(R.id.drawerLayout)
    protected DrawerLayout mDrawerLayout;
    @ViewById(R.id.frLeftMenuContainer)
    protected RecyclerView mRecyclerViewMenu;
    @ViewById(R.id.rlContainer)
    protected View mRlContainer;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private float mLastTranslate = 0.0f;

    private List<Object> mItems;
    private SettingMenuAdapter mAdapter;

    @Override
    void inits() {
        initActionbar();
        initMenu();
        initMain();
    }

    public void initMain() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, NewsFragment_.builder().build()).commit();
    }

    private void initActionbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initMenu() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewMenu.setLayoutManager(layoutManager);
        initsData();
        mAdapter = new SettingMenuAdapter(mItems, this,this);
        mRecyclerViewMenu.setAdapter(mAdapter);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.drawerlayout_scrim));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerLayout.openDrawer(GravityCompat.START);
                getSupportActionBar().setTitle(mDrawerTitle);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (mRecyclerViewMenu.getWidth() * slideOffset);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mRlContainer.setTranslationX(moveFactor);
                } else {
                    TranslateAnimation anim = new TranslateAnimation(mLastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    mRlContainer.startAnimation(anim);
                    mLastTranslate = moveFactor;
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mRecyclerViewMenu.getLayoutParams();
        params.width = ScreenUtil.getWidthScreen(this) - ScreenUtil.convertDPToPixels(this, 76);

    }

    public void initsData() {
        mItems = new ArrayList<>();
        mItems.add(new String("header"));
        mItems.add(new MenuItem(R.drawable.ic_home, "Việc làm"));
        mItems.add(new MenuItem(R.drawable.ic_user_infor, "Thông tin cá nhân"));
        mItems.add(new MenuItem(R.drawable.ic_create, "Tạo bài đăng"));
        mItems.add(new MenuItem(R.drawable.ic_save, "Việc làm đã lưu"));
        mItems.add(new MenuItem(R.drawable.ic_note, "Việc đã tương tác"));
        mItems.add(new MenuItem(R.drawable.ic_settings, "Cài đặt"));
        mItems.add(new MenuItem(R.drawable.ic_feedback, "Phản hồi"));
        mItems.add(new MenuItem(R.drawable.ic_logout, "Đăng xuất"));
        mItems.add(new Integer(1));
    }

    private void displayView(int position) {
        String title = getString(R.string.app_name);
        switch (position) {
            case 1:
                title = "Bảng tin việc làm";
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, NewsFragment_.builder().build()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 2:
                break;
            case 3:
                title="Tạo bài đăng";
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, CreateNewFragment_.builder().build()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClickListener(int position) {
        displayView(position);
    }
}
