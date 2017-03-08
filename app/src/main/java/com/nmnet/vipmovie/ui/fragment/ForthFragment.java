package com.nmnet.vipmovie.ui.fragment;


import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.nmnet.vipmovie.bean.SimpleSms;
import com.nmnet.vipmovie.utils.PermissionUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForthFragment extends ParentBaseSwipeFragment {

    private static final int SMGS = 11;
    private RecyclerView mRvMsg;
    private TextView mTvSearchMsg;
    private Handler mHandler;

    @Override
    public View setCustomerContent(FrameLayout frameLayout) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_forth, frameLayout, false);
    }

    @Override
    protected void findViews(FrameLayout flCustomerContent) {
        super.findViews(flCustomerContent);
        mRvMsg = (RecyclerView) flCustomerContent.findViewById(R.id.rv_msg);
        mTvSearchMsg = (TextView) flCustomerContent.findViewById(R.id.tv_search_msg);

        handlerResult();
    }

    private void handlerResult() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == SMGS) {
                        ArrayList<SimpleContact> contacts = (ArrayList<SimpleContact>) (msg.obj);
                        mTvSearchMsg.setText("点击搜索" + contacts.size() + "条短信");
                        setRecyclerView(contacts);
                        hideSwipeLoading();
                    }
                }
            };
        }

        PermissionUtil.check(getActivity(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.WRITE_CONTACTS}, 1, new PermissionUtil.ICall() {
            @Override
            public void call() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<SimpleSms> simpleSmses = getSmsInPhone();
                        Message msg = new Message();
                        msg.what = SMGS;
                        msg.obj = simpleSmses;
                        mHandler.sendMessage(msg);
                    }
                }).run();
            }
        });

    }

    private void setRecyclerView(ArrayList<SimpleContact> contacts) {
        mRvMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvMsg.setAdapter(new GeneRecycleAdapter(contacts, getContext()) {
            @Override
            protected View onCreateItemHolder(ViewGroup viewGroup) {
                return LayoutInflater.from(getContext()).inflate(R.layout.item_msgs_list, viewGroup, false);
            }

            @Override
            protected void onBindItemHolder(View view) {
                SimpleSms simpleSms = (SimpleSms) view.getTag();
                TextView tvMsgName = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_name);
                TextView tvMsgBody = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_body);
                TextView tvMsgStatus = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_status);
                TextView tvMsgTime = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_time);

                tvMsgName.setText(simpleSms.getName());
                tvMsgBody.setText(simpleSms.getContent());
                tvMsgStatus.setText(simpleSms.getStatus());
                tvMsgTime.setText(simpleSms.getTime());
            }
        });
    }

    @Override
    public void onSwipeRefresh() {
        super.onSwipeRefresh();
        handlerResult();
    }

    public List<SimpleSms> getSmsInPhone() {

        List<SimpleSms> simpleSmses = new ArrayList<>();

        final String SMS_URI_ALL = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";

        ContentResolver cr = getActivity().getContentResolver();
        String[] projection = new String[]{"_id", "address", "person",
                "body", "date", "type"};
        Uri uri = Uri.parse(SMS_URI_ALL);
        Cursor cur = cr.query(uri, projection, null, null, "date desc");

        while (cur.moveToNext()) {
            SimpleSms simpleSms = new SimpleSms();

            String name;
            String phoneNumber;
            String smsbody;
            String date;
            String type;

            int nameColumn = cur.getColumnIndex("person");
            int phoneNumberColumn = cur.getColumnIndex("address");
            int smsbodyColumn = cur.getColumnIndex("body");
            int dateColumn = cur.getColumnIndex("date");
            int typeColumn = cur.getColumnIndex("type");


            name = cur.getString(nameColumn);
            phoneNumber = cur.getString(phoneNumberColumn);
            smsbody = cur.getString(smsbodyColumn);
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
            date = dateFormat.format(d);

            int typeId = cur.getInt(typeColumn);
            if (typeId == 1) {
                type = "接收";
            } else if (typeId == 2) {
                type = "发送";
            } else {
                type = "草稿";
            }

            simpleSms.setPhone(phoneNumber);
            simpleSms.setName(TextUtils.isEmpty(name) ? phoneNumber : name);
            simpleSms.setContent(smsbody);
            simpleSms.setTime(date);
            simpleSms.setStatus(type);
            simpleSmses.add(simpleSms);
        }
        return simpleSmses;
    }


}
