package abstractEscapeRoom.concreteDecorations;

import interfaces.AbstractDecoration;

public class GangsterDecoration implements AbstractDecoration {
    private String name;
    private String material;

    public GangsterDecoration(String name, String material) {
        this.name = name;
        this.material = material;
    }

    @Override
    public void getDecoration() {
        System.out.println("Gangster Decoration: " + name + " made of " + material);

    }
}