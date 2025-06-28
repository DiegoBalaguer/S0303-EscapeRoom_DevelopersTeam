package mvc.enumsMenu;

public enum OptionsMenuPlayerNotify {
    SUBSCRIBE(1, "Subscribe"),
    UNSUBSCRIBE(2, "Unsubscribe"),
    NOTIFY(3, "Notify Player"),
    EXIT(0, "Back to Main Menu");

    private final int OPTION_NUMBER;
    private final String DESCRIPTION;

    OptionsMenuPlayerNotify(int optionNumber, String description) {
        this.OPTION_NUMBER = optionNumber;
        this.DESCRIPTION = description;
    }

    public int getOPTION_NUMBER() {
        return OPTION_NUMBER;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public static void viewMenu(String title) {
        System.out.println(System.lineSeparator() + title + System.lineSeparator());

        OptionsMenuPlayerNotify[] options = OptionsMenuPlayerNotify.values();

        for (OptionsMenuPlayerNotify optionMenu : options) {
            System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
        }
        System.out.println("");
    }

    public static OptionsMenuPlayerNotify getOptionByNumber(int number) {
        for (OptionsMenuPlayerNotify option : OptionsMenuPlayerNotify.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
