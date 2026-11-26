package domaine;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import exception.EmpruntDejaRenduException;

public class Emprunt implements AvecIdentifiant {
    private Long id;
    final private Livre livre;
    final private Membre membre;
    private final LocalDate dateEmprunt;
    private final LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective = null;

    public Emprunt(Livre livre, Membre membre, LocalDate dateEmprunt) {
        this.livre = Verifications.exigerPresent(livre,"livre");
        this.membre = Verifications.exigerPresent(membre,"membre");
        this.dateEmprunt = Verifications.exigerPresent(dateEmprunt,"dateEmprunt");
        this.dateRetourPrevue = this.dateEmprunt.plusDays(ReglesBibliotheque.DUREE_EMPRUNT_JOURS);
    }

    public boolean estEnCours() {
        return dateRetourEffective == null;
    }

    public boolean estEnRetard(LocalDate aujourdhui) {
        return estEnCours() && aujourdhui.isAfter(dateRetourPrevue);
    }

    public void marquerCommeRendu(LocalDate dateRetour) {
        Verifications.exigerPresent(dateRetour, "dateRetour");
        if (!estEnCours()) {
            throw new EmpruntDejaRenduException(livre.getTitre(), membre.getNom());
        }
        if (dateRetour.isBefore(dateEmprunt)) {
            throw new IllegalArgumentException("La date de retour ne peut pas précéder la date d'emprunt");
        }
        this.dateRetourEffective = dateRetour;
    }

    public long joursDeRetard(LocalDate aujourdhui) {
        if (estEnRetard(aujourdhui)) {
            return ChronoUnit.DAYS.between(this.dateRetourPrevue, aujourdhui);
        }
        return 0;
    }

    @Override
    public Long getId() {
        return this.id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Livre getLivre() {
        return livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    @Override
    public String toString() {
        return "Emprunt{id=" + id + ", livre='" + livre.getTitre() + "', membre='" + membre.getNom()
                + "', emprunté le " + dateEmprunt + ", retour prévu le " + dateRetourPrevue
                + (estEnCours() ? ", en cours" : ", rendu le " + dateRetourEffective) + "}";
    }
}
