package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.SendMessageActivity_;
import com.example.phuong.viectimnguoiapp.adapters.ContactAdapter;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by phuong on 21/02/2017.
 */
@EFragment(R.layout.fragment_contact)
public class MessageFragment extends BaseFragment implements ContactAdapter.onItemClickListener {
    @ViewById(R.id.recyclerViewContact)
    RecyclerView mRecyclerViewContact;

    private List<String> mUserContact = new ArrayList<>();
    private List<String> mUserNameContact = new ArrayList<>();
    private ContactAdapter mAdapter;
    private String idUser;
    private SharedPreferences mSharedPreferences;
    private Firebase mFirebaseMessage;
    private Firebase mFirebaseUser;

    @Override
    void inits() {
        mSharedPreferences = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        idUser = mSharedPreferences.getString(Constant.ID_USER_LOGIN, "");
        getListContactMessage();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewContact.setLayoutManager(layoutManager);
        mAdapter = new ContactAdapter(mUserNameContact, getContext(), this);
        mRecyclerViewContact.setAdapter(mAdapter);
    }

    public void getListContactMessage() {
        mFirebaseMessage = new Firebase("https://viectimnguoi-469e6.firebaseio.com/messages");
        mFirebaseMessage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String[] user = dataSnapshot.getKey().split("_");
                if (user[0].trim().equals(idUser)) {
                    mUserContact.add(user[1]);
                }
                getDataUserName();
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void itemClickListener(int position) {
        SendMessageActivity_.intent(this).idUserContact(mUserContact.get(position)).start();
    }

    public void getDataUserName(){
        mFirebaseUser = new Firebase("https://viectimnguoi-469e6.firebaseio.com/users");
        mFirebaseUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                for(String idUser : mUserContact){
                    if(idUser.equals(map.get("id").toString())){
                        mUserNameContact.add(map.get("username").toString());
                    }
                }
                mAdapter.notifyDataSetChanged();
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
