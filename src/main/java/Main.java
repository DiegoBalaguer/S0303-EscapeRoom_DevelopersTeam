import app.AppController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

      public static void main(String[] args) {

        AppController appController = new AppController();

        appController.run();

        log.info("The application is shutting down.");

    }
}
