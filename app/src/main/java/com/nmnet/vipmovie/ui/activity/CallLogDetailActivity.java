package com.nmnet.vipmovie.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nmnet.parentlib.adapter.GeneRecycleAdapter;
import com.nmnet.parentlib.utils.ViewHolderUtil;
import com.nmnet.vipmovie.R;
import com.nmnet.vipmovie.bean.SimpleCallLog;
import com.nmnet.vipmovie.utils.ContentResolerUtil;

import java.util.ArrayList;

/**
 * Created by NMNET on 2017/3/9 0009.
 */

public class CallLogDetailActivity extends BaseActionBarActivity {

    private ArrayList<SimpleCallLog> mSimpleCallLogs;
    private TextView mTvName;
    private TextView mTvPhone;
    private RecyclerView mRvItems;

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        mSimpleCallLogs = intent.getParcelableArrayListExtra("calllogdetail");
    }

    @Override
    protected void findViews() {
        super.findViews();
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mRvItems = (RecyclerView) findViewById(R.id.rv_items);

        setViews();
    }

    private void setViews() {
        mTvName.setText(mSimpleCallLogs.get(0).getName());
        mTvPhone.setText(mSimpleCallLogs.get(0).getPhone());
        mRvItems.setLayoutManager(new LinearLayoutManager(this));
        mRvItems.setAdapter(new GeneRecycleAdapter(mSimpleCallLogs, this) {
            @Override
            protected View onCreateItemHolder(ViewGroup viewGroup) {
                return LayoutInflater.from(CallLogDetailActivity.this).inflate(R.layout.item_contact_list, viewGroup, false);
            }

            @Override
            protected void onBindItemHolder(View view) {
                final SimpleCallLog simpleCallLog = (SimpleCallLog) view.getTag();
                TextView tvTime = (TextView) ViewHolderUtil.getView(view, R.id.tv_contact_name);
                TextView tvDuration = (TextView) ViewHolderUtil.getView(view, R.id.tv_contact_phone);
                TextView tvRemain = (TextView) ViewHolderUtil.getView(view, R.id.tv_remain);

                tvRemain.setVisibility(View.VISIBLE);
                tvRemain.setText(simpleCallLog.getType());
                if ("未接".equals(simpleCallLog.getType())) {
                    tvRemain.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    tvRemain.setTextColor(Color.parseColor("#BDBDBD"));
                }


                tvTime.setText("日期:" + simpleCallLog.getDate());
                tvDuration.setText("时长:" + simpleCallLog.getDuration());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentResolerUtil.callPhone(simpleCallLog.getPhone(), CallLogDetailActivity.this);
                    }
                });
            }
        });
    }

    @Override
    public int setContentLayout() {
        return R.layout.activity_call_log_detail;
    }

    @Override
    public String setTitle() {
        return "通话记录详情";
    }
}
