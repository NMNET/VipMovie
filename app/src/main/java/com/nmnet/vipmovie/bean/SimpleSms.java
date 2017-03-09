package com.nmnet.vipmovie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NMNET on 2017/3/8 0008.
 */

public class SimpleSms implements Parcelable {

    private String name;
    private String phone;
    private String content;
    private String time;
    private String status;
    private int times;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SimpleSms{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
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
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeString(this.status);
        dest.writeInt(this.times);
    }

    public SimpleSms() {
    }

    protected SimpleSms(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.status = in.readString();
        this.times = in.readInt();
    }

    public static final Parcelable.Creator<SimpleSms> CREATOR = new Parcelable.Creator<SimpleSms>() {
        @Override
        public SimpleSms createFromParcel(Parcel source) {
            return new SimpleSms(source);
        }

        @Override
        public SimpleSms[] newArray(int size) {
            return new SimpleSms[size];
        }
    };
}
