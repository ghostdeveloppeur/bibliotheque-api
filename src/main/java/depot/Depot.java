package depot;

import domaine.AvecIdentifiant;

import java.util.List;
import java.util.Optional;

public interface Depot<T extends AvecIdentifiant> {

    /** Enregistre l'objet. S'il n'a pas encore d'identifiant, le dépôt lui en attribue un. */
    T enregistrer(T element);

    /** Renvoie l'objet s'il existe, sinon un Optional vide. */
    Optional<T> trouverParId(Long id);

    /** Renvoie tous les objets, dans l'ordre d'enregistrement. */
    List<T> listerTous();

    /** Supprime l'objet (ne fait rien s'il n'existe pas). */
    void supprimer(Long id);
}
