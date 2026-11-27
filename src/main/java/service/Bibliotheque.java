package service;

import depot.Depot;
import domaine.Emprunt;
import domaine.Livre;
import domaine.Membre;
import domaine.ReglesBibliotheque;
import exception.*;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class Bibliotheque {
    private final Depot<Livre> depotLivres;
    private final Depot<Membre> depotMembres;
    private final Depot<Emprunt> depotEmprunts;

    public Bibliotheque(Depot<Livre> depotLivres, Depot<Membre> depotMembres, Depot<Emprunt> depotEmprunts) {
        this.depotLivres = depotLivres;
        this.depotMembres = depotMembres;
        this.depotEmprunts = depotEmprunts;
    }
    /** Ajoute un livre au catalogue. Refuse un ISBN déjà présent (règle R9). */
    public Livre ajouterLivre(String titre, String auteur, String isbn, int exemplaires) {
        Livre nouveau = new Livre(titre, auteur, isbn, exemplaires);
        boolean existeDeja = depotLivres.listerTous().contains(nouveau);
        if (existeDeja) {
            throw new DoublonException("Un livre avec l'ISBN " + nouveau.getIsbn() + " existe déjà");
        }
        return depotLivres.enregistrer(nouveau);
    }

    /** Renvoie le livre ou lève LivreIntrouvableException. */
    public Livre trouverLivre(Long id) {
        return depotLivres.trouverParId(id).orElseThrow(() -> new LivreIntrouvableException(id));
    }
    /** Renvoie tous les livres */
    public List<Livre> listerLivres() {
        return depotLivres.listerTous();
    }

    /** Inscrit un membre à la date du jour. */
    public Membre inscrireMembre(String nom, String email) {
        return inscrireMembre(nom, email, LocalDate.now());
    }

    /** Inscrit un membre à une date donnée. Refuse un email déjà utilisé. */
    public Membre inscrireMembre(String nom, String email, LocalDate dateInscription) {
        Membre nouveau = new Membre(nom, email, dateInscription); // valide les champs
        boolean existeDeja = depotMembres.listerTous().contains(nouveau);
        if (existeDeja) {
            throw new DoublonException("Un membre avec l'email " + nouveau.getEmail() + " existe déjà");
        }
        return depotMembres.enregistrer(nouveau);
    }

    public Membre trouverMembre(Long id) {
        return depotMembres.trouverParId(id).orElseThrow(() -> new MembreIntrouvableException(id));
    }

    public List<Membre> listerMembres() {
        return depotMembres.listerTous();
    }

    /** Tous les emprunts non rendus. */
    public List<Emprunt> empruntsEnCours() {
        return depotEmprunts.listerTous().stream().filter(Emprunt::estEnCours).toList();
    }

    public List<Emprunt> empruntsEnCoursDuMembre(Long idMembre) {
        trouverMembre(idMembre);
        return empruntsEnCours().stream()
                .filter(e -> e.getMembre().getId().equals(idMembre))
                .toList();
    }

    /**
     * Fait emprunter un livre à un membre.
     * Ordre des vérifications : membre existant, livre existant, livre disponible, limite du membre.
     */
    public Emprunt emprunter(Long idMembre, Long idLivre, LocalDate aujourdhui) {
        Membre membre = trouverMembre(idMembre);
        Livre livre = trouverLivre(idLivre);

        if (!livre.estDisponible()) {
            throw new LivreIndisponibleException(livre.getTitre());
        }
        if (empruntsEnCoursDuMembre(idMembre).size() >= ReglesBibliotheque.MAX_EMPRUNTS_PAR_MEMBRE) {
            throw new LimiteEmpruntsAtteinteException(membre.getNom(), ReglesBibliotheque.MAX_EMPRUNTS_PAR_MEMBRE);
        }
        livre.emprunterUnExemplaire();

        Emprunt emprunt = new Emprunt(livre, membre, aujourdhui);
        depotLivres.enregistrer(livre);
        depotEmprunts.enregistrer(emprunt);

        return emprunt;
    }

    public Emprunt trouverEmprunt(Long idEmprunt) {
        return depotEmprunts.trouverParId(idEmprunt).orElseThrow(() ->
                new EmpruntIntrouvableException(idEmprunt));
    }

    /** Enregistre le retour d'un livre. Refuse un emprunt déjà rendu. */
    public Emprunt rendre(Long idEmprunt, LocalDate aujourdhui) {
        Emprunt emprunt = trouverEmprunt(idEmprunt);
        emprunt.marquerCommeRendu(aujourdhui);
        emprunt.getLivre().rendreUnExemplaire();
        depotLivres.enregistrer(emprunt.getLivre());
        depotEmprunts.enregistrer(emprunt);
        return emprunt;
    }

    /** Les emprunts en retard à la date donnée. */
    public List<Emprunt> empruntsEnRetard(LocalDate aujourdhui) {
        return depotEmprunts.listerTous().stream().filter(e -> e.estEnRetard(aujourdhui)).toList();
    }

    /** Tous les emprunts d'un membre, rendus ou non. */
    public List<Emprunt> historiqueDuMembre(Long idMembre) {
        trouverMembre(idMembre);
        return depotEmprunts.listerTous().stream().filter(e -> e.getMembre().getId().equals(idMembre)).toList();
    }


}