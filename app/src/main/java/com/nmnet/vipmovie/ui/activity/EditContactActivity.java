package com.nmnet.vipmovie.ui.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nmnet.vipmovie.R;
import com.nmnet.vipmovie.bean.SimpleContact;
import com.nmnet.vipmovie.utils.ContentResolerUtil;

/**
 * Created by NMNET on 2017/3/8.
 */

public class EditContactActivity extends BaseActionBarActivity implements View.OnClickListener {

	private SimpleContact mContact;
	private EditText mEtName;
	private EditText mEtPhone;
	private EditText mEtEmail;
	private EditText mEtAddress;
	private Button mBtnConfirm;

	@Override
	protected void initData() {
		super.initData();
		Intent intent = getIntent();
		mContact = intent.getParcelableExtra("contact");
	}

	@Override
	protected void findViews() {
		super.findViews();
		mEtName = (EditText) findViewById(R.id.et_name);
		mEtPhone = (EditText) findViewById(R.id.et_phone);
		mEtEmail = (EditText) findViewById(R.id.et_email);
		mEtAddress = (EditText) findViewById(R.id.et_address);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);

		setViews();
	}

	private void setViews() {
		mBtnConfirm.setOnClickListener(this);
		if (mContact == null) {
			return;
		}
		mEtName.setText(mContact.getName());
		mEtPhone.setText(mContact.getPhone());
		mEtEmail.setText(mContact.getEmail());
		mEtAddress.setText(mContact.getAddress());
	}

	@Override
	public String setTitle() {
		return mContact == null ? "新增联系人" : "编辑联系人";
	}

	@Override
	public int setContentLayout() {
		return R.layout.activity_edit_contact;
	}

	@Override
	public void onClick(View v) {
		SimpleContact simpleContact = new SimpleContact();
		simpleContact.setName(mEtName.getText().toString());
		simpleContact.setPhone(mEtPhone.getText().toString());
		simpleContact.setEmail(mEtEmail.getText().toString());
		simpleContact.setAddress(mEtAddress.getText().toString());
		Intent intent = new Intent();
		intent.putExtra("contact", simpleContact);
		try {
			if (mContact == null) {
				ContentResolerUtil.addNewContact(this, simpleContact);
				Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK, intent);
				finish();
			} else {
				ContentResolerUtil.deleteContact(this, mContact.getName());
				ContentResolerUtil.addNewContact(this, simpleContact);
				Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK, intent);
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
