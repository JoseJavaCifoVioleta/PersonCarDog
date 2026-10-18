package org.example.repository;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;

import java.util.ArrayList;
import java.util.UUID;

public class Repository {

    private String id;
    private ArrayList<Person> people;
    private ArrayList<Car> cars;
    private ArrayList<Dog> dogs;
    private ArrayList<CarTransaction> carTransactions;
    private ArrayList<org.example.model.DogAdoption> dogAdoptions;

    public Repository(){
        this.id = UUID.randomUUID().toString();
        this.people = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.dogs = new ArrayList<>();
        this.carTransactions = new ArrayList<>();
        this.dogAdoptions = new ArrayList<>();
    }

    // ===== Person =====
    public void addPerson(Person person) {
        people.add(person);
    }

    public Person getPersonById(String id) {
        for (Person p : people) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Person> getAllPeople() {
        return people;
    }

    public boolean removePerson(String id) {
        return people.removeIf(p -> p.getId().equals(id));
    }

    // ===== Car =====
    public void addCar(Car car) {
        cars.add(car);
    }

    public Car getCarById(String id) {
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Car> getAllCars() {
        return cars;
    }

    public boolean removeCar(String id) {
        return cars.removeIf(c -> c.getId().equals(id));
    }

    // ===== Dog =====
    public void addDog(Dog dog) {
        dogs.add(dog);
    }

    public Dog getDogById(String id) {
        for (Dog d : dogs) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    public ArrayList<Dog> getAllDogs() {
        return dogs;
    }

    public boolean removeDog(String id) {
        return dogs.removeIf(d -> d.getId().equals(id));
    }

    // ===== CarTransaction =====
    public void addCarTransaction(CarTransaction carTransaction) {
        carTransactions.add(carTransaction);
    }

    public CarTransaction getCarTransactionById(String id) {
        for (CarTransaction ct : carTransactions) {
            if (ct.getId().equals(id)) {
                return ct;
            }
        }
        return null;
    }

    public ArrayList<CarTransaction> getAllCarTransactions() {
        return carTransactions;
    }

    public boolean removeCarTransaction(String id) {
        return carTransactions.removeIf(ct -> ct.getId().equals(id));
    }

    // ===== DogAdoption =====
    public void addDogAdoption(org.example.model.DogAdoption adoption) {
        dogAdoptions.add(adoption);
    }

    public org.example.model.DogAdoption getDogAdoptionById(String id) {
        for (org.example.model.DogAdoption da : dogAdoptions) {
            if (da.getId().equals(id)) {
                return da;
            }
        }
        return null;
    }

    public ArrayList<org.example.model.DogAdoption> getAllDogAdoptions() {
        return dogAdoptions;
    }

    public boolean removeDogAdoption(String id) {
        return dogAdoptions.removeIf(da -> da.getId().equals(id));
    }

    // ===== Reset / reload helpers =====
    public void clear() {
        people.clear();
        cars.clear();
        dogs.clear();
        carTransactions.clear();
        dogAdoptions.clear();
    }

    public boolean isEmpty() {
        return people.isEmpty()
                && cars.isEmpty()
                && dogs.isEmpty()
                && carTransactions.isEmpty()
                && dogAdoptions.isEmpty();
    }
}
