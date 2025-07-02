package mvc.enumsMenu;

public enum OptionsMenuCLFUSDE {

    CREATE(1, "Create item"),
    LIST_ALL(2, "List All items"), // Nueva opción
    FIND_BY_ID(3, "Find item by ID"),
    UPDATE(4, "Update item"),
    SOFT_DELETE(5, "Soft delete item"),
    DELETE(6, "Delete item"),
    EXIT(0, "Back to Main Menu");

    private final int OPTION_NUMBER;
    private final String DESCRIPTION;

    OptionsMenuCLFUSDE(int optionNumber, String description) {
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

        for (OptionsMenuCLFUSDE optionMenu : OptionsMenuCLFUSDE.values()) {
            message
                    .append(optionMenu.getOPTION_NUMBER())
                    .append(". ")
                    .append(optionMenu.getDESCRIPTION())
                    .append(System.lineSeparator());
        }
        return message.toString();
    }

    public static OptionsMenuCLFUSDE getOptionByNumber(int number) {
        for (OptionsMenuCLFUSDE option : OptionsMenuCLFUSDE.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
