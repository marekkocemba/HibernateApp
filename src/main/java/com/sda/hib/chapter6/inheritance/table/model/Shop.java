package com.sda.hib.chapter6.inheritance.table.model;

import javax.persistence.*;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Inheritance(strategy = InheritanceType.JOINED) // jeden typ adnotacji na raz (zmieniając je prawdopodobnie trzeba będzie kasować tabele)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name="shop_type", discriminatorType = DiscriminatorType.STRING)
@Table(name= "shops")
// W obrębie tego dziedziczenia możemy tabelki zorganizować na trzy sposoby
//TABLE_PER_CLASS każda tabela rozbita z własnym id i addresem, działaniem przypomina MappedSuperclass, z tą różnicąże mamy tutaj polimorfizm
//JOINED powstanie tabela Shop z kolumnami id i address oraz tabelki Supermarket i InternetShop będą miały one kolumny z pól dziedziczonych tylko dla siebie, lecz address i id będą oparte o informację z tabeli Shop
//SINGLE_TABLE wszystkie pola klasy Shop oraz wszystkich klas dziedziczących tj Supermarket, InternetShop znajdą sięw jednej wspólej tabeli + powstanie spacjalna kolumna po typie klasy
// jeśli korzystamy w SINGLE_TABLE ,możemy odkomentować adnotację @DiscriminatorColumn tutaj oraz @DiscriminatorValue w Klasach InternetShop i Supermarket, dzieki tym adnotacją mamy wpływ na nazwę kolumny po typach klasy oraz nazwy wrzucanych wartości
public class Shop {

    @Id
    @GeneratedValue
    private Long id;

    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
