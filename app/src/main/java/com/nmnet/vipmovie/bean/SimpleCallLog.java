package com.nmnet.vipmovie.bean;

/**
 * Created by NMNET on 2017/3/8 0008.
 */

public class SimpleCallLog {

    private String name;
    private String phone;
    private String type;
    private String duration;
    private String date;
    private String place;
    private int times;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CallLog{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                ", duration='" + duration + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
