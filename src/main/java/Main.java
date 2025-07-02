import app.AppManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

      public static void main(String[] args) {

          AppManager.getInstance().startApp(args.length > 0 ? args[0] : "");

        log.info("The application is shutting down.");
    }
}
