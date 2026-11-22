package domaine;

import java.util.Objects;

public class Livre {
    private String titre;
    private String auteur;
    private String isbn;
    private int exemplairesTotal = 10;
    private int exemplairesDisponibles = 5;

    public Livre(String titre, String auteur, String isbn) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
    }

    public boolean estDisponible() {
        return exemplairesDisponibles > 0;
    }
    public void emprunterUnExemplaire() {
        if (estDisponible()) {
            exemplairesDisponibles--;
        }
    }

    public void rendreUnExemplaire() {
        if (exemplairesDisponibles < exemplairesTotal) {
            exemplairesDisponibles++;
        }
        else {
            System.out.println("Exemplaires au complet");
        }
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

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.length() != 11) {
            System.out.println("ISBN incorrect");
        }
        this.isbn = isbn;
    }

    public int getExemplairesTotal() {
        return exemplairesTotal;
    }

    public void setExemplairesTotal(int exemplairesTotal) {
        if  (exemplairesTotal < 0) {
            System.out.println("Nombre d'exemplaire doit être supérieur ou égale à 0");
        }
        this.exemplairesTotal = exemplairesTotal;
    }

    public int getExemplairesDisponibles() {
        return exemplairesDisponibles;
    }

    public void setExemplairesDisponibles(int exemplairesDisponibles) {
        if (exemplairesDisponibles < exemplairesTotal && exemplairesDisponibles >= 0) {
            this.exemplairesDisponibles = exemplairesDisponibles;
        }
        else {
            System.out.println("Impossible");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(titre, auteur, isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Livre livre = (Livre) o;
        return Objects.equals(titre, livre.titre) && Objects.equals(auteur, livre.auteur) && Objects.equals(isbn, livre.isbn);
    }
}
