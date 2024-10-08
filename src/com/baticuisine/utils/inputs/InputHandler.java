package com.baticuisine.utils.inputs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandler {
    private static InputHandler instance;
    private final Scanner scanner;


    private InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public static InputHandler getInstance() {
        if (instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }

    public LocalDate getDateInput(String prompt, LocalDate afterDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = null;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                date = LocalDate.parse(input, formatter);

                if (afterDate != null && !date.isAfter(afterDate)) {
                    System.out.println("La date doit être après " + afterDate.format(formatter) + ". Veuillez réessayer.");
                } else {
                    valid = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer (format : jj/mm/aaaa) : ");
            }
        }
        return date;
    }
    public boolean getBooleanInput(String prompt) {
        boolean valid = false;
        boolean input = false;
        while (!valid) {
            System.out.print(prompt);
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("true")) {
                input = true;
                valid = true;
            } else if (response.equalsIgnoreCase("false")) {
                input = false;
                valid = true;
            } else {
                System.out.println("Entrée invalide. Veuillez entrer 'true' ou 'false'.");
            }
        }
        return input;
    }
    public int getIntInput(String prompt) {
        int input = -1;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                input = scanner.nextInt();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.nextLine();
            }
        }
        return input;
    }

    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public double getDoubleInput(String prompt) {
        double input = -1;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                input = scanner.nextDouble();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre décimal.");
                scanner.nextLine();
            }
        }
        return input;
    }


    public void close() {
        scanner.close();
    }

}
