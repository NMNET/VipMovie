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
import com.nmnet.vipmovie.utils.ContentResolerUtil;
import com.nmnet.vipmovie.utils.PermissionUtil;

import java.security.PublicKey;
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

    public void holdContacts(List<SimpleSms> simpleSmses) {
        for (SimpleSms simpleSmse : simpleSmses) {
            simpleSmse.setTimes(1);
        }
        for (int i = 0; i < simpleSmses.size(); i++)  //外循环是循环的次数
        {
            for (int j = simpleSmses.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
            {
                if (simpleSmses.get(i).getPhone().equals(simpleSmses.get(j).getPhone())) {
                    simpleSmses.get(i).setTimes(simpleSmses.get(i).getTimes() + 1);
                    simpleSmses.remove(j);
                }
            }
        }
    }

    private void handlerResult() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == SMGS) {
                        ArrayList<SimpleSms> contacts = (ArrayList<SimpleSms>) (msg.obj);
                        mTvSearchMsg.setText("点击搜索" + contacts.size() + "条短信");
                        holdContacts(contacts);
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
                        List<SimpleSms> simpleSmses = ContentResolerUtil.getAllMsgs(getContext());
                        Message msg = new Message();
                        msg.what = SMGS;
                        msg.obj = simpleSmses;
                        mHandler.sendMessage(msg);
                    }
                }).run();
            }
        });

    }

    private void setRecyclerView(ArrayList<SimpleSms> msgs) {
        mRvMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvMsg.setAdapter(new GeneRecycleAdapter(msgs, getContext()) {
            @Override
            protected View onCreateItemHolder(ViewGroup viewGroup) {
                return LayoutInflater.from(getContext()).inflate(R.layout.item_msgs_list, viewGroup, false);
            }

            @Override
            protected void onBindItemHolder(View view) {
                final SimpleSms simpleSms = (SimpleSms) view.getTag();
                TextView tvMsgName = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_name);
                TextView tvMsgBody = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_body);
                TextView tvMsgStatus = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_status);
                TextView tvMsgTime = (TextView) ViewHolderUtil.getView(view, R.id.tv_msg_time);

                tvMsgName.setText(simpleSms.getName() + "(" + simpleSms.getTimes() + ")");
                tvMsgBody.setText(simpleSms.getContent());
                tvMsgStatus.setText(simpleSms.getStatus());
                tvMsgTime.setText(simpleSms.getTime());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentResolerUtil.doSendSMS(getContext(), simpleSms.getPhone(), "");
                    }
                });
            }
        });
    }

    @Override
    public void onSwipeRefresh() {
        super.onSwipeRefresh();
        handlerResult();
    }

}
