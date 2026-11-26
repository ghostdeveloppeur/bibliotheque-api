package service;

import depot.Depot;
import domaine.Emprunt;
import domaine.Livre;
import domaine.Membre;
import exception.DoublonException;
import exception.LivreIntrouvableException;
import exception.MembreIntrouvableException;

import java.lang.reflect.Member;
import java.time.LocalDate;
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
}
