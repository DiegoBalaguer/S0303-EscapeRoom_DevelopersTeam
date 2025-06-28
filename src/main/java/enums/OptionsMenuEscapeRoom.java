package enums;

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

        public static void viewMenu(String title) {
            System.out.println(System.lineSeparator() + title + System.lineSeparator());

            enums.OptionsMenuEscapeRoom[] options = enums.OptionsMenuEscapeRoom.values();

            for (enums.OptionsMenuEscapeRoom optionMenu : options) {
                System.out.println(optionMenu.getOPTION_NUMBER() + ". " + optionMenu.getDESCRIPTION());
            }
            System.out.println("");
        }

        // Optional
        public static enums.OptionsMenuEscapeRoom getOptionByNumber(int number) {
            for (enums.OptionsMenuEscapeRoom option : enums.OptionsMenuEscapeRoom.values()) {
                if (option.getOPTION_NUMBER() == number) {
                    return option;
                }
            }
            return null;
        }
    }
