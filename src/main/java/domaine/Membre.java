package domaine;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * Une personne inscrite à la bibliothèque.
 *
 * La classe reste volontairement simple : le nombre d'emprunts en cours d'un membre n'est PAS stocké ici.
 * Il se calcule à partir des emprunts (voir Bibliotheque), pour qu'il ne puisse jamais être faux.
 */
public class Membre implements AvecIdentifiant {

    private Long id;
    private String nom;
    private final String email;
    private final LocalDate dateInscription;

    public Membre(String nom, String email, LocalDate dateInscription) {
        this.nom = Verifications.exigerNonVide(nom, "nom");
        this.email = verifierEmail(email);
        this.dateInscription = Verifications.exigerPresent(dateInscription, "dateInscription");
    }

    /** Validation volontairement simple : un « @ » ni en première ni en dernière position. */
    private static String verifierEmail(String email) {
        String nettoye = Verifications.exigerNonVide(email, "email");
        int arobase = nettoye.indexOf('@');
        if (arobase < 1 || arobase == nettoye.length() - 1) {
            throw new IllegalArgumentException("Email invalide : " + email);
        }
        return nettoye;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = Verifications.exigerNonVide(nom, "nom");
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    /** Deux membres sont les mêmes s'ils ont le même email (sans tenir compte des majuscules). */
    @Override
    public boolean equals(Object autre) {
        if (this == autre) {
            return true;
        }
        if (!(autre instanceof Membre)) {
            return false;
        }
        return email.equalsIgnoreCase(((Membre) autre).email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email.toLowerCase(Locale.ROOT));
    }

    @Override
    public String toString() {
        return "Membre{id=" + id + ", nom='" + nom + "', email='" + email + "'}";
    }
}
