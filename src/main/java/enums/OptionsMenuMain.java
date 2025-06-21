package enums;


public enum OptionsMenuMain {

    TICKET_SALE("Ticket sale"),
    PLAYER_MANAGEMENT("Player management"),
    ESCAPE_ROOM_MANAGEMENT("Escape room management"),
    FINANCIAL_MANAGEMENT("Financial management"),
    EXIT("Exit");

    private final String DESCRIPTION;

    // Constructor del enum
    OptionsMenuMain(String description) {
        this.DESCRIPTION = description;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public static void viewMenu(String title) {
        System.out.println(System.lineSeparator() + title + System.lineSeparator());
        for (OptionsMenuMain optionMenu : OptionsMenuMain.values()) {
            System.out.println(optionMenu.ordinal() + 1 + ". " + optionMenu.getDESCRIPTION());
        }
        System.out.print(System.lineSeparator() + "Choose an option: ");
    }
}

