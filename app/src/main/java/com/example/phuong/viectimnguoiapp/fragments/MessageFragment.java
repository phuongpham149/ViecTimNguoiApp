package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.SendMessageActivity_;
import com.example.phuong.viectimnguoiapp.adapters.SendMessageAdapter;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.UserChat;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by phuong on 21/02/2017.
 */
@EFragment(R.layout.fragment_contact)
public class MessageFragment extends BaseFragment implements SendMessageAdapter.onItemClickListener {
    @ViewById(R.id.recyclerViewContact)
    RecyclerView mRecyclerViewContact;
    @ViewById(R.id.prograssBarLoading)
    ProgressBar mProgressBarLoading;

    private List<UserChat> mUserChat = new ArrayList<>();
    private SendMessageAdapter mAdapter;
    private String idUser;
    private SharedPreferences mSharedPreferences;
    private RealmHelper mData;

    @Override
    void inits() {
        mData = new RealmHelper(getActivity());
        mProgressBarLoading.setVisibility(View.VISIBLE);

        mSharedPreferences = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        idUser = mSharedPreferences.getString(Constant.ID_USER_LOGIN, "");

        if (Network.checkNetWork(getActivity(), Constant.TYPE_NETWORK) || Network.checkNetWork(getActivity(), Constant.TYPE_WIFI)) {
            getListContactMessage();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataUserName();
                }
            }, 2000);
        } else {
            mUserChat = mData.getUserChats();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewContact.setLayoutManager(layoutManager);
        mAdapter = new SendMessageAdapter(mUserChat, getContext(), this);
        mRecyclerViewContact.setAdapter(mAdapter);
        mProgressBarLoading.setVisibility(View.GONE);
    }

    public void getListContactMessage() {
        DatabaseReference mFirebaseMessage = FirebaseDatabase.getInstance().getReference("/messages");
        mFirebaseMessage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String[] user = dataSnapshot.getKey().split("_");
                if (user[0].trim().equals(idUser)) {
                    UserChat userChat = new UserChat();
                    userChat.setIdUser(user[1]);
                    mUserChat.add(userChat);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void itemClickListener(int position) {
        if (Network.checkNetWork(getActivity(), Constant.TYPE_NETWORK) || Network.checkNetWork(getActivity(), Constant.TYPE_WIFI)) {
            SendMessageActivity_.intent(this).idUserContact(mUserChat.get(position).getIdUser()).mNameUserContact(mUserChat.get(position).getUsername()).start();
        } else {
            Common.createDialog(getActivity(), "Vui lòng kiểm tra kết nối mạng");
        }
    }

    public void getDataUserName() {
        DatabaseReference mFirebaseUser = FirebaseDatabase.getInstance().getReference("/users");
        mFirebaseUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                for (UserChat userChat : mUserChat) {
                    if (userChat.getIdUser().equals(map.get("id").toString())) {
                        userChat.setUsername(map.get("username").toString());
                    }
                    mData.addUserChat(userChat);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
