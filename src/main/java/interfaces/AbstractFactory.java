package interfaces;

import model.Element;

public interface AbstractFactory {
    Element createElement(String name, Object... args); // Object... para diferentes parámetros
}
