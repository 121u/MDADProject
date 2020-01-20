package com.example.mdadproject.Models;

public class Queue {
    private String queue_id;
    private String queue_no;
    private String username;
    private String queue_date;
    private String queue_time;

    public Queue() {

    }

    public Queue(String queue_id, String queue_no, String username, String queue_date, String queue_time) {
        this.queue_id = queue_id;
        this.queue_no = queue_no;
        this.username = username;
        this.queue_date = queue_date;
        this.queue_time = queue_time;
    }

    public String getQueue_id() {
        return queue_id;
    }

    public void setQueue_id(String queue_id) {
        this.queue_id = queue_id;
    }

    public String getQueue_no() {
        return queue_no;
    }

    public void setQueue_no(String queue_no) {
        this.queue_no = queue_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQueue_date() {
        return queue_date;
    }

    public void setQueue_date(String queue_date) {
        this.queue_date = queue_date;
    }

    public String getQueue_time() {
        return queue_time;
    }

    public void setQueue_time(String queue_time) {
        this.queue_time = queue_time;
    }
}
