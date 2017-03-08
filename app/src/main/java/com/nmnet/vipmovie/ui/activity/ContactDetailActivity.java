package com.nmnet.vipmovie.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nmnet.vipmovie.R;
import com.nmnet.vipmovie.bean.SimpleContact;
import com.nmnet.vipmovie.utils.ContentResolerUtil;

/**
 * Created by NMNET on 2017/3/8 0008.
 */

public class ContactDetailActivity extends BaseActionBarActivity implements View.OnClickListener {

	private SimpleContact mContact;
	private TextView mTvName;
	private TextView mTvPhone;
	private TextView mTvCall;
	private TextView mTvMsg;
	private TextView mTvEditContact;
	private TextView mTvDeleteContact;
	private TextView mTvAddContact;
	private TextView mTvEmail;
	private TextView mTvAddress;

	@Override
	protected void initData() {
		super.initData();
		Intent intent = getIntent();
		mContact = intent.getParcelableExtra("contact");
	}

	@Override
	protected void findViews() {
		super.findViews();
		mTvName = (TextView) findViewById(R.id.tv_name);
		mTvPhone = (TextView) findViewById(R.id.tv_phone);
		mTvEmail = (TextView) findViewById(R.id.tv_email);
		mTvAddress = (TextView) findViewById(R.id.tv_address);
		mTvCall = (TextView) findViewById(R.id.tv_call);
		mTvMsg = (TextView) findViewById(R.id.tv_msg);
		mTvEditContact = (TextView) findViewById(R.id.tv_edit_contact);
		mTvDeleteContact = (TextView) findViewById(R.id.tv_delete_contact);
		mTvAddContact = (TextView) findViewById(R.id.tv_add_contact);

		setViews();
	}

	private void setViews() {
		mTvName.setText(mContact.getName());
		mTvPhone.setText("电话: " + (TextUtils.isEmpty(mContact.getPhone()) ? "" : mContact.getPhone()));
		mTvEmail.setText("邮件: " + (TextUtils.isEmpty(mContact.getEmail()) ? "" : mContact.getEmail()));
		mTvAddress.setText("地址: " + (TextUtils.isEmpty(mContact.getAddress()) ? "" : mContact.getAddress()));

		mTvCall.setOnClickListener(this);
		mTvMsg.setOnClickListener(this);
		mTvEditContact.setOnClickListener(this);
		mTvDeleteContact.setOnClickListener(this);
		mTvAddContact.setOnClickListener(this);
	}

	@Override
	public String setTitle() {
		return "联系人详情";
	}

	@Override
	public int setContentLayout() {
		return R.layout.activity_contact_detail;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_call:
				ContentResolerUtil.callPhone(mContact.getPhone(), this);
				break;
			case R.id.tv_msg:
				ContentResolerUtil.doSendSMS(this, mContact.getPhone(), "");
				break;
			case R.id.tv_edit_contact:
				Intent intentEdit = new Intent(this, EditContactActivity.class);
				intentEdit.putExtra("contact", mContact);
				startActivityForResult(intentEdit, 2);
				break;
			case R.id.tv_delete_contact:

				new AlertDialog
						.Builder(this)
						.setMessage("确认删除该联系人吗？")
						.setNegativeButton("取消", null)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								try {
									ContentResolerUtil.deleteContact(ContactDetailActivity.this, mContact.getName());
									Toast.makeText(ContactDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
									finish();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						})
						.create()
						.show();
				break;
			case R.id.tv_add_contact:
				Intent intentAdd = new Intent(this, EditContactActivity.class);
				startActivityForResult(intentAdd, 1);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			mContact = data.getParcelableExtra("contact");
			setViews();
		}
	}
}
