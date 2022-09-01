package com.rohan.jms.messagestructure;

public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }
}