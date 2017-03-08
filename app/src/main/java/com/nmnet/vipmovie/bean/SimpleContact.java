package com.nmnet.vipmovie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NMNET on 2017/3/8 0008.
 */

public class SimpleContact implements Parcelable {
	private String name;
	private String phone;
	private String address;
	private String email;
	private String place;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Contact{" +
				"name='" + name + '\'' +
				", phone='" + phone + '\'' +
				", address='" + address + '\'' +
				'}';
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.phone);
		dest.writeString(this.address);
		dest.writeString(this.email);
		dest.writeString(this.place);
		dest.writeString(this.id);
	}

	public SimpleContact() {
	}

	protected SimpleContact(Parcel in) {
		this.name = in.readString();
		this.phone = in.readString();
		this.address = in.readString();
		this.email = in.readString();
		this.place = in.readString();
		this.id = in.readString();
	}

	public static final Parcelable.Creator<SimpleContact> CREATOR = new Parcelable.Creator<SimpleContact>() {
		@Override
		public SimpleContact createFromParcel(Parcel source) {
			return new SimpleContact(source);
		}

		@Override
		public SimpleContact[] newArray(int size) {
			return new SimpleContact[size];
		}
	};
}
