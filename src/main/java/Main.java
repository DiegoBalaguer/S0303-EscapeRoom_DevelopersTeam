import app.MainMenu;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

      public static void main(String[] args) {

          MainMenu mainMenu = new MainMenu();

          mainMenu.showMainMenu();


        log.info("The application is shutting down.");

    }
}
