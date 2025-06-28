package enums;

public enum OptionsMenuPlayerAward {
    AWARD_CERTIFICATE_WIN(1, "Award Certificate win to player"),
    AWARD_REWARD_WIN(2, "Award Reward win to player"),
    REVOKE_CERTIFICATE_WIN(3, "Revoke Certificate win to player"),
    REVOKE_REWARD_WIN(4, "Revoke Reward win to player"),
    EXIT(0, "Back to Main Menu");

    private final int OPTION_NUMBER;
    private final String DESCRIPTION;

    OptionsMenuPlayerAward(int optionNumber, String description) {
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

        OptionsMenuPlayerAward[] options = OptionsMenuPlayerAward.values();

        for (OptionsMenuPlayerAward optionMenu : options) {
            System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
        }
        System.out.println("");
    }

    public static OptionsMenuPlayerAward getOptionByNumber(int number) {
        for (OptionsMenuPlayerAward option : OptionsMenuPlayerAward.values()) {
            if (option.getOPTION_NUMBER() == number) {
                return option;
            }
        }
        return null;
    }
}
