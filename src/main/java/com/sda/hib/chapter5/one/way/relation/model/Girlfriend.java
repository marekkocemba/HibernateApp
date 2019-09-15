package com.sda.hib.chapter5.one.way.relation.model;

import javax.persistence.*;

@Entity
@Table(name = "girlfriends")
public class Girlfriend {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Girlfriend(String name) {
        this.name = name;
    }

    public Girlfriend(){
        // to samo co w bezparametrowym konstruktorze w klasie Mom (chapter 2)
    }

    // BRAK RELACJI

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
}
