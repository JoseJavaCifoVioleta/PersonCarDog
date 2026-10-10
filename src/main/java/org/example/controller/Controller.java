package org.example.controller;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Person;
import org.example.model.Dog;
import org.example.repository.Repository;
import org.example.service.Service;
import org.example.utils.DataSeeder;
import org.example.utils.Utils;

import java.util.List;
import java.util.Scanner;

public class Controller {

    public static void run() {
        System.out.println("Hello to Transaction Cars Person to Person!");

        Repository repo = new Repository();
        DataSeeder.seedRepository(repo);
        Scanner scan = new Scanner(System.in);

        while (true) {
            Utils.mainMenu();
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    // Redirigimos el flujo del programa hacia nuestra nueva gestión de personas
                    managePersonMenu(scan, repo);
                    break;
                case "2":
                    // Redirigimos el flujo hacia nuestra nueva gestión completa de perros
                    manageDogMenu(scan, repo);
                    break;
                case "3":
                    // Redirigimos el flujo hacia nuestra nueva gestión completa de coches
                    manageCarMenu(scan, repo);
                    break;
                case "4":
                    List<Person> people = repo.getAllPeople();
                    if (people.size() >= 2) {
                        Person seller = people.get(0);
                        Person buyer = people.get(1);
                        int fakePrice = 15000;
                        Service.buyCar(buyer, seller, fakePrice, repo);
                    } else {
                        System.out.println("No hay suficientes personas en el repositorio.");
                    }
                    break;
                case "5":
                    // Redirigimos el flujo hacia la lectura exclusiva de transacciones
                    manageTransactionMenu(scan, repo);
                    break;
                case "6": // CASO 6: Cierra el programa de forma correcta
                    System.out.println("Goodbye!");
                    return; // Rompe el bucle principal y finaliza la aplicación
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ DE PERSONAS: Conexión directa entre la pantalla y la lógica (Service)
    // =========================================================================
    private static void managePersonMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.personMenu(); // Pintamos el menú de personas en la pantalla
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // CASO 1: Crear Persona
                    String name = Utils.askString(scan, "Enter name: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    // Llamamos a la fábrica (Service) para que valide y guarde los datos
                    Service.createPerson(name, age, repo);
                    break;

                case "2": // CASO 2: Listar todas las personas
                    System.out.println("\n--- CURRENT PEOPLE IN SYSTEM ---");
                    // Obtenemos la lista y la recorremos con un bucle for-each (Helsinki Cap. 4)
                    for (Person currentPerson  : Service.getAllPeople(repo)) {
                        System.out.println(currentPerson  + " | Car: " + (currentPerson .getCar() != null ? currentPerson .getCar().getMake() : "None"));
                    }
                    break;

                case "3": // CASO 3: Buscar por ID
                    String searchId = Utils.askString(scan, "Enter Person ID to find: ");
                    Service.getPersonById(searchId, repo);
                    break;

                case "4": // CASO 4: Actualizar datos de una persona
                    String updateId = Utils.askString(scan, "Enter Person ID to update: ");
                    String newName = Utils.askString(scan, "Enter new name: ");
                    int newAge = Utils.askInt(scan, "Enter new age: ");
                    Service.updatePerson(updateId, newName, newAge, repo);
                    break;

                case "5": // CASO 5: Eliminar a una persona
                    String deleteId = Utils.askString(scan, "Enter Person ID to delete: ");
                    Service.deletePerson(deleteId, repo);
                    break;

                case "6": // CASO 6: Volver atrás
                    return; // Rompe este bucle y regresa al Menú Principal

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ DE COCHES: Captura de pantalla (Utils) unida a la lógica (Service)
    // =========================================================================
    private static void manageCarMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carMenu(); // Pintamos el menú de coches en la pantalla usando las herramientas de Utils
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // Crear Coche
                    String make = Utils.askString(scan, "Enter make: ");
                    String model = Utils.askString(scan, "Enter model: ");
                    int year = Utils.askInt(scan, "Enter year: ");
                    Service.createCar(make, model, year, repo); // Pasamos los datos recolectados al Chef (Símil)
                    break;

                case "2": // Listar todos los coches
                    System.out.println("\n--- CURRENT CARS IN SYSTEM ---");
                    for (Car currentCar : Service.getAllCars(repo)) {
                        System.out.println(currentCar); // Imprime el molde del coche usando su método toString()
                    }
                    break;

                case "3": // Buscar por ID
                    String searchId = Utils.askString(scan, "Enter Car ID to find: ");
                    Service.getCarById(searchId, repo);
                    break;

                case "4": // Actualizar Coche
                    String updateId = Utils.askString(scan, "Enter Car ID to update: ");
                    String newMake = Utils.askString(scan, "Enter new make: ");
                    String newModel = Utils.askString(scan, "Enter new model: ");
                    int newYear = Utils.askInt(scan, "Enter new year: ");
                    Service.updateCar(updateId, newMake, newModel, newYear, repo);
                    break;

                case "5": // Eliminar Coche
                    String deleteId = Utils.askString(scan, "Enter Car ID to delete: ");
                    Service.deleteCar(deleteId, repo);
                    break;

                case "6": // Volver al Menú Principal
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ DE PERROS: Captura de pantalla (Utils) unida a la lógica (Service)
    // =========================================================================
    private static void manageDogMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.dogMenu(); // Pintamos el menú de perros en la pantalla usando las herramientas de Utils
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // Crear Perro
                    String name = Utils.askString(scan, "Enter dog name: ");
                    String breed = Utils.askString(scan, "Enter breed: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    Service.createDog(name, breed, age, repo); // Enviamos los datos recolectados al Service
                    break;

                case "2": // Listar todos los perros
                    System.out.println("\n--- CURRENT DOGS IN SYSTEM ---");
                    for (Dog currentDog : Service.getAllDogs(repo)) {
                        System.out.println(currentDog); // Muestra los datos del molde usando su método toString()
                    }
                    break;

                case "3": // Buscar por ID
                    String searchId = Utils.askString(scan, "Enter Dog ID to find: ");
                    Service.getDogById(searchId, repo);
                    break;

                case "4": // Actualizar Perro
                    String updateId = Utils.askString(scan, "Enter Dog ID to update: ");
                    String newName = Utils.askString(scan, "Enter new name: ");
                    String newBreed = Utils.askString(scan, "Enter new breed: ");
                    int newAge = Utils.askInt(scan, "Enter new age: ");
                    Service.updateDog(updateId, newName, newBreed, newAge, repo);
                    break;

                case "5": // Eliminar Perro
                    String deleteId = Utils.askString(scan, "Enter Dog ID to delete: ");
                    Service.deleteDog(deleteId, repo);
                    break;

                case "6": // Volver al Menú Principal
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
    // =========================================================================
    // SUBMENÚ DE TRANSACCIONES: Interfaz de Solo Lectura (Read-Only) !!!!!!!!!
    // =========================================================================
    private static void manageTransactionMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carTransactionMenu(); // Pintamos el menú especial de transacciones (Read-Only)
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // Listar todas las transacciones históricas
                    System.out.println("\n--- CAR TRANSACTION HISTORICAL LOGS ---");
                    for (CarTransaction currentTransaction : Service.getAllCarTransactions(repo)) {
                        System.out.println(currentTransaction); // Imprime el registro inalterable generado por el sistema
                    }
                    break;

                case "2": // Buscar una transacción concreta por su identificador único
                    String searchId = Utils.askString(scan, "Enter Transaction ID to find: ");
                    Service.getCarTransactionById(searchId, repo);
                    break;

                case "3": // Volver al Menú Principal
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
