// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import com.baticuisine.config.ApplicationFactory;
import com.baticuisine.utils.menus.MenuFactory;
import com.baticuisine.utils.menus.submenus.MainMenuImpl;
public class Main {
    public static void main(String[] args) {

        System.out.println("=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===");

        ApplicationFactory config = new ApplicationFactory();

        MainMenuImpl mainMenu = MenuFactory.createMainMenu(config.getProjectRepository(), config.getClientRepository());
        mainMenu.showMenu();

    }
}