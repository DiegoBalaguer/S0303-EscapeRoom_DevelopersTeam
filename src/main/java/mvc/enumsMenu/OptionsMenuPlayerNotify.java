package mvc.enumsMenu;

public enum OptionsMenuPlayerNotify {
    SUBSCRIBE(1, "Subscribe"),
    UNSUBSCRIBE(2, "Unsubscribe"),
    NOTIFICATIONS(3, "Player Notifications"),
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

    public static String viewMenu(String title) {
        StringBuilder message = new StringBuilder();
        message.append(System.lineSeparator()).append(title).append(System.lineSeparator()).append(System.lineSeparator());

        for (OptionsMenuPlayerNotify optionMenu :  OptionsMenuPlayerNotify.values()) {
            message
                    .append(optionMenu.getOPTION_NUMBER())
                    .append(". ")
                    .append(optionMenu.getDESCRIPTION())
                    .append(System.lineSeparator());
        }
        return message.toString();
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
