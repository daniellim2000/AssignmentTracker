package com.jeffsieu.tasktracker;

/**
 * Created by user on 10/7/2016.
 */
public class Channel {
    private String name;
    private String code;

    public Channel(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
