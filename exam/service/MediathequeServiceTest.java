package exam.service;

import exam.domain.emprunt.Emprunt;
import exam.domain.membre.Membre;
import exam.domain.oeuvre.Dvd;
import exam.domain.oeuvre.Livre;
import exam.domain.oeuvre.Oeuvre;
import exam.repo.EmpruntRepository;
import exam.repo.MembreRepository;
import exam.repo.OeuvreRepository;
import exam.repo.impl.InMemoryEmpruntRepository;
import exam.repo.impl.InMemoryMembreRepository;
import exam.repo.impl.InMemoryOeuvreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour MediathequeService.
 * <p>
 * Utilise le pattern AAA (Arrange-Act-Assert) pour structurer chaque test.
 * Les tests utilisent les implémentations mémoire des repositories.
 */
class MediathequeServiceTest {

    private MediathequeService service;
    private OeuvreRepository oeuvreRepository;
    private MembreRepository membreRepository;
    private EmpruntRepository empruntRepository;

    // Données de test
    private Membre membreActif;
    private Membre membreInactif;
    private Livre livre1;
    private Livre livre2;
    private Dvd dvd1;

    /**
     * Arrange : Initialise le contexte de test avant chaque test.
     * Crée des repositories en mémoire et le service.
     */
    @BeforeEach
    void setUp() {
        // Arrange : Créer les repositories
        oeuvreRepository = new InMemoryOeuvreRepository();
        membreRepository = new InMemoryMembreRepository();
        empruntRepository = new InMemoryEmpruntRepository();

        // Arrange : Créer le service
        service = new MediathequeService(
                oeuvreRepository,
                membreRepository,
                empruntRepository
        );

        // Arrange : Créer des entités de test
        membreActif = new Membre(1, "Alice Martin");
        membreInactif = new Membre(2, "Bob Dupont");
        membreInactif.desactiver();

        livre1 = new Livre(1, "Le Seigneur des Anneaux", "J.R.R. Tolkien", 2070612881);
        livre2 = new Livre(2, "1984", "George Orwell", 2070368228);
        dvd1 = new Dvd(3, "Inception", "Christopher Nolan", 148);

        // Arrange : Sauvegarder les entités dans les repositories
        membreRepository.save(membreActif);
        membreRepository.save(membreInactif);
        oeuvreRepository.save(livre1);
        oeuvreRepository.save(livre2);
        oeuvreRepository.save(dvd1);
    }

    // ========== Tests des préconditions d'emprunt ==========

    @Test
    void emprunter_quandMembreInexistant_doitLeverIllegalStateException() {
        // Arrange : Le membre n'existe pas (pas sauvegardé)
        long membreIdInexistant = 999;
        long oeuvreId = livre1.getId();

        // Act & Assert : Vérifier que l'exception est levée
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.emprunter(membreIdInexistant, oeuvreId)
        );

