package com.example.phuong.viectimnguoiapp.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.SendMessageActivity_;
import com.example.phuong.viectimnguoiapp.adapters.ContactAdapter;
import com.example.phuong.viectimnguoiapp.utils.Constant;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by phuong on 21/02/2017.
 */
@EFragment(R.layout.fragment_contact)
public class MessageFragment extends BaseFragment implements ContactAdapter.onItemClickListener {
    @ViewById(R.id.recyclerViewContact)
    RecyclerView mRecyclerViewContact;

    private List<String> mUserContact;
    private List<String> mContactMessage;
    private ContactAdapter mAdapter;
    private String idUser;
    private SharedPreferences mSharedPreferences;

    @Override
    void inits() {
        mSharedPreferences = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        idUser = mSharedPreferences.getString(Constant.ID_USER_LOGIN, "");

        initData();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewContact.setLayoutManager(layoutManager);
        mAdapter = new ContactAdapter(mUserContact, getContext(), this);
        mRecyclerViewContact.setAdapter(mAdapter);
    }

    public void initData() {
        String url = "https://viectimnguoi-469e6.firebaseio.com/messages.json";
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        mContactMessage = new ArrayList<>();
        mUserContact = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("null")) {
                    Toast.makeText(getContext(), "No message", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        mContactMessage = getKeyElement(obj);
                        for (String contact : mContactMessage) {
                            String[] user = contact.split("_");
                            if (user[0].trim().equals(idUser)) {
                                //tu id user --> username
                                mUserContact.add(user[1]);
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    public List<String> getKeyElement(JSONObject json) {
        Iterator keysToCopyIterator = json.keys();
        List<String> keysList = new ArrayList<String>();
        while (keysToCopyIterator.hasNext()) {
            String key = (String) keysToCopyIterator.next();
            keysList.add(key);
        }
        return keysList;
    }

    @Override
    public void itemClickListener(int position) {
        SendMessageActivity_.intent(this).idUserContact(mUserContact.get(position)).start();
    }
}
