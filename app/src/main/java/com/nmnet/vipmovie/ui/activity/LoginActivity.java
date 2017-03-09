package com.nmnet.vipmovie.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nmnet.vipmovie.R;

/**
 * Created by NMNET on 2017/3/9 0009.
 */

public class LoginActivity extends BaseActionBarActivity implements View.OnClickListener {
    private String mType;
    public static String TYPE_LOGIN = "type_login";
    public static String TYPE_REGIST = "type_regist";
    private EditText mEtName;
    private EditText mEtPswd;
    private Button mBtnConfirm;


    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
    }

    @Override
    protected void findViews() {
        super.findViews();
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPswd = (EditText) findViewById(R.id.et_pswd);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        if (TYPE_REGIST.equals(mType)) {
            mBtnConfirm.setText("注册");
        } else {
            mBtnConfirm.setText("登录");
        }

        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    public int setContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public String setTitle() {
        return TYPE_REGIST.equals(mType) ? "注册用户" : "登录";
    }

    @Override
    public void onClick(View v) {
        if (TYPE_REGIST.equals(mType)) {
            doRegister();
        } else {
            doLogin();
        }
    }

    private void doLogin() {


    }

    private void doRegister() {

    }
}
