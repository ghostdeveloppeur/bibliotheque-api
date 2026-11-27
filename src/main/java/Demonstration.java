import depot.DepotEnMemoire;
import domaine.Emprunt;
import domaine.Livre;
import domaine.Membre;
import exception.BibliothequeException;
import service.Bibliotheque;

import java.time.LocalDate;

/** Petit scénario pour voir la bibliothèque fonctionner en console (phase 1, sans Spring). */
public class Demonstration {

    public static void main(String[] args) {
        Bibliotheque bibliotheque = new Bibliotheque(
                new DepotEnMemoire<>(), new DepotEnMemoire<>(), new DepotEnMemoire<>());

        LocalDate debut = LocalDate.of(2026, 11, 23);

        System.out.println("=== Catalogue et inscriptions ===");
        Livre petitPrince = bibliotheque.ajouterLivre("Le Petit Prince", "Antoine de Saint-Exupéry", "9782070612758", 1);
        Livre stranger = bibliotheque.ajouterLivre("L'Étranger", "Albert Camus", "9782070360024", 2);
        Membre lamine = bibliotheque.inscrireMembre("Lamine", "lamine@example.com", debut);
        Membre sara = bibliotheque.inscrireMembre("Sara", "sara@example.com", debut);
        bibliotheque.listerLivres().forEach(System.out::println);
        bibliotheque.listerMembres().forEach(System.out::println);

        System.out.println("\n=== Emprunts ===");
        Emprunt e1 = bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), debut);
        System.out.println(e1);

        System.out.println("\n=== Erreurs métier attendues ===");
        essayer(() -> bibliotheque.emprunter(sara.getId(), petitPrince.getId(), debut));      // plus d'exemplaire
        essayer(() -> bibliotheque.emprunter(99L, stranger.getId(), debut));                   // membre inconnu
        essayer(() -> bibliotheque.ajouterLivre("Autre titre", "Autre auteur", "9782070612758", 1)); // ISBN en double

        System.out.println("\n=== Retard ===");
        LocalDate dansDixSeptJours = debut.plusDays(17);
        bibliotheque.empruntsEnRetard(dansDixSeptJours)
                .forEach(e -> System.out.println(e + " -> " + e.joursDeRetard(dansDixSeptJours) + " jour(s) de retard"));

        System.out.println("\n=== Retour ===");
        System.out.println(bibliotheque.rendre(e1.getId(), dansDixSeptJours));
        System.out.println("Disponibles : " + petitPrince.getExemplairesDisponibles() + "/" + petitPrince.getExemplairesTotal());
        essayer(() -> bibliotheque.rendre(e1.getId(), dansDixSeptJours)); // déjà rendu
    }

    private static void essayer(Runnable action) {
        try {
            action.run();
            System.out.println("(aucune erreur)");
        } catch (BibliothequeException e) {
            System.out.println("Erreur métier : " + e.getMessage());
        }
    }
}
