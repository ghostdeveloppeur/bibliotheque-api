package exception;

public class LimiteEmpruntsAtteinteException extends BibliothequeException {
    public LimiteEmpruntsAtteinteException(String nomMembre, int maximum) {
        super(nomMembre + " a déjà " + maximum + " emprunts en cours (maximum autorisé)");
    }
}
