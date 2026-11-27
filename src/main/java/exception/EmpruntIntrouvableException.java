package exception;

public class EmpruntIntrouvableException extends BibliothequeException {
    public EmpruntIntrouvableException(Long id) {
        super("Emprunt introuvable (id = \" + id + \")");
    }
}
