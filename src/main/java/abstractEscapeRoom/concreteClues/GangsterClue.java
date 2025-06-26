package abstractEscapeRoom.concreteClues;

import interfaces.AbstractClue;

public class GangsterClue  implements AbstractClue {
    private String name;
    private String description;

    public GangsterClue(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public void getClue() {
        System.out.println("Space Clue: " + name + " - " + description);
    }


}
