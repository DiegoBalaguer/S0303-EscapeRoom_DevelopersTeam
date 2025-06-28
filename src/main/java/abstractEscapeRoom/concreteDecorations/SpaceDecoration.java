package abstractEscapeRoom.concreteDecorations;

import interfaces.AbstractDecoration;

public class SpaceDecoration implements AbstractDecoration {
    private String name;
    private String material;

    public SpaceDecoration(String name, String material) {
        this.name = name;
        this.material = material;
    }

    @Override
    public void getDecoration() {
        System.out.println("Space Decoration: " + name + " made of " + material);
    }


}
