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
                    System.out.println("\n--- LISTA DE COCHES (3 objetos) ---");
                    for (Car c : repo.getAllCars()) {
                        System.out.println(c);
                    }
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
}

/*
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

        System.out.println( "Hello to Transaction Cars Person to Person!" );

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
                    //System.out.println("Person - not implemented yet.");
                    System.out.println("\n--- LISTA DE PERSONAS (3 objetos) ---");
                    for (Person p : repo.getAllPeople()) {
                        System.out.println(p + " | Coche asignado: " + (p.getCar() != null ? p.getCar().getMake() : "Ninguno"));
                    }
                    break;
                case "2":
                    //System.out.println("Dog - not implemented yet.");
                    System.out.println("\n--- LISTA DE PERROS (2 objetos) ---");
                    for (Dog d : repo.getAllDogs()) {
                        System.out.println(d);
                    }
                    break;
                case "3":
                    //System.out.println("Car - not implemented yet.");
                    System.out.println("\n--- LISTA DE COCHES (3 objetos) ---");
                    for (Car c : repo.getAllCars()) {
                        System.out.println(c);
                    }
                    break;
                case "4":
                    //Service.buyCar(b, a , 100, repo);

                    // Recuperamos la lista de personas añadidas por el seeder
                    List<Person> people = repo.getAllPeople();

                    if (people.size() >= 2) {
                        // Tomamos a los candidatos preparados por el Seeder
                        Person seller = people.get(0); // Tiene coche
                        Person buyer = people.get(1);  // No tiene coche

                        // Ejecutamos el servicio de compraventa
                        int fakePrice = 15000;
                        Service.buyCar(buyer, seller, fakePrice, repo);
                    } else {
                        System.out.println("No hay suficientes personas en el repositorio.");
                    }

                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
*/