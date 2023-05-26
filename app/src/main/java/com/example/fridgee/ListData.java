package com.example.fridgee;

public class ListData {
    String name;
    String addDate;
    String expireDate;
    String reminderDate;
    String location;
    String weight;
    String notes;
    String image;
    String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
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

    public String getImage() {
        return image;
    }

    public ListData(String name, String date, String expiryDate, String remDate, String loc, String note, String weights, String images) {
        this.name = name;
        this.addDate = date;
        this.expireDate = expiryDate;
        this.reminderDate = remDate;
        this.location = loc;
        this.notes = note;
        this.weight = weights;
        this.image = images;
    }
//   No-argument constructor
    public ListData() {

    }
}
