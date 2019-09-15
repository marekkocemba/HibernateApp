package com.sda.hib.chapter6.inheritance.mapped.superclass.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "cat")
public class Cat extends Animal {

    private Boolean milk;

    public Boolean getMilk() {
        return milk;
    }

    public void setMilk(Boolean milk) {
        this.milk = milk;
    }
}
