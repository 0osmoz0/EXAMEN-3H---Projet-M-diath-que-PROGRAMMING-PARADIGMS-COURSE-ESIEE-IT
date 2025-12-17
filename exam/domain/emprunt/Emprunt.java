package exam.domain.emprunt;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Représente un emprunt d'une œuvre par un membre.
 * <p>
 * Un emprunt lie un membre et une œuvre à une date d'emprunt.
 * La date de retour est initialement null et ne peut être fixée qu'une seule fois.
 */
public final class Emprunt {

    private final long id;
    private final long idMembre;
    private final long idOeuvre;
    private final LocalDate dateEmprunt;
    private LocalDate dateRetour;

    /**
     * Crée un nouvel emprunt actif.
     *
     * @param id         identifiant de l'emprunt > 0
     * @param idMembre   identifiant du membre > 0
     * @param idOeuvre   identifiant de l'œuvre > 0
     * @param dateEmprunt date de l'emprunt non null
     * @throws IllegalArgumentException si un identifiant <= 0
     * @throws NullPointerException     si dateEmprunt est null
     */
    public Emprunt(long id, long idMembre, long idOeuvre, LocalDate dateEmprunt) {
        if (id <= 0 || idMembre <= 0 || idOeuvre <= 0)
            throw new IllegalArgumentException("Les identifiants doivent être strictement positifs");
        if (dateEmprunt == null)
            throw new NullPointerException("La date d'emprunt ne peut pas être null");

        this.id = id;
        this.idMembre = idMembre;
        this.idOeuvre = idOeuvre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = null; // emprunt actif au départ
    }

    public long getId() {
        return id;
    }

    public long getIdMembre() {
        return idMembre;
    }

    public long getIdOeuvre() {
        return idOeuvre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    /**
     * @return true si l'emprunt est actif (non retourné)
     */
    public boolean estActif() {
        return dateRetour == null;
    }

    /**
     * Marque l'emprunt comme retourné.
     * <p>
     * La date de retour ne peut être fixée qu'une seule fois et doit être
     * non null et >= date d'emprunt.
     *
     * @param date date de retour
     * @throws IllegalStateException    si l'emprunt a déjà été retourné
     * @throws IllegalArgumentException si date null ou antérieure à la date d'emprunt
     */
    public void marquerRetour(LocalDate date) {
        if (dateRetour != null)
            throw new IllegalStateException("Retour déjà effectué");
        if (date == null)
            throw new IllegalArgumentException("La date de retour ne peut pas être null");
        if (date.isBefore(dateEmprunt))
            throw new IllegalArgumentException("La date de retour ne peut pas être antérieure à la date d'emprunt");

        this.dateRetour = date;
    }

    /**
     * Représentation lisible de l'emprunt.
     *
     * @return chaîne contenant id, membre, œuvre, dateEmprunt et dateRetour
     */
    @Override
    public String toString() {
        return "Emprunt{id=" + id + ", membre=" + idMembre + ", oeuvre=" + idOeuvre +
               ", dateEmprunt=" + dateEmprunt + ", dateRetour=" + dateRetour + "}";
    }

    /**
     * Deux emprunts sont égaux si leurs identifiants sont identiques.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emprunt)) return false;
        Emprunt emprunt = (Emprunt) o;
        return id == emprunt.id;
    }

    /**
     * Hashcode basé uniquement sur l'identifiant.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
