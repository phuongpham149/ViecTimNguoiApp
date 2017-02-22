package com.example.phuong.viectimnguoiapp.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.adapters.NewsAdapter;
import com.example.phuong.viectimnguoiapp.objects.NewItem;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by phuong on 21/02/2017.
 */
@EFragment(R.layout.fragment_news)
public class NewsFragment extends BaseFragment {
    @ViewById(R.id.recyclerViewNews)
    RecyclerView mRecyclerViewNews;

    private NewsAdapter mAdapter;
    private List<NewItem> mNews;

    @Override
    void inits() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        initsData();
        mRecyclerViewNews.setLayoutManager(layoutManager);
        mAdapter = new NewsAdapter(mNews, getContext());
        mRecyclerViewNews.setAdapter(mAdapter);
    }

    public void initsData() {
        mNews = new ArrayList<>();
        NewItem newItem = new NewItem(new Date(), "Tìm thợ sửa mái nhà", "Làm vào ngày 23/2/2017", 1);
        mNews.add(newItem);
        mNews.add(newItem);
    }
}
