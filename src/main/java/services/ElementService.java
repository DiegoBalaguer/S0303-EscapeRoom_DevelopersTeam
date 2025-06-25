package services;

import model.Element;

import java.util.ArrayList;
import java.util.List;

// Gestión genérica para Elementos
public class ElementService<T extends Element> {
    private final List<T> elements = new ArrayList<>();

    public void create(T element) {
        elements.add(element);
    }

    public List<T> readAll() {
        return new ArrayList<>(elements);
    }

    public void update(int id, T updatedElement) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getId() == id) {
                elements.set(i, updatedElement);
            }
        }
    }

    public void delete(int id) {
        elements.removeIf(element -> element.getId() == id);
    }
}
