package exam.domain.membre;

/**
 * Représente un membre de la médiathèque.
 * <p>
 * Un membre possède un identifiant unique, un nom et un statut (ACTIF ou INACTIF).
 * Le statut est initialisé à ACTIF par défaut.
 */
public final class Membre {

    private final long id;
    private final String nom;
    private StatutMembre statut;

    /**
     * Crée un nouveau membre actif.
     *
     * @param id  identifiant unique strictement positif
     * @param nom nom non null et non vide
     * @throws IllegalArgumentException si id <= 0 ou nom null/blank
     */
    public Membre(long id, String nom) {
        if (id <= 0) throw new IllegalArgumentException("id invalide");
        if (nom == null || nom.isBlank()) throw new IllegalArgumentException("nom vide");

        this.id = id;
        this.nom = nom;
        this.statut = StatutMembre.ACTIF;
    }

    /**
     * @return true si le membre est actif, false sinon
     */
    public boolean estActif() {
        return statut == StatutMembre.ACTIF;
    }

    /**
     * Rend le membre actif.
     * Méthode idempotente.
     */
    public void activer() {
        statut = StatutMembre.ACTIF;
    }

    /**
     * Rend le membre inactif.
     * Méthode idempotente.
     */
    public void desactiver() {
        statut = StatutMembre.INACTIF;
    }

    /**
     * @return l'identifiant du membre
     */
    public long getId() {
        return id;
    }

    /**
     * @return le nom du membre
     */
    public String getNom() {
        return nom;
    }

    /**
     * Représentation lisible du membre.
     *
     * @return chaîne contenant id, nom et statut
     */
    @Override
    public String toString() {
        return "Membre{id=" + id + ", nom='" + nom + "', statut=" + statut + "}";
    }

    /**
     * Deux membres sont considérés égaux si leurs identifiants sont identiques.
     *
     * @param o l'objet à comparer
     * @return true si id identique
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Membre)) return false;
        Membre membre = (Membre) o;
        return id == membre.id;
    }

    /**
     * Le hashCode est basé uniquement sur l'identifiant.
     *
     * @return hashCode basé sur id
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}

