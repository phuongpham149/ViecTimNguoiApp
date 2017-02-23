package com.example.phuong.viectimnguoiapp.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.DetailNewActivity_;
import com.example.phuong.viectimnguoiapp.adapters.NewsAdapter;
import com.example.phuong.viectimnguoiapp.objects.NewItem;

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

    @Override
    void inits() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        initsData();
        mRecyclerViewNews.setLayoutManager(layoutManager);
        mAdapter = new NewsAdapter(mNews, getContext(), this);
        mRecyclerViewNews.setAdapter(mAdapter);
    }

    public void initsData() {
        mNews = new ArrayList<>();
        NewItem newItem = new NewItem("12/02/2017", "Tìm thợ sửa mái nhà", "Làm vào ngày 23/2/2017", 1);
        mNews.add(newItem);
        mNews.add(newItem);
    }

    @Override
    public void itemClickListener(int position) {
        DetailNewActivity_.intent(this).mNewItem(mNews.get(position)).start();
        getActivity().overridePendingTransition(R.anim.anim_slide_bottom_top, R.anim.anim_nothing);
    }
}
