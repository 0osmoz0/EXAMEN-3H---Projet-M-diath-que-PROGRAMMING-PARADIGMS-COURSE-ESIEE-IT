package exam.cli;

import exam.domain.emprunt.Emprunt;
import exam.domain.membre.Membre;
import exam.domain.oeuvre.Dvd;
import exam.domain.oeuvre.Livre;
import exam.domain.oeuvre.Oeuvre;
import exam.service.MediathequeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Interface en ligne de commande pour la médiathèque.
 * <p>
 * Cette classe gère l'interaction avec l'utilisateur et délègue toute
 * la logique métier au service. Elle ne contient aucune logique métier.
 */
public class MediathequeCLI {

    private final MediathequeService service;
    private final Scanner scanner;

    /**
     * Crée une nouvelle instance de la CLI.
     *
     * @param service le service de la médiathèque (non null)
     */
    public MediathequeCLI(MediathequeService service) {
        if (service == null) {
            throw new IllegalArgumentException("Le service ne peut pas être null");
        }
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Point d'entrée principal de l'application.
     * Initialise les données de test et lance la boucle du menu.
     */
    public void demarrer() {
        System.out.println("=== Bienvenue dans la Médiathèque ===");
        System.out.println();

        // Initialiser les données de test
        initialiserDonneesTest();

        // Boucle principale du menu
        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = lireChoixMenu();
            System.out.println();

            switch (choix) {
                case 1:
                    listerOeuvresDisponibles();
                    break;
                case 2:
                    emprunterOeuvre();
                    break;
                case 3:
                    rendreOeuvre();
                    break;
                case 4:
                    listerEmpruntsActifs();
                    break;
                case 5:
                    ajouterOeuvre();
                    break;
                case 6:
                    ajouterMembre();
                    break;
                case 0:
                    continuer = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("❌ Choix invalide. Veuillez choisir un nombre entre 0 et 6.");
            }

            if (continuer) {
                System.out.println();
                System.out.println("Appuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    /**
     * Affiche le menu principal.
     */
    private void afficherMenu() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║        MENU PRINCIPAL              ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Lister les œuvres disponibles  ║");
        System.out.println("║ 2. Emprunter une œuvre            ║");
        System.out.println("║ 3. Rendre une œuvre               ║");
        System.out.println("║ 4. Lister les emprunts actifs     ║");
        System.out.println("║ 5. Ajouter une œuvre              ║");
        System.out.println("║ 6. Ajouter un membre              ║");
        System.out.println("║ 0. Quitter                        ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.print("Votre choix : ");
    }

    /**
     * Lit le choix de l'utilisateur dans le menu.
     * Redemande tant que l'entrée n'est pas valide.
     *
     * @return le choix de l'utilisateur (0-6)
     */
    private int lireChoixMenu() {
        while (true) {
            try {
                String ligne = scanner.nextLine().trim();
                int choix = Integer.parseInt(ligne);
                if (choix >= 0 && choix <= 6) {
                    return choix;
                } else {
                    System.out.print("❌ Veuillez entrer un nombre entre 0 et 6 : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("❌ Entrée invalide. Veuillez entrer un nombre : ");
            }
        }
    }

    /**
     * Option 1 : Liste toutes les œuvres disponibles.
     */
    private void listerOeuvresDisponibles() {
        try {
            List<Oeuvre> oeuvres = service.disponibles();
            if (oeuvres.isEmpty()) {
                System.out.println("ℹ️  Aucune œuvre disponible actuellement.");
            } else {
                System.out.println("📚 Œuvres disponibles (" + oeuvres.size() + ") :");
                System.out.println();
                for (Oeuvre oeuvre : oeuvres) {
                    afficherOeuvre(oeuvre);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la récupération des œuvres : " + e.getMessage());
        }
    }

    /**
     * Affiche les détails d'une œuvre.
     *
     * @param oeuvre l'œuvre à afficher
     */
    private void afficherOeuvre(Oeuvre oeuvre) {
        System.out.print("  • ID: " + oeuvre.getId() + " | ");
        System.out.print("Type: " + oeuvre.getClass().getSimpleName() + " | ");
        System.out.print("Titre: " + oeuvre.getTitre());

        if (oeuvre instanceof Livre) {
            Livre livre = (Livre) oeuvre;
            System.out.print(" | Auteur: " + livre.getAuteur() + " | ISBN: " + livre.getIsbn());
        } else if (oeuvre instanceof Dvd) {
            Dvd dvd = (Dvd) oeuvre;
            System.out.print(" | Réalisateur: " + dvd.getRealisateur() + " | Durée: " + dvd.getDuree() + " min");
        }

        System.out.println();
    }

    /**
     * Option 2 : Emprunte une œuvre.
     */
    private void emprunterOeuvre() {
        try {
            long membreId = lireLong("Identifiant du membre : ");
            long oeuvreId = lireLong("Identifiant de l'œuvre : ");

            long empruntId = service.emprunter(membreId, oeuvreId);
            System.out.println("✅ Emprunt créé avec succès ! ID de l'emprunt : " + empruntId);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue lors de l'emprunt : " + e.getMessage());
        }
    }

    /**
     * Option 3 : Rend une œuvre.
     */
    private void rendreOeuvre() {
        try {
            long empruntId = lireLong("Identifiant de l'emprunt : ");

            service.rendre(empruntId);
            System.out.println("✅ Œuvre rendue avec succès !");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue lors du retour : " + e.getMessage());
        }
    }

    /**
     * Option 4 : Liste les emprunts actifs d'un membre.
     */
    private void listerEmpruntsActifs() {
        try {
            long membreId = lireLong("Identifiant du membre : ");

            List<Emprunt> emprunts = service.listerEmpruntsActifsPourMembre(membreId);
            if (emprunts.isEmpty()) {
                System.out.println("ℹ️  Aucun emprunt actif pour ce membre.");
            } else {
                System.out.println("📋 Emprunts actifs (" + emprunts.size() + ") :");
                System.out.println();
                for (Emprunt emprunt : emprunts) {
                    System.out.println("  • ID: " + emprunt.getId() +
                            " | Œuvre: " + emprunt.getIdOeuvre() +
                            " | Date emprunt: " + emprunt.getDateEmprunt());
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue : " + e.getMessage());
        }
    }

    /**
     * Option 5 : Ajoute une nouvelle œuvre.
     */
    private void ajouterOeuvre() {
        try {
            System.out.println("Type d'œuvre :");
            System.out.println("  1. Livre");
            System.out.println("  2. DVD");
            int typeChoix = lireInt("Votre choix (1 ou 2) : ", 1, 2);

            long id = lireLong("Identifiant : ");
            String titre = lireString("Titre : ");

            if (typeChoix == 1) {
                // Livre
                String auteur = lireString("Auteur : ");
                int isbn = lireInt("ISBN : ", 1, Integer.MAX_VALUE);
                Livre livre = new Livre(id, titre, auteur, isbn);
                service.ajouterOeuvre(livre);
                System.out.println("✅ Livre ajouté avec succès !");
            } else {
                // DVD
                String realisateur = lireString("Réalisateur : ");
                int duree = lireInt("Durée (en minutes) : ", 1, Integer.MAX_VALUE);
                Dvd dvd = new Dvd(id, titre, realisateur, duree);
                service.ajouterOeuvre(dvd);
                System.out.println("✅ DVD ajouté avec succès !");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue : " + e.getMessage());
        }
    }

    /**
     * Option 6 : Ajoute un nouveau membre.
     */
    private void ajouterMembre() {
        try {
            long id = lireLong("Identifiant : ");
            String nom = lireString("Nom : ");

            Membre membre = new Membre(id, nom);
            service.ajouterMembre(membre);
            System.out.println("✅ Membre ajouté avec succès !");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue : " + e.getMessage());
        }
    }

    /**
     * Initialise des données de test pour faciliter les tests.
     */
    private void initialiserDonneesTest() {
        try {
            System.out.println("Initialisation des données de test...");

            // Membres
            service.ajouterMembre(new Membre(1, "Alice Martin"));
            service.ajouterMembre(new Membre(2, "Bob Dupont"));
            // Membre inactif
            Membre membreInactif = new Membre(3, "Charlie Durand");
            membreInactif.desactiver();
            service.ajouterMembre(membreInactif);

            // Œuvres
            service.ajouterOeuvre(new Livre(1, "Le Seigneur des Anneaux", "J.R.R. Tolkien", 2070612881));
            service.ajouterOeuvre(new Livre(2, "1984", "George Orwell", 2070368228));
            service.ajouterOeuvre(new Dvd(3, "Inception", "Christopher Nolan", 148));
            service.ajouterOeuvre(new Dvd(4, "The Matrix", "Wachowski", 136));

            System.out.println("✅ Données de test initialisées avec succès !");
            System.out.println("   - 3 membres (2 actifs, 1 inactif)");
            System.out.println("   - 4 œuvres (2 livres, 2 DVDs)");
            System.out.println();
        } catch (Exception e) {
            System.out.println("⚠️  Erreur lors de l'initialisation des données de test : " + e.getMessage());
        }
    }

    // ========== Méthodes utilitaires pour la lecture robuste ==========

    /**
     * Lit un entier long depuis l'entrée standard.
     * Redemande tant que l'entrée n'est pas valide.
     *
     * @param prompt le message à afficher
     * @return l'entier long lu
     */
    private long lireLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String ligne = scanner.nextLine().trim();
                return Long.parseLong(ligne);
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide.");
            }
        }
    }

    /**
     * Lit un entier depuis l'entrée standard.
     * Redemande tant que l'entrée n'est pas valide ou n'est pas dans la plage.
     *
     * @param prompt le message à afficher
     * @param min    la valeur minimale acceptée
     * @param max    la valeur maximale acceptée
     * @return l'entier lu
     */
    private int lireInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String ligne = scanner.nextLine().trim();
                int valeur = Integer.parseInt(ligne);
                if (valeur >= min && valeur <= max) {
                    return valeur;
                } else {
                    System.out.println("❌ Veuillez entrer un nombre entre " + min + " et " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide.");
            }
        }
    }

    /**
     * Lit une chaîne de caractères depuis l'entrée standard.
     * Redemande tant que la chaîne est vide.
     *
     * @param prompt le message à afficher
     * @return la chaîne lue (non vide)
     */
    private String lireString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String ligne = scanner.nextLine().trim();
            if (!ligne.isEmpty()) {
                return ligne;
            } else {
                System.out.println("❌ Cette valeur ne peut pas être vide.");
            }
        }
    }
}
