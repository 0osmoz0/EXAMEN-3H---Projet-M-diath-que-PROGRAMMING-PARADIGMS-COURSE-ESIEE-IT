package exam.repo.impl;

import exam.domain.emprunt.Emprunt;
import exam.repo.EmpruntRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation en mémoire du {@link EmpruntRepository}.
 * <p>
 * Les emprunts sont stockés dans une {@link Map} où la clé est l'identifiant
 * de l'emprunt et la valeur est l'emprunt lui-même.
 * <p>
 * Cette implémentation est utilisée à des fins pédagogiques
 * et ne repose sur aucune persistance externe. Aucune synchronisation
 * n'est effectuée (contexte mono-thread assumé).
 */
public class InMemoryEmpruntRepository implements EmpruntRepository {

    /**
     * Structure de stockage interne : Map<Long, Emprunt>.
     * La clé est l'identifiant de l'emprunt.
     */
    private final Map<Long, Emprunt> emprunts = new HashMap<>();

    /**
     * {@inheritDoc}
     * <p>
     * Retourne {@link Optional#empty()} si aucun emprunt avec cet identifiant
     * n'existe dans le repository.
     */
    @Override
    public Optional<Emprunt> findById(long id) {
        return Optional.ofNullable(emprunts.get(id));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retourne une liste non modifiable contenant tous les emprunts.
     * Les modifications apportées à cette liste ne seront pas répercutées
     * sur le repository.
     */
    @Override
    public List<Emprunt> findAll() {
        return List.copyOf(emprunts.values());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retourne une liste non modifiable contenant uniquement les emprunts
     * actifs (dateRetour est null) du membre spécifié.
     */
    @Override
    public List<Emprunt> findActifsByMembreId(long membreId) {
        return emprunts.values().stream()
                .filter(emprunt -> emprunt.getIdMembre() == membreId)
                .filter(Emprunt::estActif)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        List::copyOf
                ));
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Comportement de remplacement :</b> Si un emprunt avec le même
     * identifiant existe déjà dans le repository, il est complètement
     * remplacé par le nouvel emprunt. Aucune fusion ou mise à jour
     * partielle n'est effectuée. L'ancienne valeur est simplement
     * écrasée par la nouvelle.
     *
     * @throws IllegalArgumentException si l'emprunt est null
     */
    @Override
    public void save(Emprunt emprunt) {
        if (emprunt == null) {
            throw new IllegalArgumentException("L'emprunt ne peut pas être null");
        }
        // Si l'identifiant existe déjà, l'ancienne valeur est remplacée
        emprunts.put(emprunt.getId(), emprunt);
    }
}
