package exam.domain.oeuvre;

/**
 * Représente un livre dans la médiathèque.
 * <p>
 * Un livre possède un auteur et un ISBN en plus des attributs hérités d'Oeuvre.
 * L'auteur ne peut pas être null ou vide, et l'ISBN doit être strictement positif.
 */
public final class Livre extends Oeuvre {

    private final String auteur;
    private final int isbn;

    /**
     * Crée un nouveau livre.
     *
     * @param id     identifiant unique > 0
     * @param titre  titre non null et non vide
     * @param auteur auteur non null et non vide
     * @param isbn   numéro ISBN strictement positif
     * @throws IllegalArgumentException si id <= 0, titre null/blank, auteur null/blank, ou isbn <= 0
     */
    public Livre(long id, String titre, String auteur, int isbn) {
        super(id, titre);

        if (auteur == null || auteur.isBlank())
            throw new IllegalArgumentException("L'auteur ne peut pas être vide");
        this.auteur = auteur;

        if (isbn <= 0)
            throw new IllegalArgumentException("L'ISBN doit être strictement positif");
        this.isbn = isbn;
    }

    /**
     * @return le nom de l'auteur
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * @return le numéro ISBN
     */
    public int getIsbn() {
        return isbn;
    }

    /**
     * Représentation lisible du livre.
     *
     * @return chaîne contenant type, id, titre, auteur et ISBN
     */
    @Override
    public String toString() {
        return super.toString() + ", auteur='" + auteur + "', isbn=" + isbn;
    }
}
