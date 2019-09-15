package com.sda.hib.chapter1.crud.model;

import com.sda.hib.chapter1.crud.enums.UserTypeEnum;

import javax.persistence.*;

@Entity //1*
@Table(name = "users") //2*
public class User {

    @Id //*3
    @GeneratedValue(strategy = GenerationType.AUTO) //*4
    private Long id;

    @Column(nullable = false, unique = true) //*5
    private String login;

    @Column(nullable = false) //*5
    private String password;

    @Column(name = "user_type", nullable = false) //*5
    @Enumerated(EnumType.STRING) //*6
    private UserTypeEnum userType;

    @Transient //*7
    private String garbage;

    /**
     1* - Hibernate dzięki tej adnotacji wie że że jest to encja. Znajdujący się w pliku konf
     hibernate.cfg.xml wpis <mapping class="com.sda.hib.chapter1.crud.model.User"/> jest tylko
     wskazaniem gdzie Hibernate może spodziewać się encji, natomiast adnotacja @Entity jest
     znacznikiem który mówi że jest to właśnie encja.
     Wybrałem nazwę "users_academy" ponieważ users jest słowkiem zastrzeżonym przez baze danych.


     2* - Hibernate tworząc tabele nadaje jej nazwę dokładnie taką samą jak nazwa encji,
     w bazie danych nazwy tabel powinny być liczbie mnogiej, po stronie kodu nazwy obiektów powinny
     być w liczbie pojedyńczej. Adnotacja @Table sprawia że podczas tworzenia tabeli, zostanie
     użyta nazwa wskazana w parametrze "name", po za zmianą nazwy tabeli można równierz ustawiać
     własne nazwy katalogów i schematów, odpowiednio parametrami "catalog", "schema".
     Można tą adnotacją jeszcze ustawić "uniqueConstraints" i "indexes" ale to już zostawiam

     3* - Adnotacja która mówi Hibernate-owi że pole poniżej jest kluczem głównym

     4* - Strategia generacji id-ka, bez tej adnotacji musimy nadawać id-ki ręcznie.
     Są cztery strategie:     AUTO, SEQUENCE, IDENTITY, TABLE;
     AUTO - Hibernate wybiera jedną z pozostałych trzech strategii którą uzna za najlepszą.
     SEQUENCE - Tworzona jest sekwencja za pomocą której przydzielane są kolejne numery id wpisom
     we wszystkich tabelach w obrębie bezy danych
     IDENTITY - Do kolumny id dopinany jesst obiekt który działa na podobnej zasadzie co sekwencja
     ale nadaje ona numery w obrębie jednej tabeli.
     TABLE - do doczytania
     Dodam jeszcze że strategia AUTO jest domyślną wartością tak więc, jeśli nie chcemy wybrać innej
     strategii to zamiast @GeneratedValue(strategy = GenerationType.AUTO)
     można użyć poprostu @GeneratedValue

     5* - Podobnie jak adnotacja @Table, tutaj przez parametr "name" wpływamy na to jak się
     będzie nazywała kolumna, dodatkowo można dodać ograniczenia "constrains" w przypadku tej klasy
     jest to wykluczenie pustych wartości - "nullable = false" oraz zapewnienie unikalności pola
     - "unique = true", adnotacja umozliwia dodanie jeszcze paru innych rodzajów ograniczeń.

     6* - @Enumerated, stosowana jest do obsługi enumów, z parametrem
     EnumType.STRING - enum będzie odkładany w kolumnie jako string z nazwy
     EnumType.ORDINAL - enum będzie odkładany w kolumnie jako liczba, liczba określana jest na podstawie
     kolejności, pierwszy element z klasy enum dostaje liczbę 0. Adnotacja @Enumerated bez jakiejkolwiek
     wartości działa w trybie ORDINAL.

     7* - Adnotacja @Transient sprawia że hibernate ignoruje wskazane pole.
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    public String getGarbage() {
        return garbage;
    }

    public void setGarbage(String garbage) {
        this.garbage = garbage;
    }
}
