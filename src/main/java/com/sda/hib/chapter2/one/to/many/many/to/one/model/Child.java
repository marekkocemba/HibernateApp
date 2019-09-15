package com.sda.hib.chapter2.one.to.many.many.to.one.model;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="children")
public class Child {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate dateOfBirth;

    @ManyToOne
    //@JoinColumn(name = "mom_id", referencedColumnName = "id")
    //zachowanie Hibernate gdy nie ma adnotacji:
    //Hibernate na podstawie obiektu ponizej tworzy kolumne z kluczami obcymi do tabeli moms
    // nazwa kolumny tworzona jest na zasadzie <nazwa pola poniżej>_<nazwa klucza głównego z tabeli docelowej> czyli w tym wypadku stworzona nazwa to: "mom_id",
    // wartości kolumny brane są z klucza głównego tabeli moms

    // jak łatwo siędomyślić dodanie adnotacji @JoinColumn pozwala nam na ostalanie recznie nazw oraz co bedzie traktował jako klucz główny w przeciwnej tabeli
    // wartości name i referencedColumnName, są nieszczęsliwie dobrane, bo hibernate dokładnie w tym przypadku to samo stworzy sam więc adnotacja tak naprawdę niepotrzebna
    private Mom mom;


    public Child(Mom mom, String name, LocalDate dateOfBirth) {
        this.mom = mom;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public Child(){
        // to samo co w bezparametrowym konstruktorze w klasie Mom
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Mom getMom() {
        return mom;
    }

    public void setMom(Mom mom) {
        this.mom = mom;
    }
}
