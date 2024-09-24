package com.baticuisine.utils.menus.submenus;


        import com.baticuisine.manager.ProjectManager;
        import com.baticuisine.repository.ClientRepository;
        import com.baticuisine.repository.ComponentRepository;
        import com.baticuisine.repository.ProjectRepository;
        import com.baticuisine.services.ClientService;
        import com.baticuisine.services.ProjectService;
        import com.baticuisine.utils.inputs.InputHandler;
        import com.baticuisine.utils.menus.Menu;

public class MainMenuImpl implements Menu {
    private final InputHandler inputHandler;
    private final ProjectManager projectManager;
    private final ClientRepository clientRepo;
    private final ProjectRepository projectRepo;
    private final ComponentRepository componentRepo;
    private final ClientService clientService;

    public MainMenuImpl(ProjectRepository projectRepo, ClientRepository clientRepo, ComponentRepository componentRepo, ClientService clientService) {
        this.clientService = new ClientService(clientRepo);
        ProjectService projectService = new ProjectService(projectRepo, componentRepo, clientService);
        this.projectManager = new ProjectManager(projectService, projectRepo, componentRepo, clientService);
        this.projectRepo = projectRepo;
        this.componentRepo = componentRepo;
        this.clientRepo = clientRepo;
        this.inputHandler = InputHandler.getInstance();
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
                    ProjectMenuImpl projectMenu = new ProjectMenuImpl(projectRepo, clientRepo, componentRepo, clientService);
                    projectMenu.showMenu();
                    break;
                case 2:
                    projectManager.displayExistingProjects();
                    break;
                case 3:
                    projectManager.calculateProjectCost();
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
}

