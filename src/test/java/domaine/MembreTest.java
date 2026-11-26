package domaine;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MembreTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 23);

    @Test
    void leConstructeurRefuseUnNomVide() {
        assertThrows(IllegalArgumentException.class, () -> new Membre("", "a@b.fr", DATE));
    }

    @Test
    void leConstructeurRefuseUnEmailSansArobase() {
        assertThrows(IllegalArgumentException.class, () -> new Membre("Lamine", "lamine.example.com", DATE));
    }

    @Test
    void leConstructeurRefuseUnEmailCommencantParArobase() {
        assertThrows(IllegalArgumentException.class, () -> new Membre("Lamine", "@lamine.example.com", DATE));
    }

    @Test
    void deuxMembresAvecLeMemeEmailSontEgauxQuelleQueSoitLaCasse() {
        Membre a = new Membre("Lamine", "lamine@example.com", DATE);
        Membre b = new Membre("L. Kourouma", "LAMINE@example.com", DATE);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void unMembreValideCreeSansErreur() {
        assertDoesNotThrow(() -> new Membre("Lamine", "lamine@example.com", DATE));
    }
}
