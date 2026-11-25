package domaine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReglesBibliothequeTest {
    @Test
    @DisplayName("S'assure que les deux valeurs sont strictement positives")
    void lesReglesChiffreesSontStrictementPositives() {
        assertTrue(ReglesBibliotheque.DUREE_EMPRUNT_JOURS > 0);
        assertTrue(ReglesBibliotheque.MAX_EMPRUNT_PAR_MEMBRE > 0);
    }
}
