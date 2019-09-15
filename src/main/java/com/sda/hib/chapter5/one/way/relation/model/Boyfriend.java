package com.sda.hib.chapter5.one.way.relation.model;

import javax.persistence.*;

@Entity
@Table(name = "boyfriends")
public class Boyfriend {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToOne
    private Girlfriend girlfriend;

    public Boyfriend(String name) {
        this.name = name;
    }

    public Boyfriend(){
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

    public Girlfriend getGirlfriend() {
        return girlfriend;
    }

    public void setGirlfriend(Girlfriend girlfriend) {
        this.girlfriend = girlfriend;
    }
}
