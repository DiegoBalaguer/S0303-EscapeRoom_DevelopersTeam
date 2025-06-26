package enums;

public enum OptionsMenuSale {

    SELL(1, "Sell Ticket"),
    DELETE(2, "Delete Ticket"),
    CALCULATE_TOTAL_BENEFITS(3, "Calculate Total Benefits"),
    SHOW_ALL_TICKETS(4, "Show All Tickets"),
    SALE_DETAILS(5, "Sale Details"),
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

    public static void viewMenu(String title) {
        System.out.println(System.lineSeparator() + title + System.lineSeparator());

        OptionsMenuSale[] options = OptionsMenuSale.values();

        for (OptionsMenuSale optionMenu : options) {
            System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
        }
        System.out.println("");
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
