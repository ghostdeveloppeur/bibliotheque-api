package depot;

import domaine.AvecIdentifiant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DepotEnMemoire<T extends AvecIdentifiant> implements Depot<T>{
    private final Map<Long, T> elements = new LinkedHashMap<>();
    private long prochainId = 1;

    @Override
    public T enregistrer(T element) {
        if (element.getId() == null) {
            element.setId(prochainId++);
        }
        elements.put(element.getId(), element);
        return element;
    }

    @Override
    public Optional<T> trouverParId(Long id) {
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public List<T> listerTous() {
        return new ArrayList<>(elements.values()); // copie : l'appelant ne peut pas vider notre Map
    }

    @Override
    public void supprimer(Long id) {
        elements.remove(id);
    }
}
