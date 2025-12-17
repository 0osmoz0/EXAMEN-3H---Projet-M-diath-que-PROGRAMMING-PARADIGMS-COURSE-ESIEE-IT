package exam.domain.oeuvre;

import java.util.Objects;

/**
 * Représente une œuvre de la médiathèque.
 * <p>
 * Une œuvre possède un identifiant unique, un titre et un état de disponibilité.
 * L'identifiant doit être strictement positif, et le titre ne peut pas être null ou vide.
 * La disponibilité est initialisée à true.
 */
public abstract class Oeuvre {

    private final long id;
    private final String titre;
    private boolean disponible;

    /**
     * Crée une nouvelle œuvre.
     *
     * @param id    identifiant unique strictement positif
     * @param titre titre non null et non vide
     * @throws IllegalArgumentException si id <= 0 ou titre null/blank
     */
    protected Oeuvre(long id, String titre) {
        if (id <= 0)
            throw new IllegalArgumentException("L'id doit être strictement positif");
        this.id = id;

        if (titre == null || titre.isBlank())
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        this.titre = titre;

        this.disponible = true; // état initial
    }

    /**
     * @return l'identifiant unique de l'œuvre
     */
    public long getId() {
        return id;
    }

    /**
     * @return le titre de l'œuvre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @return true si l'œuvre est disponible pour emprunt, false sinon
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Marque l'œuvre comme disponible.
     *
     * @throws IllegalStateException si l'œuvre est déjà disponible
     */
    protected void marquerDisponible() {
        if (disponible)
            throw new IllegalStateException("L'œuvre est déjà disponible");
        disponible = true;
    }

    /**
     * Marque l'œuvre comme indisponible.
     *
     * @throws IllegalStateException si l'œuvre est déjà indisponible
     */
    protected void marquerIndisponible() {
        if (!disponible)
            throw new IllegalStateException("L'œuvre est déjà indisponible");
        disponible = false;
    }

    /**
     * Deux œuvres sont considérées égales si elles ont le même identifiant.
     *
     * @param o l'objet à comparer
     * @return true si les identifiants sont identiques
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oeuvre)) return false;
        Oeuvre oeuvre = (Oeuvre) o;
        return id == oeuvre.id;
    }

    /**
     * Le hashCode est calculé uniquement à partir de l'identifiant.
     *
     * @return le hashCode basé sur l'identifiant
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Représentation lisible de l'œuvre pour debug/logs.
     *
     * @return une chaîne contenant le type, l'id et le titre
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{id=" + id + ", titre='" + titre + "'}";
    }
}
