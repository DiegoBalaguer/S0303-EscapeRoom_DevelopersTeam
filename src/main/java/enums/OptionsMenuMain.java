package enums;

public enum OptionsMenuMain {

    TICKET_MANAGEMENT(1, "Ticket management"),
    ROOM_MANAGEMENT(2, "Room management"),
    PLAYER_MANAGEMENT(3, "Player management"),
    ESCAPE_ROOM_MANAGEMENT(4, "Escape room management"),
    FINANCIAL_MANAGEMENT(5, "Financial management"),
    EXIT(0, "Exit");

    private final int OPTION_NUMBER;
    private final String DESCRIPTION;

    OptionsMenuMain(int optionNumber, String description) {
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

        OptionsMenuMain[] options = OptionsMenuMain.values();

        for (OptionsMenuMain optionMenu : options) {
             System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
        }
        System.out.println("");
    }

    // Optional
    public static OptionsMenuMain getOptionByNumber(int number) {
        for (OptionsMenuMain option : OptionsMenuMain.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
