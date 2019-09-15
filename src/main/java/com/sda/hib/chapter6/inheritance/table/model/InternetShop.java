package com.sda.hib.chapter6.inheritance.table.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "internet_shops")
//@DiscriminatorValue("internet")
public class InternetShop extends Shop {
    private String www;

    public InternetShop(String address, String www){
        setAddress(address);
        this.www = www;
    }
    public InternetShop(){

    }
    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }
}
