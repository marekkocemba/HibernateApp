package com.sda.hib.chapter2.one.to.many.many.to.one.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "moms")
public class Mom {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer age;

    // mappedBy = "mom" jest brane z pola Child.mom
    @OneToMany(mappedBy = "mom")
    private List<Child> children;

    public Mom(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Mom(){
        // W rozdziale pierwszym obiekt User inicjowany jest konstruktorem domyslnym a potem nadawalismy wartości pole po polu.
        // W tym rozdziale inicjujemy obiekty typu Mom przez konstruktor Mom(String name, Integer age),
        // tak więc nie korzystamy już z niejawnego konstruktora domyślnego- bezparametrowego
        // Teraz musimy stworzyć jawnie konstruktor bezparametrowy ponieważ ,hibernate tworząc obiekty
        // korzysta z refleksji Class<T>.newInstance() Bez tego konstruktora hibernate sypnie wyjątkiem.
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }
}
