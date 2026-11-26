package service;

import depot.DepotEnMemoire;
import domaine.Emprunt;
import domaine.Livre;
import domaine.Membre;
import domaine.ReglesBibliotheque;
import exception.DoublonException;
import exception.LivreIndisponibleException;
import exception.LivreIntrouvableException;
import exception.MembreIntrouvableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BibliothequeTest {
    private static final LocalDate AUJOURDHUI = LocalDate.of(2026, 11, 23);

    private Bibliotheque bibliotheque;
    private Membre lamine;
    private Livre petitPrince;

    @BeforeEach
    void preparer() {
        bibliotheque = new Bibliotheque(new DepotEnMemoire<Livre>(), new DepotEnMemoire<Membre>(), new DepotEnMemoire<Emprunt>());
        lamine = bibliotheque.inscrireMembre("Lamine", "lamine@example.com", LocalDate.now());
        petitPrince = bibliotheque.ajouterLivre("Le Petit Prince", "Antoine de Saint-Exupéry", "9782070612758", 1);
    }

    @Test
    void ajouterUnLivreAvecUnIsbnDejaPresentEstRefuse() {
        assertThrows(DoublonException.class,
                () -> bibliotheque.ajouterLivre("Autre titre", "Autre auteur", "9782070612758", 3));
        assertEquals(1, bibliotheque.listerLivres().size());
    }

    @Test
    void inscrireUnMembreAvecUnEmailDejaPresentEstRefuse() {
        assertThrows(DoublonException.class,
                () -> bibliotheque.inscrireMembre("Autre nom", "LAMINE@example.com"));
        assertEquals(1, bibliotheque.listerMembres().size());
    }

    @Test
    void trouverUnLivreInconnuLeveLivreIntrouvableException() {
        assertThrows(LivreIntrouvableException.class, () -> bibliotheque.trouverLivre(3L));
    }

    @Test
    void trouverUnMembreInconnuLeveMembreIntrouvableException() {
        assertThrows(MembreIntrouvableException.class, () -> bibliotheque.trouverMembre(999L));
    }
}