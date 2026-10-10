package org.example.service;

import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Person;
import org.example.model.Dog; // Importante: Añadimos a Dog para que Java sepa qué es un Perro

import java.util.ArrayList;
import java.util.Date;

public class Service {

    // ==========================================
    // MÉTODO DE REFERENCIA: COMPRAVENTA DE COCHES
    // ==========================================
    public static boolean buyCar(Person buyer, Person seller, int price, Repository repo){

        System.out.println("Welcome to BUY MENU");

        // Comprobamos si el comprador existe en la lista de nuestro almacén (Repository).
        if (!repo.getAllPeople().contains(buyer)) {
            System.out.println("Buyer not found in repository.");
            return false;
        }
        System.out.println("Buyer exits: " + buyer);

        // Comprobamos si el vendedor existe en la lista de nuestro almacén (Repository).
        if (!repo.getAllPeople().contains(seller)) {
            System.out.println("Seller not found in repository.");
            return false;
        }
        System.out.println("Seller exits: " + seller);

        // Validamos si el vendedor realmente tiene un coche para poder venderlo.
        if (seller.getCar() == null) {
            System.out.println("Seller does not have a car to sell.");
            return false;
        }

        // Extraemos temporalmente el coche del vendedor para operar con él.
        Car car = seller.getCar();
        System.out.println("Seller can sell a car: "  + car);

        // Regla de negocio: El comprador no debe tener coche actualmente para poder comprar uno nuevo.
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car.");
            return false;
        }

        System.out.println("Buyer can buy a car.");

        // Intercambio de llaves en memoria: Se le asigna el coche al comprador y se le quita al vendedor.
        buyer.setCar(car);
        seller.setCar(null);

        System.out.println("Settings done, now creating CarTransaction ...");

        // Creamos un justificante o recibo histórico (CarTransaction) con los datos de la operación.
        CarTransaction transaction = new CarTransaction(buyer, seller, new Date(), car, "Car sold for " + price);

        // Guardamos el recibo en el historial del almacén.
        repo.addCarTransaction(transaction);

        // Mostramos el recibo final por pantalla.
        System.out.println(transaction);

