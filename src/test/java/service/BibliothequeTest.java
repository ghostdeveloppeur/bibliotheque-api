package service;

import depot.DepotEnMemoire;
import domaine.Emprunt;
import domaine.Livre;
import domaine.Membre;
import domaine.ReglesBibliotheque;
import exception.*;
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

    /**
     * Crée n livres distincts, avec un seul exemplaire chacun, et renvoie leurs identifiants.
     */
    private List<Long> creerLivres(int n) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ids.add(bibliotheque.ajouterLivre("Livre " + i, "Auteur", "isbn-" + i, 1).getId());
        }
        return ids;
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

    @Test
    void empruntReussiCreeUnEmpruntEnCoursEtReduitLeStock() {
        Emprunt emprunt = bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), AUJOURDHUI);

        assertTrue(emprunt.estEnCours());
        assertEquals(AUJOURDHUI.plusDays(ReglesBibliotheque.DUREE_EMPRUNT_JOURS), emprunt.getDateRetourPrevue());
        assertEquals(0, petitPrince.getExemplairesDisponibles());
        assertEquals(1, bibliotheque.empruntsEnCours().size());
    }

    @Test
    void emprunterUnLivreIndisponibleEstRefuse() {
        Membre sara = bibliotheque.inscrireMembre("Sara", "sara@example.com", AUJOURDHUI);
        bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), AUJOURDHUI); // dernier exemplaire

        assertThrows(LivreIndisponibleException.class, () -> bibliotheque.emprunter(sara.getId(), petitPrince.getId(), AUJOURDHUI));
    }

    @Test
    void leMembreNePeutPasDepasserLeNombreMaximumDEmpruntsEnCours() {
        int max = ReglesBibliotheque.MAX_EMPRUNTS_PAR_MEMBRE;
        List<Long> livres = creerLivres(max + 1);
        for (int i = 0; i < max; i++) {
            bibliotheque.emprunter(lamine.getId(), livres.get(i), AUJOURDHUI); // jusqu'au maximum : accepté
        }

        assertThrows(LimiteEmpruntsAtteinteException.class, () -> bibliotheque.emprunter(lamine.getId(), livres.get(max), null));
    }

    @Test
    void unEmpruntRefuseNeModifiePasLeStock() {
        int max = ReglesBibliotheque.MAX_EMPRUNTS_PAR_MEMBRE;
        List<Long> livres = creerLivres(max + 1);
        for (int i = 0; i < max; i++) {
            bibliotheque.emprunter(lamine.getId(), livres.get(i), AUJOURDHUI);
        }
        Livre dernier = bibliotheque.trouverLivre(livres.get(max));

        assertThrows(LimiteEmpruntsAtteinteException.class,
                () -> bibliotheque.emprunter(lamine.getId(), dernier.getId(), AUJOURDHUI));

        assertEquals(1, dernier.getExemplairesDisponibles()); // rien n'a été retiré du rayon
        assertEquals(max, bibliotheque.empruntsEnCours().size()); // aucun emprunt fantôme
    }

    @Test
    void emprunterAvecUnMembreInconnuLeveMembreIntrouvableException() {
        assertThrows(MembreIntrouvableException.class,
                () -> bibliotheque.emprunter(999L, petitPrince.getId(), AUJOURDHUI));
    }

    @Test
    void emprunterUnLivreInconnuLeveLivreIntrouvableException() {
        assertThrows(LivreIntrouvableException.class,
                () -> bibliotheque.emprunter(lamine.getId(), 999L, AUJOURDHUI));
    }

    @Test
    void rendreUnLivreLibereUnExemplaireEtTermineLEmprunt() {
        Emprunt emprunt = bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), AUJOURDHUI);

        bibliotheque.rendre(petitPrince.getId(), AUJOURDHUI.plusDays(3));

        assertEquals(1, petitPrince.getExemplairesDisponibles());
        assertTrue(bibliotheque.empruntsEnCours().isEmpty());
    }

    @Test
    void rendreLibereLaPlaceDansLaLimiteDuMembre() {
        int max = ReglesBibliotheque.MAX_EMPRUNTS_PAR_MEMBRE;
        List<Long> livres = creerLivres(max + 1);
        Emprunt premier = bibliotheque.emprunter(lamine.getId(), livres.get(0), AUJOURDHUI);
        for (int i = 1; i < max; i++) {
            bibliotheque.emprunter(lamine.getId(), livres.get(i), AUJOURDHUI);
        }

        bibliotheque.rendre(premier.getId(), AUJOURDHUI);

        // Le membre repasse sous la limite : un nouvel emprunt est de nouveau possible.
        bibliotheque.emprunter(lamine.getId(), livres.get(max), AUJOURDHUI);
        assertEquals(max, bibliotheque.empruntsEnCoursDuMembre(lamine.getId()).size());
    }

    @Test
    void rendreDeuxFoisLeMemeEmpruntEstRefuseEtNeRemetPasUnExemplaireDeTrop() {
        Emprunt emprunt = bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), AUJOURDHUI);
        bibliotheque.rendre(emprunt.getId(), AUJOURDHUI);

        assertThrows(EmpruntDejaRenduException.class, () -> bibliotheque.rendre(emprunt.getId(), AUJOURDHUI));
        assertEquals(1, petitPrince.getExemplairesDisponibles());
    }

    @Test
    void rendreUnEmpruntInconnuLeveEmpruntIntrouvableException() {
        assertThrows(EmpruntIntrouvableException.class, () -> bibliotheque.rendre(999L, AUJOURDHUI));
    }

    @Test
    void empruntsEnRetardNeContientQueLesEmpruntsEnCoursDontLEcheanceEstDepassee() {
        Livre autre = bibliotheque.ajouterLivre("L'Étranger", "Camus", "9782070360024", 1);
        Emprunt ancien = bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), AUJOURDHUI);
        Emprunt recent = bibliotheque.emprunter(lamine.getId(), autre.getId(), AUJOURDHUI.plusDays(10));

        LocalDate date = ancien.getDateRetourPrevue().plusDays(1);
        List<Emprunt> enRetard = bibliotheque.empruntsEnRetard(date);

        assertEquals(List.of(ancien), enRetard);
        assertFalse(enRetard.contains(recent));
    }

    @Test
    void empruntsEnRetardIgnoreLesEmpruntsDejaRendus() {
        Emprunt emprunt = bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), AUJOURDHUI);
        bibliotheque.rendre(emprunt.getId(), AUJOURDHUI.plusDays(1));

        assertTrue(bibliotheque.empruntsEnRetard(AUJOURDHUI.plusDays(100)).isEmpty());
    }

    @Test
    void historiqueDuMembreContientLesEmpruntsRendusEtEnCours() {
        Livre autre = bibliotheque.ajouterLivre("L'Étranger", "Camus", "9782070360024", 1);
        Emprunt rendu = bibliotheque.emprunter(lamine.getId(), petitPrince.getId(), AUJOURDHUI);
        bibliotheque.rendre(rendu.getId(), AUJOURDHUI);
        bibliotheque.emprunter(lamine.getId(), autre.getId(), AUJOURDHUI);

        assertEquals(2, bibliotheque.historiqueDuMembre(lamine.getId()).size());
        assertEquals(1, bibliotheque.empruntsEnCoursDuMembre(lamine.getId()).size());
    }

    @Test
    void historiqueDUnMembreInconnuLeveMembreIntrouvableException() {
        assertThrows(MembreIntrouvableException.class, () -> bibliotheque.historiqueDuMembre(999L));
    }
}