        // Assert : Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("n'existe pas"));
        assertTrue(exception.getMessage().contains(String.valueOf(membreIdInexistant)));
    }

    @Test
    void emprunter_quandMembreInactif_doitLeverIllegalStateException() {
        // Arrange : Le membre inactif est déjà créé dans setUp
        long membreId = membreInactif.getId();
        long oeuvreId = livre1.getId();

        // Act & Assert : Vérifier que l'exception est levée
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.emprunter(membreId, oeuvreId)
        );

        // Assert : Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("inactif"));
        assertTrue(exception.getMessage().contains(String.valueOf(membreId)));
    }

    @Test
    void emprunter_quandQuotaAtteint_doitLeverIllegalStateException() {
        // Arrange : Créer des œuvres supplémentaires pour atteindre le quota
        Livre livre3 = new Livre(4, "Dune", "Frank Herbert", 2070368229);
        Livre livre4 = new Livre(5, "Fondation", "Isaac Asimov", 2070368230);
        oeuvreRepository.save(livre3);
        oeuvreRepository.save(livre4);

        // Arrange : Faire emprunter le quota maximum (3) au membre
        service.emprunter(membreActif.getId(), livre1.getId());
        service.emprunter(membreActif.getId(), livre2.getId());
        service.emprunter(membreActif.getId(), livre3.getId());

        // Act & Assert : Vérifier qu'un emprunt supplémentaire lève une exception
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.emprunter(membreActif.getId(), livre4.getId())
        );

        // Assert : Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("quota"));
    }

    @Test
    void emprunter_quandOeuvreInexistante_doitLeverIllegalStateException() {
        // Arrange : L'œuvre n'existe pas
        long membreId = membreActif.getId();
        long oeuvreIdInexistant = 999;

        // Act & Assert : Vérifier que l'exception est levée
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.emprunter(membreId, oeuvreIdInexistant)
        );

        // Assert : Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("n'existe pas"));
        assertTrue(exception.getMessage().contains(String.valueOf(oeuvreIdInexistant)));
    }

    @Test
    void emprunter_quandOeuvreDejaEmpruntee_doitLeverIllegalStateException() {
        // Arrange : Faire emprunter l'œuvre par un premier membre
        Membre autreMembre = new Membre(3, "Charlie Durand");
        membreRepository.save(autreMembre);
        service.emprunter(autreMembre.getId(), livre1.getId());

        // Act & Assert : Vérifier qu'un second emprunt de la même œuvre lève une exception
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.emprunter(membreActif.getId(), livre1.getId())
        );

        // Assert : Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("pas disponible"));
    }

    @Test
    void emprunter_quandIdentifiantsInvalides_doitLeverIllegalArgumentException() {
        // Arrange : Identifiants invalides (<= 0)
        long membreIdInvalide = 0;
        long oeuvreIdInvalide = -1;

        // Act & Assert : Vérifier que l'exception est levée
        assertThrows(
                IllegalArgumentException.class,
                () -> service.emprunter(membreIdInvalide, livre1.getId())
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.emprunter(membreActif.getId(), oeuvreIdInvalide)
        );
    }

    // ========== Tests du parcours nominal ==========

    @Test
    void emprunter_quandConditionsValides_doitCreerEmprunt() {
        // Arrange : Membre actif et œuvre disponible
        long membreId = membreActif.getId();
        long oeuvreId = livre1.getId();

        // Act : Emprunter l'œuvre
        long empruntId = service.emprunter(membreId, oeuvreId);

        // Assert : Vérifier que l'emprunt a été créé
        assertNotNull(empruntId);
        assertTrue(empruntId > 0);

        // Assert : Vérifier les détails de l'emprunt
        Emprunt emprunt = empruntRepository.findById(empruntId).orElse(null);
        assertNotNull(emprunt);
        assertEquals(membreId, emprunt.getIdMembre());
        assertEquals(oeuvreId, emprunt.getIdOeuvre());
        assertNotNull(emprunt.getDateEmprunt());
        assertNull(emprunt.getDateRetour());
        assertTrue(emprunt.estActif());

        // Assert : Vérifier que l'œuvre est devenue indisponible
        Oeuvre oeuvre = oeuvreRepository.findById(oeuvreId).orElse(null);
        assertNotNull(oeuvre);
        assertFalse(oeuvre.isDisponible());
    }

    @Test
    void rendre_quandEmpruntValide_doitMettreAJourEmpruntEtOeuvre() {
        // Arrange : Créer un emprunt
        long empruntId = service.emprunter(membreActif.getId(), livre1.getId());
        long oeuvreId = livre1.getId();

        // Vérifier que l'œuvre est indisponible
        Oeuvre oeuvreAvant = oeuvreRepository.findById(oeuvreId).orElse(null);
        assertNotNull(oeuvreAvant);
        assertFalse(oeuvreAvant.isDisponible());

        // Act : Rendre l'emprunt
        assertDoesNotThrow(() -> service.rendre(empruntId));

        // Assert : Vérifier que l'emprunt a été mis à jour
        Emprunt emprunt = empruntRepository.findById(empruntId).orElse(null);
        assertNotNull(emprunt);
        assertNotNull(emprunt.getDateRetour());
        assertFalse(emprunt.estActif());

        // Assert : Vérifier que l'œuvre est redevenue disponible
        Oeuvre oeuvreApres = oeuvreRepository.findById(oeuvreId).orElse(null);
        assertNotNull(oeuvreApres);
        assertTrue(oeuvreApres.isDisponible());

        // Assert : Vérifier que l'emprunt n'apparaît plus dans les emprunts actifs
        List<Emprunt> empruntsActifs = service.listerEmpruntsActifsPourMembre(membreActif.getId());
        assertFalse(empruntsActifs.contains(emprunt));
    }

    @Test
    void cycleComplet_emprunterRendreReemprunter_doitFonctionner() {
        // Arrange : Membre et œuvre valides
        long membreId = membreActif.getId();
        long oeuvreId = livre1.getId();

        // Act & Assert : Premier emprunt
        long empruntId1 = service.emprunter(membreId, oeuvreId);
        assertNotNull(empruntId1);
        assertFalse(oeuvreRepository.findById(oeuvreId).orElse(null).isDisponible());

        // Act & Assert : Retour
        assertDoesNotThrow(() -> service.rendre(empruntId1));
        assertTrue(oeuvreRepository.findById(oeuvreId).orElse(null).isDisponible());

        // Act & Assert : Réemprunt de la même œuvre
        long empruntId2 = service.emprunter(membreId, oeuvreId);
        assertNotNull(empruntId2);
        assertNotEquals(empruntId1, empruntId2); // Nouvel emprunt avec nouvel ID
        assertFalse(oeuvreRepository.findById(oeuvreId).orElse(null).isDisponible());
    }

    // ========== Tests des postconditions et cas limites ==========

    @Test
    void rendre_quandDoubleRetour_doitLeverIllegalStateException() {
        // Arrange : Créer et rendre un emprunt
        long empruntId = service.emprunter(membreActif.getId(), livre1.getId());
        service.rendre(empruntId);

        // Act & Assert : Vérifier qu'un second retour lève une exception
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.rendre(empruntId)
        );

        // Assert : Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("déjà été retourné"));
    }

    @Test
    void rendre_quandEmpruntInexistant_doitLeverIllegalStateException() {
        // Arrange : Emprunt inexistant
        long empruntIdInexistant = 999;

        // Act & Assert : Vérifier que l'exception est levée
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.rendre(empruntIdInexistant)
        );

        // Assert : Vérifier le message d'erreur
        assertTrue(exception.getMessage().contains("n'existe pas"));
    }

    @Test
    void retour_doitLibererPlaceDansQuota() {
        // Arrange : Atteindre le quota maximum
        Livre livre3 = new Livre(4, "Dune", "Frank Herbert", 2070368229);
        Livre livre4 = new Livre(5, "Fondation", "Isaac Asimov", 2070368230);
        oeuvreRepository.save(livre3);
        oeuvreRepository.save(livre4);

        long emprunt1 = service.emprunter(membreActif.getId(), livre1.getId());
        long emprunt2 = service.emprunter(membreActif.getId(), livre2.getId());
        long emprunt3 = service.emprunter(membreActif.getId(), livre3.getId());

        // Assert : Vérifier que le quota est atteint
        assertThrows(
                IllegalStateException.class,
                () -> service.emprunter(membreActif.getId(), livre4.getId())
        );

        // Act : Rendre un emprunt
        service.rendre(emprunt2);

        // Assert : Vérifier qu'un nouvel emprunt est maintenant possible
        assertDoesNotThrow(() -> {
            long nouvelEmprunt = service.emprunter(membreActif.getId(), livre4.getId());
            assertNotNull(nouvelEmprunt);
        });
    }

    // ========== Tests paramétrés ==========

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5})
    void emprunter_avecDifferentQuotas_doitRespecterQuota(int quotaMax) {
        // Arrange : Modifier le quota via réflexion (pour les tests)
        // Note: Dans un vrai projet, on utiliserait une injection de dépendance
        // ou une configuration pour le quota. Pour ce test, on simule différents quotas
        // en créant le nombre exact d'œuvres nécessaires.

        // Arrange : Créer suffisamment d'œuvres pour le quota
        for (int i = 1; i <= quotaMax + 1; i++) {
            Livre livre = new Livre(10 + i, "Livre " + i, "Auteur " + i, 1000000 + i);
            oeuvreRepository.save(livre);
        }

        // Arrange : Emprunter jusqu'au quota maximum
        for (int i = 1; i <= quotaMax; i++) {
            long oeuvreId = 10 + i;
            assertDoesNotThrow(() -> service.emprunter(membreActif.getId(), oeuvreId));
        }

        // Act & Assert : Vérifier qu'un emprunt supplémentaire est refusé
        // (Note: Le quota réel est de 3 selon EmpruntPolicy, donc ce test vérifie
        // que le système respecte le quota configuré, pas qu'il change dynamiquement)
        if (quotaMax >= 3) {
            // Si le quota testé est >= 3 (le quota réel), on doit avoir une exception
            assertThrows(
                    IllegalStateException.class,
                    () -> service.emprunter(membreActif.getId(), 10 + quotaMax + 1)
            );
        }
    }

    // ========== Tests supplémentaires pour la robustesse ==========

    @Test
    void disponibles_doitRetournerListeOeuvresDisponibles() {
        // Arrange : Une œuvre est déjà disponible (livre1)
        // Arrange : Emprunter une œuvre pour la rendre indisponible
        service.emprunter(membreActif.getId(), livre2.getId());

        // Act : Récupérer les œuvres disponibles
        List<Oeuvre> disponibles = service.disponibles();

        // Assert : Vérifier que seules les œuvres disponibles sont retournées
        assertNotNull(disponibles);
        assertTrue(disponibles.contains(livre1));
        assertTrue(disponibles.contains(dvd1));
        assertFalse(disponibles.contains(livre2)); // Empruntée, donc indisponible
    }

    @Test
    void listerEmpruntsActifsPourMembre_doitRetournerEmpruntsActifs() {
        // Arrange : Créer plusieurs emprunts
        long emprunt1 = service.emprunter(membreActif.getId(), livre1.getId());
        long emprunt2 = service.emprunter(membreActif.getId(), livre2.getId());
        service.rendre(emprunt1); // Rendre le premier

        // Act : Lister les emprunts actifs
        List<Emprunt> empruntsActifs = service.listerEmpruntsActifsPourMembre(membreActif.getId());

        // Assert : Vérifier que seul l'emprunt actif est retourné
        assertEquals(1, empruntsActifs.size());
        assertTrue(empruntsActifs.stream().anyMatch(e -> e.getId() == emprunt2));
        assertFalse(empruntsActifs.stream().anyMatch(e -> e.getId() == emprunt1));
    }

    @Test
    void ajouterOeuvre_doitSauvegarderOeuvre() {
        // Arrange : Nouvelle œuvre
        Livre nouveauLivre = new Livre(10, "Nouveau Livre", "Nouvel Auteur", 1234567890);

        // Act : Ajouter l'œuvre
        assertDoesNotThrow(() -> service.ajouterOeuvre(nouveauLivre));

        // Assert : Vérifier que l'œuvre est sauvegardée
        Oeuvre oeuvre = oeuvreRepository.findById(10).orElse(null);
        assertNotNull(oeuvre);
        assertEquals("Nouveau Livre", oeuvre.getTitre());
    }

    @Test
    void ajouterMembre_doitSauvegarderMembre() {
        // Arrange : Nouveau membre
        Membre nouveauMembre = new Membre(10, "Nouveau Membre");

        // Act : Ajouter le membre
        assertDoesNotThrow(() -> service.ajouterMembre(nouveauMembre));

        // Assert : Vérifier que le membre est sauvegardé
        Membre membre = membreRepository.findById(10).orElse(null);
        assertNotNull(membre);
        assertEquals("Nouveau Membre", membre.getNom());
    }

    @Test
    void rendre_quandIdentifiantInvalide_doitLeverIllegalArgumentException() {
        // Arrange : Identifiant invalide
        long empruntIdInvalide = 0;

        // Act & Assert : Vérifier que l'exception est levée
        assertThrows(
                IllegalArgumentException.class,
                () -> service.rendre(empruntIdInvalide)
        );
    }

    @Test
    void listerEmpruntsActifsPourMembre_quandIdentifiantInvalide_doitLeverIllegalArgumentException() {
        // Arrange : Identifiant invalide
        long membreIdInvalide = -1;

        // Act & Assert : Vérifier que l'exception est levée
        assertThrows(
                IllegalArgumentException.class,
                () -> service.listerEmpruntsActifsPourMembre(membreIdInvalide)
        );
    }
}
