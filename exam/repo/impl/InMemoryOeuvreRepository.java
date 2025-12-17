package exam.repo.impl;

import exam.domain.oeuvre.Oeuvre;
import exam.repo.OeuvreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation en mémoire du {@link OeuvreRepository}.
 * <p>
 * Les œuvres sont stockées dans une {@link Map} où la clé est l'identifiant
 * de l'œuvre et la valeur est l'œuvre elle-même.
 * <p>
 * Cette implémentation est destinée aux tests et à la démonstration,
 * sans persistance externe. Aucune synchronisation n'est effectuée
 * (contexte mono-thread assumé).
 */
public class InMemoryOeuvreRepository implements OeuvreRepository {

    /**
     * Structure de stockage interne : Map<Long, Oeuvre>.
     * La clé est l'identifiant de l'œuvre.
     */
    private final Map<Long, Oeuvre> oeuvres = new HashMap<>();

    /**
     * {@inheritDoc}
     * <p>
     * Retourne {@link Optional#empty()} si aucune œuvre avec cet identifiant
     * n'existe dans le repository.
     */
    @Override
    public Optional<Oeuvre> findById(long id) {
        return Optional.ofNullable(oeuvres.get(id));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retourne une liste non modifiable contenant toutes les œuvres.
     * Les modifications apportées à cette liste ne seront pas répercutées
     * sur le repository.
     */
    @Override
    public List<Oeuvre> findAll() {
        return List.copyOf(oeuvres.values());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retourne une liste non modifiable contenant uniquement les œuvres
     * dont la méthode {@link Oeuvre#isDisponible()} retourne true.
     */
    @Override
    public List<Oeuvre> findDisponibles() {
        return oeuvres.values().stream()
                .filter(Oeuvre::isDisponible)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        List::copyOf
                ));
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Comportement de remplacement :</b> Si une œuvre avec le même
     * identifiant existe déjà dans le repository, elle est complètement
     * remplacée par la nouvelle œuvre. Aucune fusion ou mise à jour
     * partielle n'est effectuée. L'ancienne valeur est simplement
     * écrasée par la nouvelle.
     *
     * @throws IllegalArgumentException si l'œuvre est null
     */
    @Override
    public void save(Oeuvre oeuvre) {
        if (oeuvre == null) {
            throw new IllegalArgumentException("L'œuvre ne peut pas être null");
        }
        // Si l'identifiant existe déjà, l'ancienne valeur est remplacée
        oeuvres.put(oeuvre.getId(), oeuvre);
    }
}
