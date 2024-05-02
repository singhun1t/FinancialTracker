package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
private LocalDate date;
private LocalTime time;
private String type;
private String vendor;
private double price;

    public Transaction(LocalDate date, LocalTime time, String type, String vendor, double price) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.vendor = vendor;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }



    public LocalTime getTime() {
        return time;
    }



    public String getType() {
        return type;
    }


    public String getVendor() {
        return vendor;
    }


    public double getPrice() {
        return price;
    }


}

