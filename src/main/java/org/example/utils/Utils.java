package org.example.utils;

import java.util.Scanner;

public class Utils {

    // --- COMIENZO EXPLICACIÓN  ---
    // ¿Qué es Utils? Es una caja de herramientas estática (static). Significa que cualquier
    // otra parte de nuestro programa puede usar estas funciones directamente sin tener que
    // crear un objeto Utils de verdad en memoria.
    // --- FIN DE EXPLICACIÓN ---

    public static void mainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Person operations");
        System.out.println("2. Dog operations");
        System.out.println("3. Car operations");
        System.out.println("4. Buy a car (person to person)");
        System.out.println("5. View car transactions (read only)");
        System.out.println("6. Quit");
    }

    // --- COMIENZO DE EXPLICACIÓN  ---
    // Creamos los submenús visuales. Son simples instrucciones de impresión (System.out.println)
    // que le muestran al usuario en la pantalla negra qué opciones numéricas tiene disponibles
    // para interactuar con cada tipo de objeto de nuestro sistema.
    // --- FIN DE EXPLICACIÓN ---

    public static void personMenu() {
        System.out.println("\n===== PERSON MENU =====");
        System.out.println("1. Create person");
        System.out.println("2. List all people");
        System.out.println("3. Find person by ID");
        System.out.println("4. Update person");
        System.out.println("5. Delete person");
        System.out.println("6. Back");
    }

    public static void carMenu() {
        System.out.println("\n===== CAR MENU =====");
        System.out.println("1. Create car");
        System.out.println("2. List all cars");
        System.out.println("3. Find car by ID");
        System.out.println("4. Update car");
        System.out.println("5. Delete car");
        System.out.println("6. Back");
    }

    public static void dogMenu() {
        System.out.println("\n===== DOG MENU =====");
        System.out.println("1. Create dog");
        System.out.println("2. List all dogs");
        System.out.println("3. Find dog by ID");
        System.out.println("4. Update dog");
        System.out.println("5. Delete dog");
        System.out.println("6. Back");
    }

    public static void carTransactionMenu() {
        System.out.println("\n===== CAR TRANSACTIONS (READ ONLY) =====");
        System.out.println("1. List all transactions");
        System.out.println("2. Find transaction by ID");
        System.out.println("3. Back");
    }

    public static String askMenuOption(Scanner scan) {
        System.out.print("Select an option: ");
        return scan.nextLine();
    }

    // --- COMIENZO DE EXPLICACIÓN  ---
    // ¿Por qué hacemos 'askString' y 'askInt'?
    // En Java, leer datos del teclado puede dar problemas si mezclamos números y textos.
    // Con estas dos herramientas centralizamos la lectura. 'askString' muestra una pregunta en la
    // consola, espera a que el usuario escriba algo y le devuelve ese texto al programa.
    // --- FIN DE EXPLICACIÓN ---

    public static String askString(Scanner scan, String prompt) {
        System.out.print(prompt);
        return scan.nextLine();
    }

    // --- COMIENZO DE EXPLICACIÓN  ---
    // 'askInt' hace lo mismo pero se asegura de transformar el texto que escribe el usuario
    // en un número entero (int) real que Java pueda usar para cálculos o asignaciones de edad/año.
    // Si el usuario introduce algo que no es un número, capturamos el fallo con un bloque "try-catch"
    // para evitar que todo el programa explote (Crash) por sorpresa.
    // --- FIN DE EXPLICACIÓN ---

    public static int askInt(Scanner scan, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scan.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric format. Please enter a valid whole number.");
            }
        }
    }
}