        return true;
    }

    // ==========================================
    // CAPÍTULO 1: OPERACIONES CRUD PARA PERSONAS
    // ==========================================

    // CREAR: Registra una nueva persona en el sistema.
    // Afecta a: La lista global de personas del Repository, añadiendo un elemento más.
    public static Person createPerson(String name, int age, Repository repo) {
        System.out.println("Starting process to create a new Person...");

        // VALIDACIÓN: Comprobamos si el texto está vacío o si la edad es negativa.
        // Si los datos son erróneos, frenamos el programa con un 'return null' para evitar guardar basura.
        if (name == null || name.trim().isEmpty() || age < 0) {
            System.out.println("Error: Name cannot be blank and age must be valid.");
            return null;
        }

        // Fabricamos el objeto real en la memoria del ordenador utilizando la plantilla (Constructor de Person).
        Person newPerson = new Person(name, age);

        // Guardamos el objeto recién fabricado dentro de las listas del repositorio (Almacén central).
        repo.addPerson(newPerson);
        System.out.println("Person created and saved successfully: " + newPerson);
        return newPerson;
    }

    // LEER (UNO): Busca a una persona específica en el sistema.
    // ¿Cómo funciona? Vamos mirando uno a uno (Bucle for) los registros del almacén hasta encontrar el ID coincidente.
    public static Person getPersonById(String id, Repository repo) {
        System.out.println("Searching for person with ID: " + id);

        for (Person p : repo.getAllPeople()) {
            if (p.getId().equals(id)) {
                System.out.println("Match found: " + p.getName());
                return p; // Si lo encuentra, rompe la búsqueda inmediatamente y devuelve a la persona.
            }
        }
        System.out.println("Person with ID " + id + " not found in our records.");
        return null; // Si termina el bucle y no vio a nadie con ese ID, devuelve "nada" (null).
    }

    // LEER (TODOS): Devuelve todos los registros existentes.
    // Afecta a: Ningún dato se altera, solo sirve para consultar y listar lo que hay dentro.
    public static ArrayList<Person> getAllPeople(Repository repo) {
        System.out.println("Retrieving complete list of people...");
        return repo.getAllPeople();
    }

    // ACTUALIZAR: Modifica los datos de alguien que ya existe.
    // Afecta a: Las propiedades (nombre y edad) de un objeto específico que ya reside en el repositorio.
    public static boolean updatePerson(String id, String newName, int newAge, Repository repo) {
        System.out.println("Starting update process for ID: " + id);

        // Primero, usamos nuestra propia herramienta de búsqueda para ver si esa persona existe.
        Person target = getPersonById(id, repo);
        if (target == null) {
            return false; // Si no existe, es imposible actualizarla.
        }

        // Volvemos a comprobar que el usuario no intente poner textos vacíos o edades absurdas.
        if (newName == null || newName.trim().isEmpty() || newAge < 0) {
            System.out.println("Error: New update values are invalid.");
            return false;
        }

        // Aplicamos los cambios directamente sobre las variables internas del objeto mediante métodos 'set'.
        target.setName(newName);
        target.setAge(newAge);
        System.out.println("Person fields updated successfully: " + target);
        return true;
    }

    // BORRAR: Elimina por completo a un usuario del sistema.
    // REGLA DE INTEGRIDAD ESPECIAL: Si borramos a una persona y resulta que tenía un coche asignado,
    // debemos romper el enlace (poner su propiedad coche a 'null') antes de destruirla.
    // Si no hiciéramos esto, el coche se quedaría flotando en el sistema asociado a un dueño fantasma que ya no existe.
    public static boolean deletePerson(String id, Repository repo) {
        System.out.println("Starting deletion process for ID: " + id);

        Person target = getPersonById(id, repo);
        if (target == null) {
            return false;
        }

        // Control de seguridad: ¿Esta persona tiene coche?
        if (target.getCar() != null) {
            System.out.println("Safety Check: This person owns a car. Clearing ownership reference before deleting the person...");
            target.setCar(null); // Desvinculamos el coche para proteger la base de datos.
        }

        // Borramos físicamente al usuario de la lista del almacén.
        boolean removed = repo.getAllPeople().remove(target);
        if (removed) {
            System.out.println("Person has been completely removed from the system.");
        }
        return removed;
    }
}


/*
package org.example.service;

import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Person;

import java.util.Date;

public class Service {


    public static boolean buyCar(Person buyer, Person seller, int price, Repository repo){

        System.out.println("Welcome to BUY MENU");
        // person buyer a exists at array
        if (!repo.getAllPeople().contains(buyer)) {
            System.out.println("Buyer not found in repository.");
            return false;
        }
        System.out.println("Buyer exits: " + buyer);

        // person seller b exists at array
        if (!repo.getAllPeople().contains(seller)) {
            System.out.println("Seller not found in repository.");
            return false;
        }
        System.out.println("Seller exits: " + seller);

        // car exists??
        if (seller.getCar() == null) {
            System.out.println("Seller does not have a car to sell.");
            return false;
        }

        // person a has a car
        Car car = seller.getCar();
        System.out.println("Seller can sell a car: "  + car);

        // person b does not have a car
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car.");
            return false;
        }

        System.out.println("Buyer can buy a car.");

        // personB.setCar (bmw)
        buyer.setCar(car);
        // personA.car = null
        seller.setCar(null);

        System.out.println("Settings done, now creating CarTransaction ...");

        // create object CarTransaction
        CarTransaction transaction = new CarTransaction(buyer, seller, new Date(), car, "Car sold for " + price);
        // save object CarTransaction at repo
        repo.addCarTransaction(transaction);

        // PRINT
        System.out.println(transaction);

        return true;
    }
}
*/
