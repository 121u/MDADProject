package com.example.mdadproject.Models;

public class Appointment {
    private String id;
    private String date;
    private String starttime;
    private String endtime;
    private String status;
    private String pid;
    private String username;
    private String petname;

    public Appointment() {

    }

    public Appointment(String id, String date, String starttime, String endtime, String status, String pid, String username, String petname) {
        this.id = id;
        this.date = date;
        this.starttime = starttime;
        this.endtime = endtime;
        this.status = status;
        this.pid = pid;
        this.username = username;
        this.petname = petname;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }
}
