package mvc.enumsMenu;

public enum OptionsMenuItem {
    ADD(1, "Create"),
    SHOW(2, "Show one registre"),
    LIST_ALL(3, "List all registres"),
    LIST_BY_ROOM(4, "List by room"),
    DELETE(5, "Remove"),
    UPDATE(6, "Update information"),
    EXIT(0, "Return to main menu");

    private final String DESCRIPTION;
    private final int OPTION_NUMBER;


    OptionsMenuItem(int optionNumber, String description) {
        this.OPTION_NUMBER = optionNumber;
        this.DESCRIPTION = description;
    }


    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public int getOPTION_NUMBER() {
        return OPTION_NUMBER;
    }

    public static void viewMenuItem(String title) {
        System.out.println(System.lineSeparator() + title + System.lineSeparator());
        for (OptionsMenuItem optionMenu : OptionsMenuItem.values()) {
            System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
        }
        //System.out.println("0. Return to main menu");
    }

    public static OptionsMenuItem getOptionByNumber(int number) {
        for (OptionsMenuItem option : OptionsMenuItem.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }


}
