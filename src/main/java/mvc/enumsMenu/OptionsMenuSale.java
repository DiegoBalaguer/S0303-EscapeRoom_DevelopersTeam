package mvc.enumsMenu;

public enum OptionsMenuSale {

    SELL(1, "Sell Ticket"),
    DELETE(2, "Delete Ticket"),
    CALCULATE_TOTAL_BENEFITS(3, "Calculate Total Benefits"),
    SHOW_ALL_TICKETS(4, "Show All Tickets"),
    EXIT(0, "Back to Main Menu");

    private final int OPTION_NUMBER;
    private final String DESCRIPTION;

    OptionsMenuSale(int optionNumber, String description) {
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

        for (OptionsMenuSale optionMenu : OptionsMenuSale.values()) {
            message
                    .append(optionMenu.getOPTION_NUMBER())
                    .append(". ")
                    .append(optionMenu.getDESCRIPTION())
                    .append(System.lineSeparator());
        }
        return message.toString();
    }

    public static OptionsMenuSale getOptionByNumber(int number) {
        for (OptionsMenuSale option : OptionsMenuSale.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
