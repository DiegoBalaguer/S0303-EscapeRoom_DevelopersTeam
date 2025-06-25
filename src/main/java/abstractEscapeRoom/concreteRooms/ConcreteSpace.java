package abstractEscapeRoom.concreteRooms;

import interfaces.AbstractFactory;
import model.Element;

public class ConcreteSpace implements AbstractFactory {
    @Override
    public Element createElement(String name, Object... args) {
        return null;
    }
}
