package com.tinyms.app;

/**
 * Created by tinyms on 13-12-22.
 */
public class Match {
    private int id;
    private String season = "";
    private String main = "";
    private String client = "";
    private String score = "";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
