package exam;

import exam.cli.MediathequeCLI;
import exam.repo.EmpruntRepository;
import exam.repo.MembreRepository;
import exam.repo.OeuvreRepository;
import exam.repo.impl.InMemoryEmpruntRepository;
import exam.repo.impl.InMemoryMembreRepository;
import exam.repo.impl.InMemoryOeuvreRepository;
import exam.service.MediathequeService;

/**
 * Point d'entrée principal de l'application de médiathèque.
 * <p>
 * Initialise les repositories, le service et la CLI, puis démarre l'application.
 */
public class Main {

    public static void main(String[] args) {
        // Initialisation des repositories (implémentations en mémoire)
        OeuvreRepository oeuvreRepository = new InMemoryOeuvreRepository();
        MembreRepository membreRepository = new InMemoryMembreRepository();
        EmpruntRepository empruntRepository = new InMemoryEmpruntRepository();

        // Initialisation du service avec les repositories
        MediathequeService service = new MediathequeService(
                oeuvreRepository,
                membreRepository,
                empruntRepository
        );

        // Création et démarrage de la CLI
        MediathequeCLI cli = new MediathequeCLI(service);
        cli.demarrer();
    }
}
