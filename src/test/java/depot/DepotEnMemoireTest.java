package depot;

import domaine.Livre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepotEnMemoireTest {

    private DepotEnMemoire<Livre> depot;

    @BeforeEach
    void preparer() {
        depot = new DepotEnMemoire<>();
    }

    @Test
    void enregistrerAttribueDesIdentifiantsSuccessifs() {
        Livre a = depot.enregistrer(new Livre("A", "Auteur", "111", 1));
        Livre b = depot.enregistrer(new Livre("B", "Auteur", "222", 1));

        assertEquals(1L, a.getId());
        assertEquals(2L, b.getId());
    }

    @Test
    void trouverParIdRenvoieLObjetEnregistreOuUnOptionalVide() {
        Livre a = depot.enregistrer(new Livre("A", "Auteur", "111", 1));

        assertEquals(a, depot.trouverParId(a.getId()).orElseThrow());
        assertTrue(depot.trouverParId(99L).isEmpty());
    }

    @Test
    void supprimerRetireLObjetDeLaListe() {
        Livre a = depot.enregistrer(new Livre("A", "Auteur", "111", 1));
        depot.enregistrer(new Livre("B", "Auteur", "222", 1));

        depot.supprimer(a.getId());

        assertEquals(1, depot.listerTous().size());
    }
}
