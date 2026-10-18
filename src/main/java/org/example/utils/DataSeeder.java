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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataSeeder {

    public static void seedRepository(Repository repo) {
        Faker faker = new Faker();

        List<Car> temporaryCars = new ArrayList<>();
        List<Person> temporaryPeople = new ArrayList<>();
        List<Dog> temporaryDogs = new ArrayList<>();

        // 1. Generate 3 Cars
        for (int i = 0; i < 3; i++) {
            Car car = new Car(
                    faker.company().name(),
                    faker.commerce().productName(),
                    faker.number().numberBetween(1995, 2025)
            );
            repo.addCar(car);
            temporaryCars.add(car);
        }

        // 2. Generate 5 People (all adults so they are allowed to adopt)
        for (int i = 0; i < 5; i++) {
            Person person = new Person(
                    faker.name().fullName(),
                    faker.number().numberBetween(18, 80)
            );
            temporaryPeople.add(person);
        }

        for (Person p : temporaryPeople) {
            repo.addPerson(p);
        }

        // Cars for the buyCar demo: the demo seller (index 0) must own a car
        // and the demo buyer (index 1) must not.
        temporaryPeople.get(0).setCar(temporaryCars.get(0));
        temporaryPeople.get(2).setCar(temporaryCars.get(1));

        // 3. Generate 5 Dogs (unique names, so the console demo is easy to follow)
        Set<String> usedDogNames = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            String baseName = faker.dog().name();
            String name = baseName;
            int suffix = 2;
            while (!usedDogNames.add(name)) {
                name = baseName + " " + (suffix++);
            }
            Dog dog = new Dog(
                    name,
                    faker.dog().breed(),
                    faker.number().numberBetween(1, 15)
            );
            repo.addDog(dog);
            temporaryDogs.add(dog);
        }

        // 4. Fake transactions (demo data, unchanged)
        CarTransaction tx1 = new CarTransaction(
                temporaryPeople.get(1),
                temporaryPeople.get(2),
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

        // 5. Seed many-to-many adoptions through the Service, so that every
        //    relationship mutation stays inside the business layer.
        int[][] adoptionPlan = {
                {0, 0}, // person 0 adopts dog 0
                {1, 0}, // person 1 adopts dog 0  -> dog 0 now has 2 owners (max)
                {0, 1}, // person 0 adopts dog 1
                {2, 2}, // person 2 adopts dog 2
                {3, 2}, // person 3 adopts dog 2  -> dog 2 now has 2 owners (max)
                {4, 3}, // person 4 adopts dog 3
                {0, 4}  // person 0 adopts dog 4  -> person 0 now has 3 dogs (max)
        };

        int seeded = 0;
        for (int[] pair : adoptionPlan) {
            Person adopter = temporaryPeople.get(pair[0]);
            Dog dog = temporaryDogs.get(pair[1]);
            int fee = faker.number().numberBetween(0, 200);
            ResultOperation res = Service.adoptDog(adopter, dog, fee, repo);
            if (res.isSuccess()) {
                seeded++;
            }
        }

        // Print verification
        System.out.println("=== DATA SEEDING COMPLETE ===");
        System.out.println("People: " + repo.getAllPeople().size()
                + " | Cars: " + repo.getAllCars().size()
                + " | Dogs: " + repo.getAllDogs().size()
                + " | Transactions: " + repo.getAllCarTransactions().size()
                + " | Adoptions: " + repo.getAllDogAdoptions().size()
                + " (seeded via Service: " + seeded + ")");
        System.out.println("Shared-custody examples: dog '" + temporaryDogs.get(0).getName()
                + "' has 2 owners; person '" + temporaryPeople.get(0).getName()
                + "' has 3 dogs (both at their maximum).");
        System.out.println("================================");
    }
}
