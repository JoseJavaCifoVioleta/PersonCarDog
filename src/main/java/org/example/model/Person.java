package org.example.model;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class Person {

    private String id;
    private String name;
    private int age;
    private Car car;
    private List<Dog> dogs = new ArrayList<>();

    public Person(){
        this.id = UUID.randomUUID().toString();
    }

    public Person(String name, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    // Setter isn't necessary anymore because Service must be the only one authorized to modify any lists.
    // Also, we're no longer assigning dogs and people individually.
    // Every person has its own list of dogs using the method "person.getDogs().add(dog);"

    @Override
    public String toString() {

        // Preparing dog text
        String dogsText = "";
        for (Dog dog : dogs) {
            dogsText = dogsText + dog.getName() + " (" + dog.getId() + "), ";
        }
        // Returning all the information together
        return "Person{id='" + id + "', name='" + name + "', age=" + age + ", dogs=" + dogsText + "}";
    }
}
