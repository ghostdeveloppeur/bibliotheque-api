package domaine;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprunt {
    private Livre livre;
    private Membre membre;
    private final LocalDate dateEmprunt;
    private final LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective = null;

    public Emprunt(Livre livre, Membre membre) {
        this.livre = livre;
        this.membre = membre;
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = this.dateEmprunt.plusDays(ReglesBibliotheque.DUREE_EMPRUNT_JOURS);
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
