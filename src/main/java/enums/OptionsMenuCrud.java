package enums;

public enum OptionsMenuCrud {
    ADD ("Create"),
    SHOW("Show"),
    REMOVE("Delete"),
    UPDATE("Update information"),
    CALCULATE("Calculate item budget"),
    RETURN("Return to main menu");

    private final String DESCRIPTION;


    OptionsMenuCrud(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public static void viewMenuCrud(String title) {
        System.out.println(System.lineSeparator() + title + System.lineSeparator());
        for (OptionsMenuCrud optionMenu : OptionsMenuCrud.values()) {
            System.out.println(optionMenu.ordinal() + 1 + ". " + optionMenu.getDESCRIPTION());
        }
        System.out.println(System.lineSeparator() + "Choose an option: ");
    }
}
