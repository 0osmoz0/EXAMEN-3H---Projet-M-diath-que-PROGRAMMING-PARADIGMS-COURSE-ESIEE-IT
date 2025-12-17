package exam.domain.oeuvre;

/**
 * Représente un DVD dans la médiathèque.
 * <p>
 * Un DVD possède un réalisateur et une durée en minutes en plus des attributs hérités d'Oeuvre.
 * Le réalisateur ne peut pas être null ou vide, et la durée doit être strictement positive.
 */
public final class Dvd extends Oeuvre {

    private final String realisateur;
    private final int duree;

    /**
     * Crée un nouveau DVD.
     *
     * @param id          identifiant unique > 0
     * @param titre       titre non null et non vide
     * @param realisateur nom du réalisateur non null et non vide
     * @param duree       durée en minutes strictement positive
     * @throws IllegalArgumentException si id <= 0, titre null/blank, realisateur null/blank, ou duree <= 0
     */
    public Dvd(long id, String titre, String realisateur, int duree) {
        super(id, titre);

        if (realisateur == null || realisateur.isBlank())
            throw new IllegalArgumentException("Le réalisateur ne peut pas être vide");
        this.realisateur = realisateur;

        if (duree <= 0)
            throw new IllegalArgumentException("La durée doit être strictement positive");
        this.duree = duree;
    }

    /**
     * @return le nom du réalisateur
     */
    public String getRealisateur() {
        return realisateur;
    }

    /**
     * @return la durée en minutes
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Représentation lisible du DVD.
     *
     * @return chaîne contenant type, id, titre, réalisateur et durée
     */
    @Override
    public String toString() {
        return super.toString() + ", realisateur='" + realisateur + "', duree=" + duree +
                " minutes"; 
    }
}   