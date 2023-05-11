package com.example.fridgee;

public class ListData {
    String name, addDate, expireDate, reminderDate, weight, notes;
    int image;

    public ListData(String name, String addDate, String expireDate, String reminderDate, String weight, String notes, int image) {
        this.name = name;
        this.addDate = addDate;
        this.expireDate = expireDate;
        this.reminderDate = reminderDate;
        this.weight = weight;
        this.notes = notes;
        this.image = image;
    }
}
