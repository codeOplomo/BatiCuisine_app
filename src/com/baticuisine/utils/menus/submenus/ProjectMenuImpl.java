package com.baticuisine.utils.menus.submenus;

import com.baticuisine.models.Client;
import com.baticuisine.models.Project;
import com.baticuisine.models.Material;
import com.baticuisine.models.Workforce;
import com.baticuisine.services.ProjectService;
import com.baticuisine.utils.inputs.InputHandler;
import com.baticuisine.utils.menus.Menu;

import java.util.ArrayList;
import java.util.List;

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
                String addProject = inputHandler.getStringInput("Souhaitez-vous ajouter un nouveau projet pour ce client ? (y/n) : ");
                if (addProject.equalsIgnoreCase("y")) {
                    addNewProjectForClient(client);
                }
            }
        } else if (choice == 2) {
            client = addNewClientAndProject();
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

    private Client addNewClientAndProject() {
        String name = inputHandler.getStringInput("Entrez le nom du client : ");
        String phone = inputHandler.getStringInput("Entrez le numéro de téléphone : ");
        String address = inputHandler.getStringInput("Entrez l'adresse : ");
        boolean isPro = inputHandler.getBooleanInput("Est-ce un client professionnel ? (true/false) : ");
        String projectName = inputHandler.getStringInput("Entrez le nom du projet : ");
        double area = inputHandler.getDoubleInput("Entrez la surface de la cuisine (en m²) : ");

        List<Material> materials = addMaterials(); // Gather materials
        List<Workforce> workforces = addWorkforce(); // Gather workforces

        return projectService.addClient(name, phone, address, isPro, projectName, area, materials, workforces);
    }

    private void addNewProjectForClient(Client client) {
        String projectName = inputHandler.getStringInput("Entrez le nom du projet : ");
        double area = inputHandler.getDoubleInput("Entrez la surface de la cuisine (en m²) : ");

        Project project = projectService.addProjectForClient(client, projectName, area);
        if (project != null) {
            System.out.println("Projet ajouté avec succès pour le client " + client.getName() + " !");
        } else {
            System.out.println("Erreur lors de l'ajout du projet.");
        }
    }

    private List<Material> addMaterials() {
        List<Material> materials = new ArrayList<>();
        boolean continueAddingMaterials;

        do {
            String materialName = inputHandler.getStringInput("Entrez le nom du matériau : ");
            int quantity = (int) inputHandler.getDoubleInput("Entrez la quantité de ce matériau (en unités appropriées) : "); // Ensure quantity is an int
            double unitCost = inputHandler.getDoubleInput("Entrez le coût unitaire de ce matériau : ");
            double transportCost = inputHandler.getDoubleInput("Entrez le coût de transport de ce matériau : ");
            double qualityCoefficient = inputHandler.getDoubleInput("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");

            Material material = new Material(materialName, "material", 0.0, unitCost, quantity, transportCost, qualityCoefficient);
            materials.add(material);

            String addAnother = inputHandler.getStringInput("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            continueAddingMaterials = addAnother.equalsIgnoreCase("y");
        } while (continueAddingMaterials);

        return materials;
    }


    private List<Workforce> addWorkforce() {
        List<Workforce> workforces = new ArrayList<>();
        boolean continueAddingWorkforce;

        do {
            String workforceType = inputHandler.getStringInput("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
            double hourlyRate = inputHandler.getDoubleInput("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
            int hoursWorked = inputHandler.getIntInput("Entrez le nombre d'heures travaillées : ");
            double productivityFactor = inputHandler.getDoubleInput("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");

            Workforce workforce = new Workforce(workforceType, "workforce", 0, hourlyRate, hoursWorked, productivityFactor);
            workforces.add(workforce);

            String addAnother = inputHandler.getStringInput("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            continueAddingWorkforce = addAnother.equalsIgnoreCase("y");
        } while (continueAddingWorkforce);

        return workforces;
    }
}
