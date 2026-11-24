package domaine;

/** Petites vérifications communes aux classes du domaine (visibles uniquement dans ce package). */
final class Verifications {

    private Verifications() {
    }

    /** Renvoie le texte nettoyé (sans espaces autour), ou refuse s'il est null ou vide. */
    static String exigerNonVide(String valeur, String nomChamp) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le champ « " + nomChamp + " » est obligatoire");
        }
        return valeur.trim();
    }

    /** Refuse un objet null. */
    static <T> T exigerPresent(T valeur, String nomChamp) {
        if (valeur == null) {
            throw new IllegalArgumentException("Le champ « " + nomChamp + " » est obligatoire");
        }
        return valeur;
    }
}
