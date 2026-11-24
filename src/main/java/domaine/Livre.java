package domaine;

import java.util.Objects;
import exception.LivreIndisponibleException;

public class Livre implements AvecIdentifiant {
    private Long id;
    private String titre;
    private String auteur;
    private final String isbn;
    private final int exemplairesTotal;
    private int exemplairesDisponibles;

    public Livre(String titre, String auteur, String isbn, int exemplairesTotal) {
        this.titre = Verifications.exigerNonVide(titre, "titre");
        this.auteur = Verifications.exigerNonVide(auteur, "auteur");
        this.isbn = Verifications.exigerNonVide(isbn, "isbn");
        if (exemplairesTotal < 1) {
            throw new IllegalArgumentException("Un livre doit avoir au moins 1 exemplaire");
        }
        this.exemplairesTotal = exemplairesTotal;
        this.exemplairesDisponibles = exemplairesTotal; // au départ, tout est en rayon
    }

    public boolean estDisponible() {
        return exemplairesDisponibles > 0;
    }

    public void emprunterUnExemplaire() {
        if (!estDisponible()) {
            throw new LivreIndisponibleException(titre);
        }
        exemplairesDisponibles--;
    }

    public void rendreUnExemplaire() {
        if (exemplairesDisponibles >= exemplairesTotal) {
            throw new IllegalStateException(
                    "Tous les exemplaires de « " + titre + " » sont déjà en rayon");
        }
        exemplairesDisponibles++;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getIsbn() {
        return isbn;
    }


    public int getExemplairesTotal() {
        return exemplairesTotal;
    }


    public int getExemplairesDisponibles() {
        return exemplairesDisponibles;
    }


    /** Deux livres sont les mêmes s'ils ont le même ISBN (identité « naturelle » du livre). */
    @Override
    public boolean equals(Object autre) {
        if (this == autre) {
            return true;
        }
        if (!(autre instanceof Livre)) {
            return false;
        }
        return isbn.equals(((Livre) autre).isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Livre{id=" + id + ", titre='" + titre + "', auteur='" + auteur + "', isbn='" + isbn
                + "', disponibles=" + exemplairesDisponibles + "/" + exemplairesTotal + "}";
    }
}