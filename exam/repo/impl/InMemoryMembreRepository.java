package exam.repo.impl;

import exam.domain.membre.Membre;
import exam.repo.MembreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implémentation en mémoire du {@link MembreRepository}.
 * <p>
 * Les membres sont stockés dans une {@link Map} où la clé est l'identifiant
 * du membre et la valeur est le membre lui-même.
 * <p>
 * Cette implémentation est destinée aux tests et à l'usage pédagogique,
 * sans persistance externe. Aucune synchronisation n'est effectuée
 * (contexte mono-thread assumé).
 */
public class InMemoryMembreRepository implements MembreRepository {

    /**
     * Structure de stockage interne : Map<Long, Membre>.
     * La clé est l'identifiant du membre.
     */
    private final Map<Long, Membre> membres = new HashMap<>();

    /**
     * {@inheritDoc}
     * <p>
     * Retourne {@link Optional#empty()} si aucun membre avec cet identifiant
     * n'existe dans le repository.
     */
    @Override
    public Optional<Membre> findById(long id) {
        return Optional.ofNullable(membres.get(id));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retourne une liste non modifiable contenant tous les membres.
     * Les modifications apportées à cette liste ne seront pas répercutées
     * sur le repository.
     */
    @Override
    public List<Membre> findAll() {
        return List.copyOf(membres.values());
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Comportement de remplacement :</b> Si un membre avec le même
     * identifiant existe déjà dans le repository, il est complètement
     * remplacé par le nouveau membre. Aucune fusion ou mise à jour
     * partielle n'est effectuée. L'ancienne valeur est simplement
     * écrasée par la nouvelle.
     *
     * @throws IllegalArgumentException si le membre est null
     */
    @Override
    public void save(Membre membre) {
        if (membre == null) {
            throw new IllegalArgumentException("Le membre ne peut pas être null");
        }
        // Si l'identifiant existe déjà, l'ancienne valeur est remplacée
        membres.put(membre.getId(), membre);
    }
}
