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


    // =========================================================================
    // CAPÍTULO 2: OPERACIONES CRUD PARA COCHES (CAR)
    // =========================================================================

    // CREAR: Fabricamos un objeto 'Car' (Model) y lo guardamos en el almacén (Repository).
    // Helsinki Cap. 5: Uso de constructores para inicializar objetos con datos obligatorios.
    public static Car createCar(String make, String model, int year, Repository repo) {
        System.out.println("Starting process to create a new Car...");

        // VALIDACIÓN (Lógica de Service): Impedimos años imposibles o textos vacíos (.isEmpty()).
        if (make == null || make.trim().isEmpty() || model == null || model.trim().isEmpty() || year < 1886) {
            System.out.println("Error: Make and model cannot be blank, and year must be valid.");
            return null; // Si falla la regla de negocio, el Chef (Service) frena la operación.
        }

        // Model en acción: Usamos la plantilla 'Car' para dar forma al nuevo registro en memoria.
        Car newCar = new Car(make, model, year);

        // Repository en acción: Abrimos la despensa general y guardamos el coche en su lista interna.
        repo.addCar(newCar);
        System.out.println("Car created and saved successfully to repository: " + newCar);
        return newCar;
    }

    // LEER (UNO): Busca un coche específico en el almacén utilizando su identificador único (ID).
    // Helsinki Cap. 4: Uso de bucles For-Each para examinar colecciones de datos una por una.
    public static Car getCarById(String id, Repository repo) {
        System.out.println("Searching repository for Car ID: " + id);

        // Recorremos la lista que nos da el Repository buscando una coincidencia de ID.
        for (Car c : repo.getAllCars()) {
            if (c.getId().equals(id)) {
                System.out.println("Match found: " + c.getMake() + " " + c.getModel());
                return c; // Si lo encuentra, devuelve el coche y termina la función.
            }
        }
        System.out.println("Car with ID " + id + " not found in our records.");
        return null;
    }

    // LEER (TODOS): Obtiene el listado completo de coches de la base de datos en memoria.
    // Sirve para consultar el estado actual del almacén (Repository) sin alterar ningún dato.
    public static ArrayList<Car> getAllCars(Repository repo) {
        System.out.println("Retrieving complete list of cars from repository...");
        return repo.getAllCars();
    }

    // ACTUALIZAR: Modifica los datos internos de un coche que ya existe en el almacén.
    // Helsinki Cap. 5: Uso de métodos 'setter' para modificar de forma segura variables privadas.
    public static boolean updateCar(String id, String newMake, String newModel, int newYear, Repository repo) {
        System.out.println("Starting update process for Car ID: " + id);

        // Reutilizamos nuestro método de lectura para verificar si el coche realmente existe en la 'despensa' (simil).
        Car target = getCarById(id, repo);
        if (target == null) {
            return false;
        }

        // Volvemos a validar las reglas antes de sobreescribir los datos viejos.
        if (newMake == null || newMake.trim().isEmpty() || newModel == null || newModel.trim().isEmpty() || newYear < 1886) {
            System.out.println("Error: New update values for Car are invalid.");
            return false;
        }

        // Modificamos el estado del objeto 'Model' persistido en las listas del repositorio.
        target.setMake(newMake);
        target.setModel(newModel);
        target.setYear(newYear);
        System.out.println("Car fields updated successfully: " + target);
        return true;
    }

    // BORRAR: Elimina un coche por completo de la lista del almacén.
    // REGLA DE INTEGRIDAD (Vínculo de Modelos): Si eliminamos un coche, debemos revisar si alguna
    // Persona (Model) lo tiene asignado como su propiedad para ponérselo a 'null' (Ninguno).
    public static boolean deleteCar(String id, Repository repo) {
        System.out.println("Starting deletion process for Car ID: " + id);

        Car target = getCarById(id, repo);
        if (target == null) {
            return false;
        }

        // Helsinki Cap. 4 y 5: Recorremos los dueños para limpiar referencias rotas en la memoria.
        for (Person p : repo.getAllPeople()) {
            if (p.getCar() != null && p.getCar().equals(target)) {
                System.out.println("Safety Check: Removing car link from owner: " + p.getName());
                p.setCar(null); // Desvinculamos el coche de la persona para mantener los datos limpios.
            }
        }

        // Eliminamos el coche físicamente de la lista del Repository.
        boolean removed = repo.getAllCars().remove(target);
        if (removed) {
            System.out.println("Car has been completely removed from the system.");
        }
        return removed;
    }
}
