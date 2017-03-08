package com.nmnet.vipmovie.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nmnet.vipmovie.R;

/**
 * Created by NMNET on 2017/3/8 0008.
 */

public class CallKeyboradView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private OnKeyboradClickListener mListener;

    public CallKeyboradView(Context context) {
        this(context, null);
    }

    public CallKeyboradView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallKeyboradView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_call_keyborad, this, true);

        findViewById(R.id.tv_number0).setOnClickListener(this);
        findViewById(R.id.tv_number1).setOnClickListener(this);
        findViewById(R.id.tv_number2).setOnClickListener(this);
        findViewById(R.id.tv_number3).setOnClickListener(this);
        findViewById(R.id.tv_number4).setOnClickListener(this);
        findViewById(R.id.tv_number5).setOnClickListener(this);
        findViewById(R.id.tv_number6).setOnClickListener(this);
        findViewById(R.id.tv_number7).setOnClickListener(this);
        findViewById(R.id.tv_number8).setOnClickListener(this);
        findViewById(R.id.tv_number9).setOnClickListener(this);
        findViewById(R.id.tv_jing).setOnClickListener(this);
        findViewById(R.id.tv_mi).setOnClickListener(this);

        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.tv_clear).setOnClickListener(this);
        findViewById(R.id.iv_call).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_number0:
                mListener.onNumberClick("0");
                break;
            case R.id.tv_number1:
                mListener.onNumberClick("1");
                break;
            case R.id.tv_number2:
                mListener.onNumberClick("2");
                break;
            case R.id.tv_number3:
                mListener.onNumberClick("3");
                break;
            case R.id.tv_number4:
                mListener.onNumberClick("4");
                break;
            case R.id.tv_number5:
                mListener.onNumberClick("5");
                break;
            case R.id.tv_number6:
                mListener.onNumberClick("6");
                break;
            case R.id.tv_number7:
                mListener.onNumberClick("7");
                break;
            case R.id.tv_number8:
                mListener.onNumberClick("8");
                break;
            case R.id.tv_number9:
                mListener.onNumberClick("9");
                break;
            case R.id.tv_jing:
                mListener.onNumberClick("#");
                break;
            case R.id.tv_mi:
                mListener.onNumberClick("*");
                break;
            case R.id.tv_back:
                mListener.onBackClick();
                break;
            case R.id.tv_clear:
                mListener.onClearClick();
                break;
            case R.id.iv_call:
                mListener.onCallClick();
                break;
        }

    }


    public void setOnKeyboradClickListener(OnKeyboradClickListener listener) {
        mListener = listener;
    }

    public interface OnKeyboradClickListener {
        void onNumberClick(String number);

        void onCallClick();

        void onClearClick();

        void onBackClick();
    }

}
