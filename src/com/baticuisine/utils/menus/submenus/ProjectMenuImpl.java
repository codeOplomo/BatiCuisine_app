package com.baticuisine.utils.menus.submenus;

import com.baticuisine.models.Client;
import com.baticuisine.models.Project;
import com.baticuisine.services.ProjectService;
import com.baticuisine.utils.inputs.InputHandler;
import com.baticuisine.utils.menus.Menu;

public class ProjectMenuImpl implements Menu {

    private final ProjectService projectService;
    private final InputHandler inputHandler;

    public ProjectMenuImpl(ProjectService projectService) {
        this.projectService = projectService;
        this.inputHandler = InputHandler.getInstance(); // Access singleton instance
    }

    @Override
    public void showMenu() {
        System.out.println("--- Recherche de client ---");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");

        int choice = inputHandler.getIntInput("Choisissez une option : ");
        Client client = null;

        if (choice == 1) {
            client = searchExistingClient();
            if (client != null) {
                // If client exists, ask if user wants to add a new project
                String addProject = inputHandler.getStringInput("Souhaitez-vous ajouter un nouveau projet pour ce client ? (y/n) : ");
                if (addProject.equalsIgnoreCase("y")) {
                    addNewProjectForClient(client);
                }
            }
        } else if (choice == 2) {
            String name = inputHandler.getStringInput("Entrez le nom du client : ");
            String phone = inputHandler.getStringInput("Entrez le numéro de téléphone : ");
            String address = inputHandler.getStringInput("Entrez l'adresse : ");
            boolean isPro = inputHandler.getBooleanInput("Est-ce un client professionnel ? (true/false) : ");

            // Collect project information at the same time
            String projectName = inputHandler.getStringInput("Entrez le nom du projet : ");
            double area = inputHandler.getDoubleInput("Entrez la surface de la cuisine (en m²) : ");

            // Add both client and project in one go
            client = projectService.addClient(name, phone, address, isPro, projectName, area);
        }

        if (client != null) {
            System.out.println("Client et projet ajoutés avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout du client ou du projet.");
        }
    }

    private Client searchExistingClient() {
        String clientName = inputHandler.getStringInput("Entrez le nom du client : ");
        Client client = projectService.findClientByName(clientName);
        if (client != null) {
            System.out.println("Client trouvé !");
            System.out.println("Nom : " + client.getName());
            System.out.println("Adresse : " + client.getAddress());
            System.out.println("Numéro de téléphone : " + client.getPhone());
            String continueWithClient = inputHandler.getStringInput("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            if (continueWithClient.equalsIgnoreCase("y")) {
                return client;
            }
        } else {
            System.out.println("Client non trouvé.");
        }
        return null;
    }

    private void addNewProjectForClient(Client client) {
        // Collect the project details for the existing client
        String projectName = inputHandler.getStringInput("Entrez le nom du projet : ");
        double area = inputHandler.getDoubleInput("Entrez la surface de la cuisine (en m²) : ");

        // Add the project for the client
        Project project = projectService.addProjectForClient(client, projectName, area);
        if (project != null) {
            System.out.println("Projet ajouté avec succès pour le client " + client.getName() + " !");
        } else {
            System.out.println("Erreur lors de l'ajout du projet.");
        }
    }


}
