package org.example.controller;

import org.example.model.Car;
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
                    System.out.println("\n--- LISTA DE PERROS (2 objetos) ---");
                    for (Dog d : repo.getAllDogs()) {
                        System.out.println(d);
                    }
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
                    // Temporalmente dejamos este aviso hasta que hagamos el commit de Transacciones
                    System.out.println("Car Transactions - not implemented yet.");
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
                    for (Person p : Service.getAllPeople(repo)) {
                        System.out.println(p + " | Car: " + (p.getCar() != null ? p.getCar().getMake() : "None"));
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
                    for (Car c : Service.getAllCars(repo)) {
                        System.out.println(c); // Imprime el molde del coche usando su método toString()
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
}
