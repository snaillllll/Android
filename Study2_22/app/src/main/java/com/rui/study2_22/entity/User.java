package com.rui.study2_22.entity;

public class User {
    public int id;
    public String name;
    public boolean married;

    public User(){}

    public User(String name, boolean married) {
        this.name = name;
        this.married = married;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", married=" + married +
                '}';
    }
}
