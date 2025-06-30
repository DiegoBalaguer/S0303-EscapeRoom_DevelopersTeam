package mvc.enumsMenu;

public enum OptionsMenuCrud {
    ADD (1,"Create"),
    SHOW(2,"Show"),
    REMOVE(3,"Delete"),
    UPDATE(4,"Update information"),
    CALCULATE(5,"Calculate item budget"),
    EXIT(0,"Return to main menu");

    private final String DESCRIPTION;
    private final int OPTION_NUMBER;

    OptionsMenuCrud(int optionNumber, String description) {
        this.OPTION_NUMBER = optionNumber;
        this.DESCRIPTION = description;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public int getOPTION_NUMBER() {
        return OPTION_NUMBER;
    }

    public static String viewMenu(String title) {
    StringBuilder message = new StringBuilder();
    message.append(System.lineSeparator()).append(title).append(System.lineSeparator()).append(System.lineSeparator());

        for (OptionsMenuCrud optionMenu : OptionsMenuCrud.values()) {
            message
                    .append(optionMenu.getOPTION_NUMBER())
                    .append(". ")
                    .append(optionMenu.getDESCRIPTION())
                    .append(System.lineSeparator());
        }
        return message.toString();
    }

    public static OptionsMenuCrud getOptionByNumber(int number) {
        for (OptionsMenuCrud option : OptionsMenuCrud.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
