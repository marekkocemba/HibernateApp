package com.sda.hib.chapter6.inheritance.mapped.superclass.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "dogs")
public class Dog extends Animal {

    private Boolean bone;

    public Boolean getBone() {
        return bone;
    }

    public void setBone(Boolean bone) {
        this.bone = bone;
    }
}
