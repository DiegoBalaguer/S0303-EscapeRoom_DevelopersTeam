package mvc.enumsMenu;

public enum OptionsMenuPlayer {

    CREATE(1, "Create Player"),
    LIST_ALL(2, "List All Players"), // Nueva opción
    READ(3, "Find Player by ID"),
    UPDATE(4, "Update Player"),
    SOFT_DELETE(5, "Soft delete Player"),
    DELETE(6, "Delete Player"),
    AWARDS_MANAGEMENT(7, "Awards Management Player"),
    NOTIFY_MANAGEMENT(8, "Notify Management Player"),
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

    public static String viewMenu(String title) {
        StringBuilder message = new StringBuilder();
        message.append(System.lineSeparator()).append(title).append(System.lineSeparator()).append(System.lineSeparator());

        for (OptionsMenuPlayer optionMenu : OptionsMenuPlayer.values()) {
            message
                    .append(optionMenu.getOPTION_NUMBER())
                    .append(". ")
                    .append(optionMenu.getDESCRIPTION())
                    .append(System.lineSeparator());
        }
        return message.toString();
    }

    public static OptionsMenuPlayer getOptionByNumber(int number) {
        for (OptionsMenuPlayer option : OptionsMenuPlayer.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
