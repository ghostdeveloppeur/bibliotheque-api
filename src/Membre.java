import java.time.LocalDate;
import java.util.Objects;

public class Membre {
    String nom;
    String prenom;
    final LocalDate dateInscription;

    public Membre(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateInscription = LocalDate.now();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Membre membre = (Membre) o;
        return Objects.equals(nom, membre.nom) && Objects.equals(prenom, membre.prenom) && Objects.equals(dateInscription, membre.dateInscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, dateInscription);
    }
}

