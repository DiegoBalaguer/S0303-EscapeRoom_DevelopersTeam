package wrapperCrud;

import java.util.ArrayList;
import java.util.List;

public class ElementListWrapper<T> {
    private final List<T> list;

    public ElementListWrapper(List<T> list) {
        this.list = list;
    }

    public synchronized void add(T element) {
        list.add(element);
    }

    public synchronized void remove(T element) {
        list.remove(element);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    public synchronized List<T> getAll() {
        return new ArrayList<>(list); // Devuelve una copia de la lista para evitar modificaciones externas
    }

    public synchronized int size() {
        return list.size();
    }

    @Override
    public synchronized String toString() {
        return list.toString();
    }
}