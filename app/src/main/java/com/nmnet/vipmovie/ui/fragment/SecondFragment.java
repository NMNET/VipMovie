package com.nmnet.vipmovie.ui.fragment;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
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
import com.nmnet.vipmovie.bean.SimpleCallLog;
import com.nmnet.vipmovie.bean.SimpleContact;
import com.nmnet.vipmovie.bean.SimpleSms;
import com.nmnet.vipmovie.utils.PermissionUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecondFragment extends ParentBaseSwipeFragment {
    private static final int CALL_LOG = 20;
    private RecyclerView mRvCallLog;
    private TextView mTvSearchCallLog;
    private Handler mHandler;

    @Override
    public View setCustomerContent(FrameLayout frameLayout) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_second, frameLayout, false);
    }

    @Override
    protected void findViews(FrameLayout flCustomerContent) {
        super.findViews(flCustomerContent);
        mRvCallLog = (RecyclerView) flCustomerContent.findViewById(R.id.rv_call_log);
        mTvSearchCallLog = (TextView) flCustomerContent.findViewById(R.id.tv_search_call_log);
        handlerResult();
    }


    private void handlerResult() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == CALL_LOG) {
                        ArrayList<SimpleCallLog> simpleCallLogs = (ArrayList<SimpleCallLog>) (msg.obj);
                        mTvSearchCallLog.setText("点击搜索" + simpleCallLogs.size() + "条通话记录");
                        setRecyclerView(simpleCallLogs);
                        hideSwipeLoading();
                    }
                }
            };
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SimpleCallLog> simpleCallLogs = getCallLogList();
                Message msg = new Message();
                msg.what = CALL_LOG;
                msg.obj = simpleCallLogs;
                mHandler.sendMessage(msg);
            }
        }).run();


    }

    private void setRecyclerView(ArrayList<SimpleCallLog> simpleCallLogs) {
        mRvCallLog.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCallLog.setAdapter(new GeneRecycleAdapter(simpleCallLogs, getContext()) {
            @Override
            protected View onCreateItemHolder(ViewGroup viewGroup) {
                return LayoutInflater.from(getContext()).inflate(R.layout.item_call_log_list, viewGroup, false);
            }

            @Override
            protected void onBindItemHolder(View view) {
                SimpleCallLog simpleCallLog = (SimpleCallLog) view.getTag();
                TextView tvCallLogName = (TextView) ViewHolderUtil.getView(view, R.id.tv_call_log_name);
                TextView tvCallLogNumber = (TextView) ViewHolderUtil.getView(view, R.id.tv_call_log_number);
                TextView tvCallLogStatus = (TextView) ViewHolderUtil.getView(view, R.id.tv_call_log_status);
                TextView tvCallLogTime = (TextView) ViewHolderUtil.getView(view, R.id.tv_call_log_time);

                tvCallLogName.setText(simpleCallLog.getName());
                tvCallLogNumber.setText(simpleCallLog.getPhone());
                String type = simpleCallLog.getType();
                if ("未接".equals(type)) {
                    tvCallLogStatus.setTextColor(Color.parseColor("#ff0000"));
                }
                tvCallLogStatus.setText(type);
                tvCallLogTime.setText(simpleCallLog.getDate());
            }
        });

    }


    @Override
    public void onSwipeRefresh() {
        super.onSwipeRefresh();
        handlerResult();
    }

    private List<SimpleCallLog> getCallLogList() {
        final ContentResolver resolver = getActivity().getContentResolver();
        final List<SimpleCallLog> list = new ArrayList<>();
        PermissionUtil.check(getActivity(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.WRITE_CONTACTS}, 1, new PermissionUtil.ICall() {
            @Override
            public void call() {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.CACHED_NAME
                                , CallLog.Calls.NUMBER
                                , CallLog.Calls.DATE
                                , CallLog.Calls.DURATION
                                , CallLog.Calls.TYPE}
                        , null, null, CallLog.Calls.DEFAULT_SORT_ORDER
                );

                // 3.通过Cursor获得数据
                while (cursor.moveToNext()) {
                    SimpleCallLog simpleCallLog = new SimpleCallLog();
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(dateLong));
                    int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    String typeString = "";
                    switch (type) {
                        case CallLog.Calls.INCOMING_TYPE:
                            typeString = "打入";
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            typeString = "打出";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            typeString = "未接";
                            break;
                        default:
                            break;
                    }
                    simpleCallLog.setName(TextUtils.isEmpty(name) ? "未备注联系人" : name);
                    simpleCallLog.setPhone(number);
                    simpleCallLog.setDate(date);
                    simpleCallLog.setDuration((duration / 60) + "分钟");
                    simpleCallLog.setType(typeString);
                    list.add(simpleCallLog);
                }
            }
        });

        return list;
    }


}
