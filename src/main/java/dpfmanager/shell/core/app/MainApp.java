package dpfmanager.shell.core.app;

import dpfmanager.shell.application.app.CommandLineApp;
import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.application.app.ServerApp;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
public class MainApp {

  public static void main(String[] args) {
    // Initial, set log level to severe (remove JacpFX logs)
    Logger rootLog = Logger.getLogger("");
    rootLog.setLevel(Level.SEVERE);

    List<String> params = Arrays.asList(args);
    if (params.isEmpty() || params.contains("-gui")) {
      GuiApp.main(args);
    } else if (params.contains("-server")) {
      ServerApp.main(args);
    } else {
      CommandLineApp.main(args);
    }
  }
}
