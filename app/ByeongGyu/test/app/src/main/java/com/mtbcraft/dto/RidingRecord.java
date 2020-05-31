package com.mtbcraft.dto;

import java.text.ParseException;

public class RidingRecord {
    public String rr_num;
    public String rr_rider;
    public String rr_date;
    public String rr_distance;
    public String rr_topspeed;
    public String rr_avgspeed;
    public String rr_high;
    public String rr_gpx;
    public String rr_open;
    public String rr_breaktime;
    public String rr_time;
    public String rr_area;

    public String getRr_num() {
        return rr_num;
    }

    public void setRr_num(String rr_num) {
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

    public void setRr_date(String rr_date) throws ParseException {

        this.rr_date = rr_date;
    }

    public String getRr_distance() {
        return rr_distance;
    }

    public void setRr_distance(String rr_distance) {
        this.rr_distance = rr_distance;
    }

    public String getRr_topspeed() {
        return rr_topspeed;
    }

    public void setRr_topspeed(String rr_topspeed) {
        this.rr_topspeed = rr_topspeed;
    }

    public String getRr_avgspeed() {
        return rr_avgspeed;
    }

    public void setRr_avgspeed(String rr_avgspeed) {
        this.rr_avgspeed = rr_avgspeed;
    }

    public String getRr_high() {
        return rr_high;
    }

    public void setRr_high(String rr_high) {
        this.rr_high = rr_high;
    }

    public String getRr_gpx() {
        return rr_gpx;
    }

    public void setRr_gpx(String rr_gpx) {
        this.rr_gpx = rr_gpx;
    }

    public String getRr_open() {
        return rr_open;
    }

    public void setRr_open(String rr_open) {
        this.rr_open = rr_open;
    }

    public String getRr_breaktime() {
        return rr_breaktime;
    }

    public void setRr_breaktime(String rr_breaktime) {
        this.rr_breaktime = rr_breaktime;
    }

    public String getRr_time() {
        return rr_time;
    }

    public void setRr_time(String rr_time) {
        this.rr_time = rr_time;
    }

    public String getRr_area() {
        return rr_area;
    }

    public void setRr_area(String rr_area) {
        this.rr_area = rr_area;
    }
}
