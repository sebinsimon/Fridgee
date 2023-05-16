package com.example.fridgee;

public class ListData {
    String name;
    String addDate;
    String expireDate;
    String reminderDate;
    String location;
    String weight;
    String notes;

//    int image;


    public String getName() {
        return name;
    }

    public String getAddDate() {
        return addDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public String getLocation() {
        return location;
    }

    public String getWeight() {
        return weight;
    }

    public String getNotes() {
        return notes;
    }

    public ListData(String name, String date, String expiryDate, String remDate, String loc, String note, String weights) {
        this.name = name;
        this.addDate = date;
        this.expireDate = expiryDate;
        this.reminderDate = remDate;
        this.location = loc;
        this.notes = note;
        this.weight = weights;
    }
//   No-argument constructor
    public ListData() {

    }
}
