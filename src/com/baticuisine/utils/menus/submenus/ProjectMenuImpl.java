package com.baticuisine.utils.menus.submenus;

import com.baticuisine.models.Client;
import com.baticuisine.models.Project;
import com.baticuisine.models.Material;
import com.baticuisine.models.Workforce;
import com.baticuisine.repository.ClientRepository;
import com.baticuisine.repository.ComponentRepository;
import com.baticuisine.repository.ProjectRepository;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjectService;
import com.baticuisine.utils.inputs.InputHandler;
import com.baticuisine.utils.menus.Menu;

import java.util.ArrayList;
import java.util.List;

public class ProjectMenuImpl implements Menu {

    private final ProjectService projectService;
    private final ClientService clientService;
    private final InputHandler inputHandler;

    public ProjectMenuImpl(ProjectRepository projectRepo, ClientRepository clientRepo, ComponentRepository componentRepo, ClientService clientService) {
        this.projectService = new ProjectService(projectRepo, componentRepo, clientService);
        this.clientService = new ClientService(clientRepo);
        this.inputHandler = InputHandler.getInstance();
    }


    public void showMenu() {
        System.out.println("--- Recherche de client ---");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");

        int choice = inputHandler.getIntInput("Choisissez une option : ");
        Client client = null;

        switch (choice) {
            case 1:
                client = searchExistingClient();
                if (client != null) {
                    String addProject = inputHandler.getStringInput("Souhaitez-vous ajouter un nouveau projet pour ce client ? (y/n) : ");
                    if (addProject.equalsIgnoreCase("y")) {
                        addProjectForExistingClient(client);
                    }
                }
                break;
            case 2:
                client = addClientAndProject();
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }

        if (client != null) {
            System.out.println("Client et projet ajoutés avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout du client ou du projet.");
        }
    }

    private Client searchExistingClient() {
        String clientName = inputHandler.getStringInput("Entrez le nom du client : ");
        Client client = clientService.findClientByName(clientName);
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

    private Client addProjectForClient(Client existingClient, String name, String phone, String address, boolean isPro, String projectName, double area, List<Material> materials, List<Workforce> workforces) {
        Client client;
        if (existingClient != null) {
            client = existingClient;
        } else {
            client = new Client(name, phone, address, isPro);
        }

        Project project = projectService.addProjectForClient(client, projectName, area);
        if (project != null) {
            client.addProject(project);
            System.out.println("Projet ajouté avec succès pour le client " + client.getName() + " !");
        } else {
            System.out.println("Erreur lors de l'ajout du projet.");
            return null;
        }

        if (existingClient == null) {
            return clientService.addClient(name, phone, address, isPro, projectName, area, materials, workforces);
        }

        return client;
    }

    private Client addClientAndProject() {
        String name = inputHandler.getStringInput("Entrez le nom du client : ");
        String phone = inputHandler.getStringInput("Entrez le numéro de téléphone : ");
        String address = inputHandler.getStringInput("Entrez l'adresse : ");
        boolean isPro = inputHandler.getBooleanInput("Est-ce un client professionnel ? (true/false) : ");

        List<Material> materials = addMaterials();
        List<Workforce> workforces = addWorkforce();

        String projectName = inputHandler.getStringInput("Entrez le nom du projet : ");
        double area = inputHandler.getDoubleInput("Entrez la surface de la cuisine (en m²) : ");

        Client client = clientService.addClient(name, phone, address, isPro, projectName, area, materials, workforces);

        if (client == null) {
            System.out.println("Erreur lors de l'ajout du client.");
            return null;
        }

        return client;
    }

    private void addProjectForExistingClient(Client client) {
        String projectName = inputHandler.getStringInput("Entrez le nom du projet : ");
        double area = inputHandler.getDoubleInput("Entrez la surface de la cuisine (en m²) : ");

        List<Material> materials = addMaterials();
        List<Workforce> workforces = addWorkforce();

        addProjectForClient(client, null, null, null, false, projectName, area, materials, workforces);
    }
}
