package com.kim.html;

/**
 * Created by KIM on 15/12/25.
 */
public class Contact {
    private int id;
    private String name;
    private String mobile;

    public Contact(int id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
