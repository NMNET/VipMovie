package com.nmnet.vipmovie.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nmnet.vipmovie.R;

/**
 * Created by NMNET on 2017/3/8 0008.
 */

public class BaseActionBarActivity extends AppCompatActivity {

    private TextView mTvTitle;
    private FrameLayout mFlContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_action_bar);
        initData();
        initView();
        findViews();
    }

    protected void initData() {

    }

    protected void findViews() {

    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
        mTvTitle.setText(setTitle());
        LayoutInflater.from(this).inflate(setContentLayout(), mFlContent, true);
    }

    public int setContentLayout() {
        return 0;
    }


    public String setTitle() {
        return null;
    }

}
