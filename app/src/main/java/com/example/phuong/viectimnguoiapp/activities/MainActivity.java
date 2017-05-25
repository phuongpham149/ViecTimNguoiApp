package com.example.phuong.viectimnguoiapp.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.SettingMenuAdapter;
import com.example.phuong.viectimnguoiapp.eventBus.BusProvider;
import com.example.phuong.viectimnguoiapp.fragments.CreateNewFragment_;
import com.example.phuong.viectimnguoiapp.fragments.HistoryFragment_;
import com.example.phuong.viectimnguoiapp.fragments.JobsPingFragment_;
import com.example.phuong.viectimnguoiapp.fragments.MessageFragment_;
import com.example.phuong.viectimnguoiapp.fragments.MyJobFragment_;
import com.example.phuong.viectimnguoiapp.fragments.NewsFragment_;
import com.example.phuong.viectimnguoiapp.fragments.NotifyReportFragment_;
import com.example.phuong.viectimnguoiapp.fragments.PostSavedFrament_;
import com.example.phuong.viectimnguoiapp.fragments.SettingFragment_;
import com.example.phuong.viectimnguoiapp.fragments.UpdateInformationFragment_;
import com.example.phuong.viectimnguoiapp.objects.MenuItem;
import com.example.phuong.viectimnguoiapp.service.NotifyService;
import com.example.phuong.viectimnguoiapp.utils.ScreenUtil;
import com.example.phuong.viectimnguoiapp.utils.SharedPreferencesUtils;
import com.example.phuong.viectimnguoiapp.utils.SubscribeSettingHelper;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 20/02/2017.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements SettingMenuAdapter.itemClick {
    public ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.toolBar)
    protected Toolbar mToolbar;
    @ViewById(R.id.drawerLayout)
    protected DrawerLayout mDrawerLayout;
    @ViewById(R.id.frLeftMenuContainer)
    protected RecyclerView mRecyclerViewMenu;
    @ViewById(R.id.rlContainer)
    protected View mRlContainer;

    @Extra
    boolean mIsSetting = false;

    private float mLastTranslate = 0.0f;
    private TextView mTvTitleToolbar;

    private List<Object> mItems;
    private SettingMenuAdapter mAdapter;

    @Override
    void inits() {
        initActionbar();
        initMenu();
        initMain();
        startService(new Intent(this, NotifyService.class));
    }

    public void initMain() {
        String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String lastId = SharedPreferencesUtils.getInstance().getLastUserId(this);
        if (TextUtils.equals(currentId, lastId)) {
            displayView(1);
        } else {
            SharedPreferencesUtils.getInstance().setLastUserId(this, currentId);
            displayView(9);
        }
    }

    public void setTitleToolbar(String title) {
        if (mTvTitleToolbar != null) {
            mTvTitleToolbar.setText(title);
        }
    }

    public String getTitleToolbar() {
        if (mTvTitleToolbar != null) {
            return mTvTitleToolbar.getText().toString();
        }
        return "";
    }

    private void initActionbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mTvTitleToolbar = (TextView) mToolbar.findViewById(R.id.tvtitleToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initMenu() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewMenu.setLayoutManager(layoutManager);
        initsData();
        mAdapter = new SettingMenuAdapter(mItems, this, this);
        mRecyclerViewMenu.setAdapter(mAdapter);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.drawerlayout_scrim));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerLayout.openDrawer(GravityCompat.START);
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
        mItems.add(new MenuItem(R.drawable.ic_user, "Thông tin cá nhân"));
        mItems.add(new MenuItem(R.drawable.ic_create, "Tạo bài đăng"));
        mItems.add(new MenuItem(R.drawable.ic_chat, "Tin nhắn"));
        mItems.add(new MenuItem(R.drawable.ic_work_menu, "Việc làm của tôi"));
        mItems.add(new MenuItem(R.drawable.ic_save, "Việc làm đã lưu"));
        mItems.add(new MenuItem(R.drawable.ic_note, "Việc đã tương tác"));
        mItems.add(new MenuItem(R.drawable.ic_timeline, "Lịch sử"));
        mItems.add(new MenuItem(R.drawable.ic_settings, "Cài đặt"));
        mItems.add(new MenuItem(R.drawable.report, "Thông báo vi phạm"));
        mItems.add(new MenuItem(R.drawable.ic_exit, "Đăng xuất"));
        mItems.add(new Integer(1));
    }

    private void displayView(int position) {
        switch (position) {
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, NewsFragment_.builder().build()).commit();
                setTitleToolbar("Bảng tin");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, UpdateInformationFragment_.builder().build()).commit();
                setTitleToolbar("Thông tin cá nhân");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, CreateNewFragment_.builder().build()).commit();
                setTitleToolbar("Tạo bài đăng");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, MessageFragment_.builder().build()).commit();
                setTitleToolbar("Tin nhắn");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 5:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, MyJobFragment_.builder().build()).commit();
                setTitleToolbar("Việc làm của tôi");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 6:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, PostSavedFrament_.builder().build()).commit();
                setTitleToolbar("Bài đăng đã lưu");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 7:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, JobsPingFragment_.builder().build()).commit();
                setTitleToolbar("Việc đã đặt chỗ");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 8:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, HistoryFragment_.builder().build()).commit();
                setTitleToolbar("Lịch sử");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 9:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, SettingFragment_.builder().build()).commit();
                setTitleToolbar("Cài đặt");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 10:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, NotifyReportFragment_.builder().build()).commit();
                setTitleToolbar("Thông báo vi phạm");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case 11:
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, NewsFragment_.builder().isLogout(true).build()).commit();
                setTitleToolbar("Bảng tin");
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
