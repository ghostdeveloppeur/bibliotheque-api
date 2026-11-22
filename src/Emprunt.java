import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprunt {
    Livre livre;
    Membre membre;
    final LocalDate dateEmprunt;
    final LocalDate dateRetourPrevue;
    LocalDate dateRetourEffective = null;

    public Emprunt(Livre livre, Membre membre) {
        this.livre = livre;
        this.membre = membre;
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = this.dateEmprunt.plusDays(14);
    }

    public boolean estEnCours() {
        return dateRetourEffective != null;
    }

    public boolean estEnRetard(LocalDate aujourdhui) {
        if (estEnCours()) {
            if (aujourdhui.isAfter(dateRetourPrevue)) {
                return true;
            }
        }
        return false;
    }

    public void marquerCommeRendu(LocalDate date) {
        if(estEnCours()) {
            this.dateRetourEffective = date;
        }
        System.out.println("Déjà rendu");
    }

    public long joursDeRetard(LocalDate aujourdhui) {
        if (estEnRetard(aujourdhui)) {
            return ChronoUnit.DAYS.between(this.dateRetourPrevue, aujourdhui);
        }
        return 0;
    }
}
