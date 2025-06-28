package enums;

public enum OptionsMenuClue {
        ADD (1,"Create"),
        SHOW(2,"Show"),
        REMOVE(3,"Delete"),
        UPDATE(4,"Update information"),
        EXIT(0,"Return to main menu");

        private final String DESCRIPTION;
        private final int OPTION_NUMBER;


        OptionsMenuClue(int optionNumber, String description) {
            this.OPTION_NUMBER = optionNumber;
            this.DESCRIPTION = description;
        }


        public String getDESCRIPTION() {
            return DESCRIPTION;
        }

        public int getOPTION_NUMBER() {
            return OPTION_NUMBER;
        }

        public static void viewMenuClue(String title) {
            System.out.println(System.lineSeparator() + title + System.lineSeparator());
            for (enums.OptionsMenuClue optionMenu : enums.OptionsMenuClue.values()) {
                System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
            }
            //System.out.println("0. Return to main menu");
        }

        public static enums.OptionsMenuClue getOptionByNumber(int number) {
            for (enums.OptionsMenuClue option : enums.OptionsMenuClue.values()) {
                if (option.getOPTION_NUMBER() == number) {
                    return option;
                }
            }
            return null;
        }


}
