package exam.repo;

import exam.domain.oeuvre.Oeuvre;
import java.util.List;
import java.util.Optional;

/**
 * Repository des œuvres de la médiathèque.
 * <p>
 * Cette interface définit les opérations de persistance pour les œuvres.
 * Les implémentations concrètes (ex : en mémoire) ne doivent pas exposer
 * leur mécanisme de stockage.
 *
 * <p>
 * Conformément au principe DIP, les services métier dépendent de cette
 * abstraction et non des implémentations.
 */
public interface OeuvreRepository {

    /**
     * Recherche une œuvre par son identifiant.
     *
     * @param id identifiant de l'œuvre (strictement positif)
     * @return un {@link Optional} contenant l'œuvre si elle existe,
     *         ou {@link Optional#empty()} sinon
     */
    Optional<Oeuvre> findById(long id);

    /**
     * Retourne la liste de toutes les œuvres enregistrées.
     *
     * @return liste de toutes les œuvres (jamais null, non modifiable)
     */
    List<Oeuvre> findAll();

    /**
     * Retourne uniquement les œuvres disponibles (disponible=true).
     *
     * @return liste des œuvres disponibles (jamais null, non modifiable)
     */
    List<Oeuvre> findDisponibles();

    /**
     * Sauvegarde une œuvre dans le repository.
     * <p>
     * Si une œuvre avec le même identifiant existe déjà,
     * elle est remplacée par la nouvelle valeur.
     * <p>
     * <b>Comportement de remplacement :</b> Si l'identifiant existe déjà,
     * l'ancienne valeur est complètement remplacée par la nouvelle.
     * Aucune fusion ou mise à jour partielle n'est effectuée.
     *
     * @param oeuvre œuvre à sauvegarder (non null)
     * @throws IllegalArgumentException si l'œuvre est null
     */
    void save(Oeuvre oeuvre);
}

