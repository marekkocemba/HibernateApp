package com.sda.hib.chapter3.one.to.one.model;

import javax.persistence.*;

@Entity
@Table(name = "husbands")
public class Husband {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToOne
    //@JoinColumn(name = "wife_id", referencedColumnName = "id") // adnotacja działa dokładnie tak samo jak w klasie Child, linia 18,
    // nazw nie przemapowywujemy w jakiś niestandardowy schemat nazw, hibernate ustali dokładnie takie same wartości, czyli możemy sobie poradzić w tym wypadku bez tej adnotacji
    private Wife wife;

    public Husband(String name) {
        this.name = name;
    }

    public Husband(){
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

    public Wife getWife() {
        return wife;
    }

    public void setWife(Wife wife) {
        this.wife = wife;
    }
}
