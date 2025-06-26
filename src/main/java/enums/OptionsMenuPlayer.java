package enums;

public enum OptionsMenuPlayer {

    CREATE(1, "Create Player"),
    LIST_ALL(2, "List All Players"), // Nueva opción
    READ(3, "Find Player by ID"),
    UPDATE(4, "Update Player"),
    DELETE(5, "Delete Player"),
    PROVIDE_CERTIFICATES(6, "Provide Certificates"),
    PROVIDE_REWARDS(7, "Provide Rewards"),
    SUBSCRIBE(8, "Subscribe"),
    UNSUBSCRIBE(9, "Unsubscribe"),
    NOTIFY(10, "Notify Player"),
    EXIT(0, "Back to Main Menu");

    private final int OPTION_NUMBER;
    private final String DESCRIPTION;

    OptionsMenuPlayer(int optionNumber, String description) {
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

        OptionsMenuPlayer[] options = OptionsMenuPlayer.values();

        for (OptionsMenuPlayer optionMenu : options) {
            System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
        }
        System.out.println("");
    }

    // Optional
    public static OptionsMenuPlayer getOptionByNumber(int number) {
        for (OptionsMenuPlayer option : OptionsMenuPlayer.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
