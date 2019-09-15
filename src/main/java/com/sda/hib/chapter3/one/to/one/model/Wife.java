package com.sda.hib.chapter3.one.to.one.model;

import javax.persistence.*;

@Entity
@Table(name = "wifes")
public class Wife {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    // mappedBy = "wife" jest brane z pola Husbant.wife
    @OneToOne(mappedBy = "wife")
    private Husband husband;

    public Wife(String name) {
        this.name = name;
    }

    public Wife(){
        // to samo co w bezparametrowym konstruktorze w klasie Mom (chapter 2)
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Husband getHusband() {
        return husband;
    }

    public void setHusband(Husband husband) {
        this.husband = husband;
    }
}
