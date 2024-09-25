package com.baticuisine.manager;

import com.baticuisine.models.*;
import com.baticuisine.repository.ComponentRepository;
import com.baticuisine.repository.ProjectRepository;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.DevisService;
import com.baticuisine.services.ProjectService;
import com.baticuisine.utils.inputs.InputHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectManager {
    private final ProjectService projectService;

    private final DevisService devisService;
    private final InputHandler inputHandler;

    public ProjectManager(ProjectService projectService, ProjectRepository projectRepo, ComponentRepository componentRepo, ClientService clientService) {
        this.projectService = projectService;
        this.devisService = new DevisService(projectRepo, componentRepo, clientService);
        this.inputHandler = InputHandler.getInstance();
    }

    private void createQuotationForProject(Project project) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("Création d'un devis pour le projet : " + project.getProjectName());

        System.out.print("Entrez la date d'émission du devis (format : jj/mm/aaaa) : ");
        LocalDate emissionDate;
        while (true) {
            try {
                String emissionInput = scanner.nextLine();
                emissionDate = LocalDate.parse(emissionInput, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer (format : jj/mm/aaaa) : ");
            }
        }

        LocalDate validityDate;
        while (true) {
            System.out.print("Entrez la date de validité du devis (format : jj/mm/aaaa) : ");
            try {
                String validityInput = scanner.nextLine();
                validityDate = LocalDate.parse(validityInput, formatter);

                // Validate that validityDate is after emissionDate
                if (validityDate.isBefore(emissionDate)) {
                    System.out.println("La date de validité doit être après la date d'émission. Veuillez réessayer : ");
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer (format : jj/mm/aaaa) : ");
            }
        }

        System.out.print("Souhaitez-vous enregistrer le devis ? (y/n) : ");
        boolean saveDevis = scanner.next().equalsIgnoreCase("y");

        if (saveDevis) {
            Devis devis = new Devis(emissionDate, validityDate, project.getId());
            Optional<Devis> optionalDevis = devisService.saveDevis(devis);

            if (optionalDevis.isPresent()) {
                Devis savedDevis = optionalDevis.get();
                System.out.println("Devis enregistré avec succès !");

                System.out.print("Souhaitez-vous accepter ce devis ? (y/n) : ");
                boolean acceptQuote = scanner.next().equalsIgnoreCase("y");

                if (acceptQuote) {
                    savedDevis.setAccepted(true);
                    devisService.updateDevisAcceptance(savedDevis);
                    System.out.println("Le devis a été accepté avec succès !");
                } else {
                    System.out.println("Le devis n'a pas été accepté.");
                }

                // Show the quote details again (with the acceptance status updated)
                displayQuoteWithProjectDetails(project, savedDevis);

            } else {
                System.out.println("Erreur lors de l'enregistrement du devis.");
            }
        } else {
            System.out.println("Création du devis annulée.");
        }
    }


    private void displayQuoteWithProjectDetails(Project project, Devis devis) {
        System.out.println("----- Détails du Projet -----");
        System.out.println("Nom du projet : " + project.getProjectName());

        System.out.println("\n----- Composants du Projet -----");

        List<Component> components = project.getComponents();

        for (Component component : components) {
            System.out.println(component.toString());
        }

        System.out.printf("Coût total : %.2f\n", project.getTotalCost());
        System.out.println("-----------------------------------");

        System.out.println("\n----- Détails du Devis -----");
        System.out.println("Date d'émission : " + devis.getEmissionDate());
        System.out.println("Date de validité : " + devis.getValidityDate());
        System.out.println("Est accepté : " + (devis.isAccepted() ? "Oui" : "Non"));
        System.out.println("Montant estimé : " + devis.getEstimatedAmount());
        System.out.println("-----------------------------------");
    }


    public void displayExistingProjects() {
        System.out.println("--- Affichage des Projets Existants ---");
        List<Project> projects = projectService.getAllProjects();
        projects.forEach(project -> {
            System.out.println(project);
            System.out.println("--- ++++++++++++++++++++++++++++++ ---");
        });

        Scanner scanner = new Scanner(System.in);
        System.out.print("Souhaitez-vous créer un devis pour un projet ? (y/n) : ");
        boolean createQuote = scanner.next().equalsIgnoreCase("y");

        if (createQuote) {
            System.out.print("Entrez le nom du projet pour lequel vous souhaitez créer un devis : ");
            String projectName = scanner.next();

            Optional<Project> optionalProject = projectService.findByName(projectName);
            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                createQuotationForProject(project);
            } else {
                System.out.println("Aucun projet trouvé avec le nom : " + projectName);
            }
        }
    }


    public void calculateProjectCost() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();

        Optional<Project> optionalProject = projectService.findByName(projectName);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();

            if (project.isCostCalculated()) {
                System.out.println("Le coût du projet " + project.getProjectName() + " a déjà été calculé.");
                return;
            }

            System.out.println("=== Calcul du coût du projet : " + project.getProjectName() + " ===");
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            final double tvaPercentage = scanner.nextDouble() / 100;

            System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
            boolean applyMargin = scanner.next().equalsIgnoreCase("y");
            final double profitMargin = applyMargin ? getProfitMargin(scanner) : 0;

            System.out.println("--- Détail des Coûts pour le projet : " + project.getProjectName() + " ---");

            double totalMaterialCost = calculateMaterialCost(project, tvaPercentage);
            double totalWorkforceCost = calculateWorkforceCost(project, tvaPercentage);

            double totalCostBeforeMargin = totalMaterialCost + totalWorkforceCost;

            System.out.println("+------------------------+------------------------+");
            System.out.printf("| %-22s | %22s |%n", "Item", "Cost (€)");
            System.out.println("+------------------------+------------------------+");
            System.out.printf("| %-22s | %22.2f |%n", "Coût Matériaux (avec TVA)", totalMaterialCost);
            System.out.printf("| %-22s | %22.2f |%n", "Coût Main-d'œuvre (avec TVA)", totalWorkforceCost);
            System.out.printf("| %-22s | %22.2f |%n", "Coût total avant marge", totalCostBeforeMargin);

            double finalCost = calculateFinalCost(totalCostBeforeMargin, profitMargin);
            System.out.printf("| %-22s | %22.2f |%n", "Coût total final", finalCost);
            System.out.println("+------------------------+------------------------+");

            project.setTotalCost(finalCost);
            project.setProfitMargin(profitMargin);
            project.setCostCalculated(true);

            projectService.updateProjectCostInfo(project);

        } else {
            System.out.println("Aucun projet trouvé avec le nom : " + projectName);
        }
    }

    private double getProfitMargin(Scanner scanner) {
        System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
        return scanner.nextDouble() / 100;
    }

    private double calculateMaterialCost(Project project, double tvaPercentage) {
        System.out.println("Calculating material costs for project: " + project.getProjectName());
        System.out.println("+--------------------------------------------------------------+");
        System.out.printf("| %-20s | %-10s | %-10s | %-10s | %-10s |%n", "Matériau", "Quantité", "Coût Unitaire (€)", "Transport (€)", "Coût (€)");
        System.out.println("+--------------------------------------------------------------+");

        double totalMaterialCost = project.getComponents().stream()
                .filter(Material.class::isInstance)
                .map(Material.class::cast)
                .peek(material -> System.out.printf("| %-20s | %-10d | %-10.2f | %-10.2f | %-10.2f |%n",
                        material.getName(),
                        material.getQuantity(),
                        material.getUnitCost(),
                        material.getTransportCost(),
                        material.calculateCost()))
                .mapToDouble(Component::calculateCost)
                .sum();

        System.out.println("+--------------------------------------------------------------+");
        double materialCostWithTVA = totalMaterialCost + (totalMaterialCost * tvaPercentage);
        System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**%n", tvaPercentage * 100, materialCostWithTVA);
        return materialCostWithTVA;
    }

    private double calculateWorkforceCost(Project project, double tvaPercentage) {
        System.out.println("+---------------------------------------------------------------+");
        System.out.printf("| %-20s | %-10s | %-15s | %-10s | %-10s |%n", "Travailleur", "Heures", "Taux Horaire (€)", "Productivité", "Coût (€)");
        System.out.println("+---------------------------------------------------------------+");

        double totalWorkforceCost = project.getComponents().stream()
                .filter(Workforce.class::isInstance)
                .map(Workforce.class::cast)
                .peek(workforce -> System.out.printf("| %-20s | %-10d | %-15.2f | %-10.2f | %-10.2f |%n",
                        workforce.getName(),
                        workforce.getWorkHours(),
                        workforce.getHourlyRate(),
                        workforce.getWorkerProductivity(),
                        workforce.calculateCost()))
                .mapToDouble(Component::calculateCost)
                .sum();

        System.out.println("+---------------------------------------------------------------+");
        double workforceCostWithTVA = totalWorkforceCost + (totalWorkforceCost * tvaPercentage);
        System.out.printf("**Coût total de la main-d'œuvre avec TVA (%.0f%%) : %.2f €**%n", tvaPercentage * 100, workforceCostWithTVA);
        return workforceCostWithTVA;
    }

    private double calculateFinalCost(double totalCostBeforeMargin, double profitMargin) {
        if (profitMargin > 0) {
            double marginAmount = totalCostBeforeMargin * profitMargin;
            System.out.printf("4. Marge bénéficiaire (%.0f%%) : %.2f €%n", profitMargin * 100, marginAmount);
            return totalCostBeforeMargin + marginAmount;
        }
        return totalCostBeforeMargin;
    }
}
