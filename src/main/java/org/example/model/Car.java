package org.example.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;

@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class Car {

    private String id;
    private String make;
    private String model;
    private int year;

    public Car(){
        this.id = UUID.randomUUID().toString();
    }

    public Car(String make, String model, int year) {
        this.id = UUID.randomUUID().toString();
        this.make = make;
        this.model = model;
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Car{id='" + id + "', make='" + make + "', model='" + model + "', year=" + year + "}";
    }
}
