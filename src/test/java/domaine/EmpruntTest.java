package domaine;

import exception.EmpruntDejaRenduException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmpruntTest {
    private static final LocalDate DEBUT = LocalDate.of(2026, 11, 23);

    private Emprunt emprunt;
    private LocalDate echeance;

    @BeforeEach
    void preparer() {
        Livre livre = new Livre("Le Petit Prince", "Saint-Exupéry", "9782070612758", 1);
        Membre membre = new Membre("Lamine", "lamine@example.com", DEBUT);
        emprunt = new Emprunt(livre, membre, DEBUT);
        echeance = emprunt.getDateRetourPrevue();
    }

    @Test
    void laDateDeRetourPrevueEstLaDateDEmpruntPlusLaDureeReglementaire() {
        assertEquals(DEBUT.plusDays(ReglesBibliotheque.DUREE_EMPRUNT_JOURS), emprunt.getDateRetourPrevue());
    }

    @Test
    void unNouvelEmpruntEstEnCours() {
        assertTrue(emprunt.estEnCours());
        assertNull(emprunt.getDateRetourEffective());
    }

    @Test
    void laVeilleDeLEcheanceOnNEstPasEnRetard() {
        assertFalse(emprunt.estEnRetard(echeance.minusDays(1)));
    }

    @Test
    void leJourDeLEcheanceOnNEstPasEnRetard() {
        assertFalse(emprunt.estEnRetard(echeance));
    }

    @Test
    void leLendemainDeLEcheanceOnEstEnRetard() {
        assertTrue(emprunt.estEnRetard(echeance.plusDays(1)));
    }

    @Test
    void unEmpruntRenduNEstJamaisEnRetard() {
        emprunt.marquerCommeRendu(echeance.plusDays(5));

        assertFalse(emprunt.estEnRetard(echeance.plusDays(10)));
    }

    @Test
    void joursDeRetardCompteLesJoursApresLEcheance() {
        assertEquals(3, emprunt.joursDeRetard(echeance.plusDays(3)));
    }

    @Test
    void joursDeRetardVautZeroQuandOnNEstPasEnRetard() {
        assertEquals(0, emprunt.joursDeRetard(echeance));
    }

    @Test
    void marquerCommeRenduEnregistreLaDateEtTermineLEmprunt() {
        LocalDate retour = DEBUT.plusDays(5);

        emprunt.marquerCommeRendu(retour);

        assertFalse(emprunt.estEnCours());
        assertEquals(retour, emprunt.getDateRetourEffective());
    }

    @Test
    void rendreDeuxFoisLeMemeEmpruntLeveEmpruntDejaRenduException() {
        emprunt.marquerCommeRendu(DEBUT.plusDays(5));

        assertThrows(EmpruntDejaRenduException.class, () -> emprunt.marquerCommeRendu(DEBUT.plusDays(6)));
    }

    @Test
    void onNePeutPasRendreUnLivreAvantDeLAvoirEmprunte() {
        assertThrows(IllegalArgumentException.class, () -> emprunt.marquerCommeRendu(DEBUT.minusDays(1)));
    }


}

