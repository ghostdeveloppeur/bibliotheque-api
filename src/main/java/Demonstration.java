import domaine.Livre;
import domaine.Membre;

import java.time.LocalDate;

public class Demonstration {
    public static void main(String[] args) {
        Livre petitPrince = new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", "9782070612758", 1);
        Livre stranger = new Livre("L'Étranger", "Albert Camus", "9782070360024", 2);
        Membre lamine = new Membre("Lamine", "lamine@example.com", LocalDate.now());
        Membre sara = new Membre("Sara", "sara@example.com", LocalDate.now());

        Livre miserables = new Livre("Les Misérables","Victor Hugo", "9782081473256",2);
        System.out.println(petitPrince.toString());
        System.out.println(stranger.toString());
        System.out.println(lamine.toString());
        System.out.println(sara.toString());
    }
}