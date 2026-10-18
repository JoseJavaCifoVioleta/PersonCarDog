package org.example.utils;

import com.github.javafaker.Faker;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.service.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSeeder {

    public static void seedRepository(Repository repo) {
        Faker faker = new Faker();

        List<Car> temporaryCars = new ArrayList<>();
        List<Person> temporaryPeople = new ArrayList<>();

        //  1. Generate 3 Cars (Objects 1, 2 y 3)
        for (int i = 0; i < 3; i++) {
            Car car = new Car(
                    faker.company().name(),
                    faker.commerce().productName(),
                    faker.number().numberBetween(1995, 2025)
            );
            repo.addCar(car);
            temporaryCars.add(car);
        }

        // 2. Generate 3 People (Objects 4, 5 y 6)
        for (int i = 0; i < 3; i++) {
            Person person = new Person(
                    faker.name().fullName(),
                    faker.number().numberBetween(18, 80)
            );
            temporaryPeople.add(person);
        }

        // Service.buyCar needs a valid seller and buyer
        // so we assign the first car to the first person
        temporaryPeople.get(0).setCar(temporaryCars.get(0));

        // Person 1 will not have a car
        // Person 2 will have a random car
        temporaryPeople.get(2).setCar(temporaryCars.get(1));

        // Add people to repository
        for (Person p : temporaryPeople) {
            repo.addPerson(p);
        }

        // 3. Generate 4 Dogs (Objects 7, 8, 9, 10) - include 2 already adopted
        List<Dog> temporaryDogs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Dog dog = new Dog(
                    faker.dog().name(),
                    faker.dog().breed(),
                    faker.number().numberBetween(1, 15)
            );
            repo.addDog(dog);
            temporaryDogs.add(dog);
        }

        // 4. Generate 2 fake transactions
        CarTransaction tx1 = new CarTransaction(
                temporaryPeople.get(1), // buyer
                temporaryPeople.get(2), // seller
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );
        repo.addCarTransaction(tx1);

        CarTransaction tx2 = new CarTransaction(
                temporaryPeople.get(2),
                temporaryPeople.get(0),
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );
        repo.addCarTransaction(tx2);

        // 5. Seed adoptions exercising the shared-custody rules (spec section 6):
        //    - one dog with two active owners
        //    - one person with two active dogs
        //    Every mutation goes through Service.adoptDog so the relationship
        //    is maintained in a single place.
        if (temporaryPeople.size() > 2 && temporaryDogs.size() > 2) {
            Person ownerOne = temporaryPeople.get(0);
            Person ownerTwo = temporaryPeople.get(2);
            Dog sharedDog = temporaryDogs.get(2);
            Dog secondDogForOwnerOne = temporaryDogs.get(0);
            Dog secondDogForOwnerTwo = temporaryDogs.get(3);

            Service.adoptDog(ownerOne, sharedDog, faker.number().numberBetween(0, 200), repo);
            Service.adoptDog(ownerTwo, sharedDog, faker.number().numberBetween(0, 200), repo);
            Service.adoptDog(ownerOne, secondDogForOwnerOne, faker.number().numberBetween(0, 200), repo);
            Service.adoptDog(ownerTwo, secondDogForOwnerTwo, faker.number().numberBetween(0, 200), repo);
        }

        // Print verification
        System.out.println("=== DATA SEEDING COMPLETE ===");
        System.out.println("People: " + repo.getAllPeople().size() + " | Cars: " + repo.getAllCars().size() + " | Dogs: " + repo.getAllDogs().size() + " | Transactions: " + repo.getAllCarTransactions().size() + " | Adoptions: " + repo.getAllDogAdoptions().size());
        System.out.println("================================");
        System.out.println("TOTAL NEW INSTANCIES AT REPO: 10+");
        System.out.println("=============================");
    }
}
