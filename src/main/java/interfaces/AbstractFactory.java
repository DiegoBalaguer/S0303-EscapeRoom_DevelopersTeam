package interfaces;

import mvc.model.Element;

public interface AbstractFactory {
    <T extends Element> T createElement(String type, Object... args);
}
