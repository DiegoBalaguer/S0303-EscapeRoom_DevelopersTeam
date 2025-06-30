package mvc.view;

import mvc.enumsMenu.OptionsMenuPlayerAward;

public class PlayerAwardsView {

    private static BaseView baseView =  new BaseView();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayerAward.viewMenu(title);
    }
}
