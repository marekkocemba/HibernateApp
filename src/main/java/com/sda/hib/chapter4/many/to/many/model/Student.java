package com.sda.hib.chapter4.many.to.many.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String surname;

    private Integer indexNumber;

    @ManyToMany
    @JoinTable(
           // name = "adapter", //nazwa tabeli przejsciowej,bez parametru "name" hibernate utworzy nazwe ze zlepienia nazwy pierwszej tabeli z drugą
            joinColumns = @JoinColumn(name = "student_id"),//opisana adnotacja w chapter 2 i 3 joincolumns w tym przypadku dotyczy id studenta
            inverseJoinColumns = @JoinColumn(name = "course_id")//opisana adnotacja w chapter 2 i 3 inverseJoinColumns w tym przypadku dotyczy id kursu
            )
    private List<Course> courseList;

    public Student(String name, String surname, Integer indexNumber) {
        this.name = name;
        this.surname = surname;
        this.indexNumber = indexNumber;
    }

    public Student() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber = indexNumber;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
