package com.example.testapplication.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class RidingRecord {
    @SerializedName("rr_num")
    @Expose
    private int rr_num;
    @SerializedName("rr_rider")
    @Expose
    private String rr_rider;
    @SerializedName("rr_date")
    @Expose
    private String rr_date;
    @SerializedName("rr_distance")
    @Expose
    private int rr_distance;
    @SerializedName("rr_topspeed")
    @Expose
    private int rr_topspeed;
    @SerializedName("rr_avgspeed")
    @Expose
    private int rr_avgspeed;
    @SerializedName("rr_high")
    @Expose
    private int rr_high;
    @SerializedName("rr_gpx")
    @Expose
    private String rr_gpx;
    @SerializedName("rr_open")
    @Expose
    private int rr_open;
    @SerializedName("rr_breaktime")
    @Expose
    private int rr_breaktime;
    @SerializedName("rr_time")
    @Expose
    private int rr_time;
    @SerializedName("rr_area")
    @Expose
    private String rr_area;
    @SerializedName("rr_comp")
    @Expose
    private int rr_comp;
    @SerializedName("rr_like")
    @Expose
    private int rr_like;
    @SerializedName("rr_name")
    @Expose
    private String rr_name;

    public int getRr_num() {
        return rr_num;
    }

    public void setRr_num(int rr_num) {
        this.rr_num = rr_num;
    }

    public String getRr_rider() {
        return rr_rider;
    }

    public void setRr_rider(String rr_rider) {
        this.rr_rider = rr_rider;
    }

    public String getRr_date() {
        return rr_date;
    }

    public void setRr_date(String rr_date) {
        this.rr_date = rr_date;
    }

    public int getRr_distance() {
        return rr_distance;
    }

    public void setRr_distance(int rr_distance) {
        this.rr_distance = rr_distance;
    }

    public int getRr_topspeed() {
        return rr_topspeed;
    }

    public void setRr_topspeed(int rr_topspeed) {
        this.rr_topspeed = rr_topspeed;
    }

    public int getRr_avgspeed() {
        return rr_avgspeed;
    }

    public void setRr_avgspeed(int rr_avgspeed) {
        this.rr_avgspeed = rr_avgspeed;
    }

    public int getRr_high() {
        return rr_high;
    }

    public void setRr_high(int rr_high) {
        this.rr_high = rr_high;
    }

    public String getRr_gpx() {
        return rr_gpx;
    }

    public void setRr_gpx(String rr_gpx) {
        this.rr_gpx = rr_gpx;
    }

    public int getRr_open() {
        return rr_open;
    }

    public void setRr_open(int rr_open) {
        this.rr_open = rr_open;
    }

    public int getRr_breaktime() {
        return rr_breaktime;
    }

    public void setRr_breaktime(int rr_breaktime) {
        this.rr_breaktime = rr_breaktime;
    }

    public int getRr_time() {
        return rr_time;
    }

    public void setRr_time(int rr_time) {
        this.rr_time = rr_time;
    }

    public String getRr_area() {
        return rr_area;
    }

    public void setRr_area(String rr_area) {
        this.rr_area = rr_area;
    }

    public int getRr_comp() {
        return rr_comp;
    }

    public void setRr_comp(int rr_comp) {
        this.rr_comp = rr_comp;
    }

    public int getRr_like() {
        return rr_like;
    }

    public void setRr_like(int rr_like) {
        this.rr_like = rr_like;
    }

    public String getRr_name() {
        return rr_name;
    }

    public void setRr_name(String rr_name) {
        this.rr_name = rr_name;
    }
}
