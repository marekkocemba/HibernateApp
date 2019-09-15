package com.sda.hib.chapter6.inheritance.table.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "supermarkets")
//@DiscriminatorValue("supermarket")
public class Supermarket extends Shop {

    private Long parkingPlaces;

    public Supermarket(String address, Long parkingPlaces){
        setAddress(address);
        this.parkingPlaces = parkingPlaces;
    }

    public Supermarket(){

    }

    public Long getParkingPlaces() {
        return parkingPlaces;
    }

    public void setParkingPlaces(Long parkingPlaces) {
        this.parkingPlaces = parkingPlaces;
    }
}
