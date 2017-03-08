package com.nmnet.vipmovie.ui.fragment;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nmnet.vipmovie.R;
import com.nmnet.vipmovie.ui.widget.CallKeyboradView;
import com.nmnet.vipmovie.utils.ContentResolerUtil;
import com.nmnet.vipmovie.utils.PermissionUtil;

import org.w3c.dom.Text;

public class FirstFragment extends Fragment {

	private TextView mTvCallNumber;
	private CallKeyboradView mCkv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_first, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTvCallNumber = (TextView) view.findViewById(R.id.tv_call_number);
		mCkv = (CallKeyboradView) view.findViewById(R.id.ckv);

		final StringBuilder sbNumber = new StringBuilder();
		mCkv.setOnKeyboradClickListener(new CallKeyboradView.OnKeyboradClickListener() {
			@Override
			public void onNumberClick(String number) {
				sbNumber.append(number);
				mTvCallNumber.setText(sbNumber.toString());
			}

			@Override
			public void onCallClick() {
				ContentResolerUtil.callPhone(sbNumber.toString(), getActivity());
			}

			@Override
			public void onClearClick() {
				sbNumber.delete(0, sbNumber.length());
				mTvCallNumber.setText(sbNumber.toString());
			}

			@Override
			public void onBackClick() {
				try {
					sbNumber.deleteCharAt(sbNumber.length() - 1);
					mTvCallNumber.setText(sbNumber.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
