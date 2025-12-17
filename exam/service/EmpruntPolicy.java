package exam.service;

/**
 * Politique d'emprunt de la médiathèque.
 * <p>
 * Définit les règles métier concernant les emprunts,
 * comme le quota maximum d'emprunts actifs par membre.
 */
public final class EmpruntPolicy {

    /**
     * Quota maximum d'emprunts actifs par membre.
     */
    private static final int QUOTA_MAX = 3;

    /**
     * Vérifie si un membre peut emprunter une nouvelle œuvre
     * en fonction de son nombre d'emprunts actifs.
     *
     * @param nombreEmpruntsActifs le nombre d'emprunts actifs du membre
     * @return true si le membre peut emprunter, false sinon
     */
    public static boolean peutEmprunter(int nombreEmpruntsActifs) {
        return nombreEmpruntsActifs < QUOTA_MAX;
    }

    /**
     * Retourne le quota maximum d'emprunts actifs.
     *
     * @return le quota maximum
     */
    public static int getQuotaMax() {
        return QUOTA_MAX;
    }
}
