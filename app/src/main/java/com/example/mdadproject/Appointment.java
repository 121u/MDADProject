package com.example.mdadproject;

public class Appointment {
    private String id;
    private String date;
    private String starttime;
    private String endtime;
    private String status;
    private String pid;

    public Appointment() {

    }

    public Appointment(String id, String date, String starttime, String endtime, String status, String pid) {
        this.id = id;
        this.date = date;
        this.starttime = starttime;
        this.endtime = endtime;
        this.status = status;
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
