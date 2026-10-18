package org.example.model;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class Dog {

    private String id;
    private String name;
    private String breed;
    private int age;
    private List<Person> owners = new ArrayList<>();

    public Dog() {}

    public Dog(String name, String breed, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
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

    public List<Person> getOwners() {
        return owners;
    }

    // Setter isn't necessary anymore because Service must be the only one authorized to modify any lists.
    // Also, we're no longer assigning dogs and people individually.
    // Every dog has its own list of owners using the method "person.getOwners().add(owner);"

    @Override
    public String toString() {

        // Preparing owner text
        String ownersText = "";
        for (Person owner : owners) {
            ownersText = ownersText + owner.getName() + " (" + owner.getId() + "), ";
        }

        // Returning all the information together
        return "Dog{id='" + id + "', name='" + name + "', breed='" + breed + "', age=" + age +
                ", owners=" + ownersText + "}";
    }
}
