package com.nmnet.vipmovie.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.nmnet.vipmovie.bean.SimpleCallLog;
import com.nmnet.vipmovie.bean.SimpleContact;
import com.nmnet.vipmovie.bean.SimpleSms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by NMNET on 2017/3/8.
 */

public class ContentResolerUtil {

	/**
	 * 获得所有联系人
	 *
	 * @param context
	 * @return
	 */
	public static ArrayList<SimpleContact> getAllContacts(Context context) {
		// 首先,从raw_contacts中读取联系人的id("contact_id")
		// 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
		// 然后,根据mimetype来区分哪个是联系人,哪个是电话号码
		Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		ArrayList<SimpleContact> list = new ArrayList<>();
		// 从raw_contacts中读取联系人的id("contact_id")
		Cursor rawContactsCursor = context.getContentResolver().query(rawContactsUri, new String[]{"contact_id"}, null, null, null);
		if (rawContactsCursor != null) {
			while (rawContactsCursor.moveToNext()) {
				String contactId = rawContactsCursor.getString(0);
				// System.out.println(contactId);
				// 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
				Cursor dataCursor = context.getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?",
						new String[]{contactId}, null);
				if (dataCursor != null) {
					SimpleContact contact = new SimpleContact();
					contact.setId(contactId);
					while (dataCursor.moveToNext()) {
						String data1 = dataCursor.getString(0);
						String mimetype = dataCursor.getString(1);
						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							contact.setPhone(data1);
						} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
							contact.setName(data1);
						} else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
							contact.setEmail(data1);
						} else if ("vnd.android.cursor.item/postal-address_v2".equals(mimetype)) {
							contact.setAddress(data1);
						}
					}
					list.add(contact);
					dataCursor.close();
				}
			}
			rawContactsCursor.close();
		}
		return list;
	}

	/**
	 * 获得所有短信
	 *
	 * @param context
	 * @return
	 */
	public static List<SimpleSms> getAllMsgs(Context context) {

		List<SimpleSms> simpleSmses = new ArrayList<>();

		final String SMS_URI_ALL = "content://sms/";
		final String SMS_URI_INBOX = "content://sms/inbox";
		final String SMS_URI_SEND = "content://sms/sent";
		final String SMS_URI_DRAFT = "content://sms/draft";

		ContentResolver cr = context.getContentResolver();
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
			simpleSms.setContent(smsbody);
			simpleSms.setTime(date);
			simpleSms.setStatus(type);
			simpleSmses.add(simpleSms);
//			simpleSms.setName(TextUtils.isEmpty(name) ? phoneNumber : name);
//			String name = queryNameByPhone(context, simpleSms.getPhone());
			simpleSms.setName(TextUtils.isEmpty(name) ? simpleSms.getPhone() : queryNameByPhone(context, simpleSms.getPhone()));
		}

		for (SimpleSms simpleSmse : simpleSmses) {
			String name = queryNameByPhone(context, simpleSmse.getPhone());
			simpleSmse.setName(TextUtils.isEmpty(name) ? simpleSmse.getPhone() : name);
		}

		return simpleSmses;
	}

	public static String queryNameByPhone(Context context, String phone) {
//		String phone = "12345678";
		//uri=  content://com.android.contacts/data/phones/filter/#
		String name = null;
		try {
			Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + phone);
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null); //从raw_contact表中返回display_name
			if (cursor.moveToFirst()) {
				name = cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;

	}

	/**
	 * 添加一个新联系人
	 *
	 * @param context
	 * @param simpleContact
	 */
	public static void addNewContact(Context context, SimpleContact simpleContact) {
		//插入raw_contacts表，并获取_id属性
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		long contact_id = ContentUris.parseId(resolver.insert(uri, values));
		//插入data表
		uri = Uri.parse("content://com.android.contacts/data");
		//add Name
		values.put("raw_contact_id", contact_id);
		values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/name");
		values.put("data2", simpleContact.getName());
		values.put("data1", simpleContact.getName());
		resolver.insert(uri, values);
		values.clear();
		//add Phone
		values.put("raw_contact_id", contact_id);
		values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
		values.put("data2", "2");   //手机
		values.put("data1", simpleContact.getPhone());
		resolver.insert(uri, values);
		values.clear();
		//add email
		values.put("raw_contact_id", contact_id);
		values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/email_v2");
		values.put("data2", "2");   //单位
		values.put("data1", simpleContact.getEmail());
		resolver.insert(uri, values);
		values.clear();
		//add address
		values.put("raw_contact_id", contact_id);
		values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/postal-address_v2");
		values.put("data2", "2");   //单位
		values.put("data1", simpleContact.getAddress());
		resolver.insert(uri, values);
	}

	/**
	 * 删除联系人
	 *
	 * @param context
	 * @param name
	 * @throws Exception
	 */
	public static void deleteContact(Context context, String name) throws Exception {
//		String name = "xzdong";
		//根据姓名求id
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, "display_name=?", new String[]{name}, null);
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(0);
			//根据id删除data中的相应数据
			resolver.delete(uri, "display_name=?", new String[]{name});
			uri = Uri.parse("content://com.android.contacts/data");
			resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
		}
	}

	/**
	 * 更新联系人
	 *
	 * @param context
	 * @param contact
	 * @throws Exception
	 */
	public static void updateContact(Context context, SimpleContact contact) throws Exception {
//		int id = 1;
		String id = contact.getId();
		Uri uri = Uri.parse("content://com.android.contacts/data");//对data表的所有数据操作
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("data1", contact.getPhone());//电话
		resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2", id});
		values.clear();
		values.put("data1", contact.getName());//姓名
		resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/name", id});
		values.clear();
		values.put("data1", contact.getAddress());//地址
		resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/postal-address_v2", id});
		values.clear();
		values.put("data1", contact.getEmail());//email
		resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/email_v2", id});
	}

	/**
	 * 插入一条短信
	 *
	 * @param context
	 * @param simpleSms
	 */
	public static void insertMsg(Context context, SimpleSms simpleSms) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		ContentValues values = new ContentValues();
		values.put("address", simpleSms.getPhone());
		values.put("type", 1);
		values.put("date", System.currentTimeMillis());
		values.put("body", simpleSms.getContent());
		values.put("person", simpleSms.getName());
		resolver.insert(uri, values);
	}

	/**
	 * 发送一条短信
	 *
	 * @param context
	 * @param phoneNumber
	 * @param message
	 */
	public static void doSendSMS(Context context, String phoneNumber, String message) {
		if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
			intent.putExtra("sms_body", message);
			context.startActivity(intent);
		}
	}

	public void deleteSMS(Context context, SimpleSms simpleSms) {
		try {
			ContentResolver resolver = context.getContentResolver();
			// Query SMS
			Uri uriSms = Uri.parse("content://sms/inbox");
			Cursor c = resolver.query(uriSms, new String[]{"_id", "thread_id"}, null, null, null);
			if (null != c && c.moveToFirst()) {
				do {
					// Delete SMS
					long threadId = c.getLong(1);
					resolver.delete(Uri.parse("content://sms/conversations/" + threadId), null, null);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		}
	}

	public static void callPhone(final String number, Activity activity) {
		final boolean[] isValid = {false};
		PermissionUtil.check(activity, Manifest.permission.CALL_PHONE, 12, new PermissionUtil.ICall() {
			@Override
			public void call() {
				try {
					//开启系统拨号器
					isValid[0] = true;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		if (isValid[0]) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_CALL);
			//uri:统一资源标示符（更广）
			intent.setData(Uri.parse("tel:" + number));
			activity.startActivity(intent);
		}

	}


}
