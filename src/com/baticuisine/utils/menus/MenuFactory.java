package com.baticuisine.utils.menus;

import com.baticuisine.repository.ClientRepository;
import com.baticuisine.repository.ComponentRepository;
import com.baticuisine.repository.ProjectRepository;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjectService;
import com.baticuisine.utils.menus.submenus.MainMenuImpl;
import com.baticuisine.utils.menus.submenus.ProjectMenuImpl;

public class MenuFactory {

    public static MainMenuImpl createMainMenu(ProjectRepository projectRepo, ClientRepository clientRepo, ComponentRepository componentRepo, ClientService clientService) {
        return new MainMenuImpl(projectRepo, clientRepo, componentRepo, clientService);
    }

    public static ProjectMenuImpl createProjectMenu(ProjectRepository projectRepo, ClientRepository clientRepo, ComponentRepository componentRepo, ClientService clientService) {
        return new ProjectMenuImpl(projectRepo, clientRepo, componentRepo, clientService);
    }
    /*public static Menu getMenu(String menuType) {
        if (menuType == null) {
            return null;
        }
        if (menuType.equalsIgnoreCase("main")) {
            return new MainMenu();
        } else if (menuType.equalsIgnoreCase("client")) {
            return new ClientMenu();
        } else if (menuType.equalsIgnoreCase("component")) {
            return new ComponentMenu();
        } else if (menuType.equalsIgnoreCase("devis")) {
            return new DevisMenu();
        }
        return null;
    }*/
}
