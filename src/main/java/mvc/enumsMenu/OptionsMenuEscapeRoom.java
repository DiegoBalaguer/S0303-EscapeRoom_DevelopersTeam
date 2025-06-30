package mvc.enumsMenu;

public enum OptionsMenuEscapeRoom {

    CLUE_MANAGEMENT(1, "Clue management"),
    DECORATION_MANAGEMENT(2, "Decoration management"),
    EXIT(0, "Exit");

    private final int OPTION_NUMBER;
    private final String DESCRIPTION;

    OptionsMenuEscapeRoom(int optionNumber, String description) {
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

        for (OptionsMenuEscapeRoom optionMenu : OptionsMenuEscapeRoom.values()) {
            message
                    .append(optionMenu.getOPTION_NUMBER())
                    .append(". ")
                    .append(optionMenu.getDESCRIPTION())
                    .append(System.lineSeparator());
        }
        return message.toString();
    }

    public static OptionsMenuEscapeRoom getOptionByNumber(int number) {
        for (OptionsMenuEscapeRoom option : OptionsMenuEscapeRoom.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
