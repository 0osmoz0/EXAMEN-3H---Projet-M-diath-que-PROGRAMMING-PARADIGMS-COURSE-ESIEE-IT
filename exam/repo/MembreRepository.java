package exam.repo;

import exam.domain.membre.Membre;
import java.util.List;
import java.util.Optional;

/**
 * Repository des membres de la médiathèque.
 * <p>
 * Cette interface définit les opérations de persistance des membres.
 * Elle est utilisée par les services métier pour accéder aux membres
 * sans dépendre d’une implémentation concrète (DIP).
 */
public interface MembreRepository {

    /**
     * Recherche un membre par son identifiant.
     *
     * @param id identifiant du membre (strictement positif)
     * @return un {@link Optional} contenant le membre s'il existe,
     *         ou {@link Optional#empty()} sinon
     */
    Optional<Membre> findById(long id);

    /**
     * Retourne la liste de tous les membres enregistrés.
     *
     * @return liste de tous les membres (jamais null, non modifiable)
     */
    List<Membre> findAll();

    /**
     * Sauvegarde un membre dans le repository.
     * <p>
     * Si un membre avec le même identifiant existe déjà,
     * il est remplacé par la nouvelle valeur.
     * <p>
     * <b>Comportement de remplacement :</b> Si l'identifiant existe déjà,
     * l'ancienne valeur est complètement remplacée par la nouvelle.
     * Aucune fusion ou mise à jour partielle n'est effectuée.
     *
     * @param membre membre à sauvegarder (non null)
     * @throws IllegalArgumentException si le membre est null
     */
    void save(Membre membre);
}

