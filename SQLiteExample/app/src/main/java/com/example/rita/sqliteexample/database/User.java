package com.example.rita.sqliteexample.database;

/**
 * Created by Rita on 01/04/2015.
 */
public class User {

    private int id;
    private String name;

    public User(){

    }

    public User(String name){
        this.name = name;
    }

    public User(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
}
