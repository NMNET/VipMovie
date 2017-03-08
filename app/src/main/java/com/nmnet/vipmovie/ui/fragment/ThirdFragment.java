package com.nmnet.vipmovie.ui.fragment;


import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nmnet.parentlib.adapter.GeneRecycleAdapter;
import com.nmnet.parentlib.ui.fragment.ParentBaseSwipeFragment;
import com.nmnet.parentlib.utils.ViewHolderUtil;
import com.nmnet.vipmovie.R;
import com.nmnet.vipmovie.bean.SimpleContact;
import com.nmnet.vipmovie.ui.activity.ContactDetail;
import com.nmnet.vipmovie.ui.activity.MyCenterActivity;
import com.nmnet.vipmovie.utils.PermissionUtil;

import java.util.ArrayList;

public class ThirdFragment extends ParentBaseSwipeFragment implements View.OnClickListener {

    private static final int CONTACTS = 10;
    private RecyclerView mRvContact;
    private Handler mHandler;
    private TextView mTvSearchContact;

    @Override
    public View setCustomerContent(FrameLayout frameLayout) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_third, frameLayout, false);
    }

    @Override
    public void onSwipeRefresh() {
        super.onSwipeRefresh();
        handlerResult();
    }

    @Override
    protected void findViews(FrameLayout flCustomerContent) {
        super.findViews(flCustomerContent);
        mRvContact = (RecyclerView) flCustomerContent.findViewById(R.id.rv_contacts);
        mTvSearchContact = (TextView) flCustomerContent.findViewById(R.id.tv_search_contact);
        flCustomerContent.findViewById(R.id.iv_my_center).setOnClickListener(this);

        handlerResult();
    }

    private void handlerResult() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == CONTACTS) {
                        ArrayList<SimpleContact> contacts = (ArrayList<SimpleContact>) (msg.obj);
                        mTvSearchContact.setText("点击搜索" + contacts.size() + "位联系人");
                        setRecyclerView(contacts);
                        hideSwipeLoading();
                    }
                }
            };
        }

        PermissionUtil.check(getActivity(), new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 1, new PermissionUtil.ICall() {
            @Override
            public void call() {
                showSwipeLoading();
                getContacts();
            }
        });

    }

    private void setRecyclerView(ArrayList<SimpleContact> contacts) {
        mRvContact.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContact.setAdapter(new GeneRecycleAdapter(contacts, getContext()) {
            @Override
            protected View onCreateItemHolder(ViewGroup viewGroup) {
                return LayoutInflater.from(getContext()).inflate(R.layout.item_contact_list, viewGroup, false);
            }

            @Override
            protected void onBindItemHolder(View view) {
                SimpleContact contact = (SimpleContact) view.getTag();
                TextView tvContactName = (TextView) ViewHolderUtil.getView(view, R.id.tv_contact_name);
                TextView tvContactPhone = (TextView) ViewHolderUtil.getView(view, R.id.tv_contact_phone);
                tvContactName.setText(contact.getName());
                tvContactPhone.setText(contact.getPhone());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ContactDetail.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }


    private void getContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<SimpleContact> contacts = readContact();
                Message msg = new Message();
                msg.what = CONTACTS;
                msg.obj = contacts;
                Log.d("ThirdFragment", "contacts:" + contacts);
                mHandler.sendMessage(msg);
            }
        }).run();
    }

    private ArrayList<SimpleContact> readContact() {
        // 首先,从raw_contacts中读取联系人的id("contact_id")
        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码
        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        ArrayList<SimpleContact> list = new ArrayList<>();
        // 从raw_contacts中读取联系人的id("contact_id")
        Cursor rawContactsCursor = getContext().getContentResolver().query(rawContactsUri, new String[]{"contact_id"}, null, null, null);
        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(0);
                // System.out.println(contactId);
                // 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
                Cursor dataCursor = getContext().getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?",
                        new String[]{contactId}, null);
                if (dataCursor != null) {
                    SimpleContact contact = new SimpleContact();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            contact.setPhone(data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            contact.setName(data1);
                        }
                    }
                    list.add(contact);
                    dataCursor.close();
                }
            }
            rawContactsCursor.close();
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MyCenterActivity.class);
        startActivity(intent);

    }
}
