package exam.util;

/**
 * Générateur d'identifiants uniques.
 * <p>
 * Génère des identifiants séquentiels à partir de 1.
 */
public class IdGenerator {

    private long nextId = 1;

    /**
     * Génère et retourne le prochain identifiant unique.
     *
     * @return le prochain identifiant (strictement positif)
     */
    public long nextId() {
        return nextId++;
    }
}
