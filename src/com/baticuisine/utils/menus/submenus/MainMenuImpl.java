package com.baticuisine.utils.menus.submenus;

import com.baticuisine.models.Component;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjectService;
import com.baticuisine.repository.ProjectRepository;
import com.baticuisine.repository.ClientRepository;
import com.baticuisine.utils.inputs.InputHandler;
import com.baticuisine.utils.menus.Menu;

import java.util.List;

public class MainMenuImpl implements Menu {
    private final ProjectService projectService;
    private final ClientService clientService;
    private final InputHandler inputHandler;
    private final ProjectRepository projectRepo;
    private final ClientRepository clientRepo;

    public MainMenuImpl(ProjectRepository projectRepo, ClientRepository clientRepo) {
        this.projectRepo = projectRepo;  // Save references to the repositories
        this.clientRepo = clientRepo;
        this.projectService = new ProjectService(projectRepo, clientRepo);
        this.clientService = new ClientService(clientRepo);
        this.inputHandler = InputHandler.getInstance(); // Access singleton instance
    }

    @Override
    public void showMenu() {
        while (true) {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Calculer le coût d'un projet");
            System.out.println("4. Quitter");

            int choice = inputHandler.getIntInput("Choisissez une option : ");

            switch (choice) {
                case 1:
                    // Pass projectRepo and clientRepo to ProjectMenuImpl constructor
                    ProjectMenuImpl projectMenu = new ProjectMenuImpl(projectRepo, clientRepo);
                    projectMenu.showMenu();
                    break;
                case 2:
                    displayExistingProjects();
                    break;
                case 3:
                    calculateProjectCost();
                    break;
                case 4:
                    System.out.println("Au revoir !");
                    inputHandler.close();
                    return;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void displayExistingProjects() {
        System.out.println("--- Affichage des Projets Existants ---");
        projectService.getAllProjects().forEach(project -> {
            System.out.println(project); // Calls the toString() method of Project

            System.out.println("--- ++++++++++++++++++++++++++++++ ---");
        });
    }



    private void calculateProjectCost() {
        // Implementation of cost calculation logic
    }
}

