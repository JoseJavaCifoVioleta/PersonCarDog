package org.example.controller;


import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;

import org.example.repository.Repository;
import org.example.service.Service;
import org.example.utils.DataSeeder;
import org.example.utils.JsonStore;
import org.example.utils.ResultOperation;
import org.example.utils.Utils;

import java.util.List;
import java.util.Scanner;

public class Controller {

    public static void run() {

        System.out.println("Hello to Transaction Cars Person to Person!");

        // 1. Instanciamos el repositorio único vacío
        Repository repo = new Repository();

        // 2. Poblamos el repositorio dinámicamente usando Java Faker (Exactamente 10 objetos en total)
        DataSeeder.seedRepository(repo);
        Scanner scan = new Scanner(System.in);

        while (true) {
            Utils.mainMenu();

            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    personSubMenu(scan, repo);
                    break;
                case "2":
                    dogSubMenu(scan, repo);
                    break;
                case "3":
                    carSubMenu(scan, repo);
                    break;
                case "4":
                    // buyCar uses current dummy data for demo; enhance later if needed
                    runBuyCarDemo(scan, repo);
                    break;
                case "5":
                    carTransactionSubMenu(scan, repo);
                    break;
                case "6":
                    adoptDogFlow(scan, repo);
                    break;
                case "7":
                    returnDogFlow(scan, repo);
                    break;
                case "8":
                    dogAdoptionSubMenu(scan, repo);
                    break;
                case "9":
                    JsonStore.save(repo, JsonStore.DEFAULT_FILE);
                    break;
                case "10": {
                    Repository loaded = JsonStore.load(JsonStore.DEFAULT_FILE);
                    if (loaded != null) {
                        repo = loaded;
                        System.out.println("Repository replaced with the loaded data.");
                    }
                    break;
                }
                case "11":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // ===== PERSON SUBMENU =====
    private static void personSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.personMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    String name = Utils.askString(scan, "Enter name: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    Service.createPerson(name, age, repo);
                    break;
                case "2":
                    Service.getAllPeople(repo);
                    break;
                case "3":
                    String pid = Utils.askString(scan, "Enter person ID: ");
                    Service.getPersonById(pid, repo);
                    break;
                case "4":
                    String uid = Utils.askString(scan, "Enter person ID to update: ");
                    String newName = Utils.askString(scan, "New name (leave blank to keep): ");
                    int newAge = Utils.askInt(scan, "New age (-1 to keep): ");
                    Service.updatePerson(uid, newName, newAge, repo);
                    break;
                case "5":
                    String did = Utils.askString(scan, "Enter person ID to delete: ");
                    Service.deletePerson(did, repo);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== DOG SUBMENU =====
    private static void dogSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.dogMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    String name = Utils.askString(scan, "Enter dog name: ");
                    String breed = Utils.askString(scan, "Enter breed: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    Service.createDog(name, breed, age, repo);
                    break;
                case "2":
                    Service.getAllDogs(repo);
                    break;
                case "3":
                    String did = Utils.askString(scan, "Enter dog ID: ");
                    Service.getDogById(did, repo);
                    break;
                case "4":
                    String uid = Utils.askString(scan, "Enter dog ID to update: ");
                    String newName = Utils.askString(scan, "New name (blank=keep): ");
                    String newBreed = Utils.askString(scan, "New breed (blank=keep): ");
                    int newAge = Utils.askInt(scan, "New age (-1=keep): ");
                    Service.updateDog(uid, newName, newBreed, newAge, repo);
                    break;
                case "5":
                    String delId = Utils.askString(scan, "Enter dog ID to delete: ");
                    Service.deleteDog(delId, repo);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== CAR SUBMENU =====
    private static void carSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    String make = Utils.askString(scan, "Enter make: ");
                    String model = Utils.askString(scan, "Enter model: ");
                    int year = Utils.askInt(scan, "Enter year: ");
                    Service.createCar(make, model, year, repo);
                    break;
                case "2":
                    Service.getAllCars(repo);
                    break;
                case "3":
                    String cid = Utils.askString(scan, "Enter car ID: ");
                    Service.getCarById(cid, repo);
                    break;
                case "4":
                    String uid = Utils.askString(scan, "Enter car ID to update: ");
                    String newMake = Utils.askString(scan, "New make (blank=keep): ");
                    String newModel = Utils.askString(scan, "New model (blank=keep): ");
                    int newYear = Utils.askInt(scan, "New year (-1=keep): ");
                    Service.updateCar(uid, newMake, newModel, newYear, repo);
                    break;
                case "5":
                    String delId = Utils.askString(scan, "Enter car ID to delete: ");
                    Service.deleteCar(delId, repo);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== CAR TRANSACTION (READ ONLY) SUBMENU =====
    private static void carTransactionSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carTransactionMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    Service.getAllCarTransactions(repo);
                    break;
                case "2":
                    String tid = Utils.askString(scan, "Enter transaction ID: ");
                    Service.getCarTransactionById(tid, repo);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== DOG ADOPTION (READ ONLY) SUBMENU =====
    private static void dogAdoptionSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.dogAdoptionMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    ResultOperation resAll = Service.getAllDogAdoptions(repo);
                    System.out.println("resultOperation: " + resAll);
                    break;
                case "2":
                    String tid = Utils.askString(scan, "Enter adoption ID: ");
                    ResultOperation resById = Service.getDogAdoptionById(tid, repo);
                    System.out.println("resultOperation: " + resById);
                    break;
                case "3":
                    listDogsOfPersonFlow(scan, repo);
                    break;
                case "4":
                    listOwnersOfDogFlow(scan, repo);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== DOG CUSTODY (READ ONLY) FLOWS =====
    private static void listDogsOfPersonFlow(Scanner scan, Repository repo) {
        String pid = Utils.askString(scan, "Enter person ID: ");
        Person person = repo.getPersonById(pid.trim());
        ResultOperation res = Service.getDogsByOwner(person, repo);
        System.out.println("resultOperation: " + res);
    }

    private static void listOwnersOfDogFlow(Scanner scan, Repository repo) {
        String did = Utils.askString(scan, "Enter dog ID: ");
        Dog dog = repo.getDogById(did.trim());
        ResultOperation res = Service.getOwnersByDog(dog, repo);
        System.out.println("resultOperation: " + res);
    }

    // ===== ADOPT / RETURN FLOWS =====
    private static void adoptDogFlow(Scanner scan, Repository repo) {
        System.out.println("Welcome to ADOPT A DOG (shared custody)");
        String pid = Utils.askString(scan, "Enter person ID: ");
        String did = Utils.askString(scan, "Enter dog ID: ");
        int fee = Utils.askInt(scan, "Enter adoption fee: ");

        Person adopter = repo.getPersonById(pid.trim());
        Dog dog = repo.getDogById(did.trim());

        ResultOperation res = Service.adoptDog(adopter, dog, fee, repo);
        System.out.println("resultOperation: " + res);
    }

    private static void returnDogFlow(Scanner scan, Repository repo) {
        System.out.println("Welcome to RETURN A DOG (end one adoption)");
        String pid = Utils.askString(scan, "Enter person ID: ");
        String did = Utils.askString(scan, "Enter dog ID: ");

        Person person = repo.getPersonById(pid.trim());
        Dog dog = repo.getDogById(did.trim());

        ResultOperation res = Service.returnDog(person, dog, repo);
        System.out.println("resultOperation: " + res);
    }

    // Demo buyCar using two people from repo (or create minimal if none)
    private static void runBuyCarDemo(Scanner scan, Repository repo) {
        System.out.println("Welcome to BUY A CAR (person to person)");

        java.util.ArrayList<Person> people = repo.getAllPeople();
        if (people.size() < 2) {
            System.out.println("Need at least two people. Creating demo people...");
            org.example.utils.ResultOperation rsSeller = Service.createPerson("DemoSeller", 30, repo);
            org.example.utils.ResultOperation rsBuyer = Service.createPerson("DemoBuyer", 25, repo);
            org.example.utils.ResultOperation rsCar = Service.createCar("DemoMake", "DemoModel", 2020, repo);
            Person seller = rsSeller.isSuccess() ? (Person) rsSeller.getUpdatedObject() : null;
            Car demoCar = rsCar.isSuccess() ? (Car) rsCar.getUpdatedObject() : null;
            if (seller != null && demoCar != null) {
                seller.setCar(demoCar);
            }
            people = repo.getAllPeople();
        }

        // Pick first two for simplicity (or let user choose later)
        Person seller = people.get(0);
        Person buyer = people.size() > 1 ? people.get(1) : people.get(0);

        if (seller.getCar() == null) {
            System.out.println("Seller has no car. Create one for seller first (use Car menu).");
            return;
        }
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car. Use menus to adjust data first.");
            return;
        }

        int price = Utils.askInt(scan, "Enter price: ");
        if (price < 0) price = 0;

        ResultOperation buyRes = Service.buyCar(buyer, seller, price, repo);
        System.out.println("resultOperation: " + buyRes);
    }
}
