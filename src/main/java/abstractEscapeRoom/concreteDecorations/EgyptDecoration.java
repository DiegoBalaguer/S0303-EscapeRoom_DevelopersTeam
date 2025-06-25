package abstractEscapeRoom.concreteDecorations;

import interfaces.AbstractDecoration;

public class EgyptDecoration implements AbstractDecoration {
    private String name;
    private String material;

    public EgyptDecoration(String name, String material) {
        this.name = name;
        this.material = material;
    }

    @Override
    public void getDecoration() {
        System.out.println("Egyptian Decoration: " + name + " made of " + material);
    }
}