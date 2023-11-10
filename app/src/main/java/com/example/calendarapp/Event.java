package com.example.calendarapp;

public class Event {

    public String title;
    public String description;
    public String location;
    //The below two vars might have to switch their datatypes
    //idk how exactly date and time should be stored
    public String time;
    public String date;

    public Event(String title, String description, String location, String time, String date){
        this.title = title;
        this.description = description;
        this.location = location;
        this.time = time;
        this.date = date;
    }

}
