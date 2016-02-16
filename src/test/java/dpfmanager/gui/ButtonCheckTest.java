package dpfmanager.gui;

import dpfmanager.RebirthApp;
import dpfmanager.shell.MainApp;
import javafx.scene.Node;
import javafx.stage.Stage;

import org.jrebirth.af.api.concurrent.JRebirthRunnable;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;


/**
 * Created by Adrià Llorens on 13/01/2016.
 */
public class ButtonCheckTest extends ApplicationTest {

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(RebirthApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testButtonsBelow() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running check buttons test...");

    // Continue 3 button
    clickOnAndReload("#newButton");
    clickOnAndReload("#continue");
    clickOnAndReload("#continue");
    clickOnAndReload("#continue");
    FxAssert.verifyThat("#included4", NodeMatchers.isVisible());

    // Check files button
    clickOnAndReload("#butDessign");
    clickOnAndReload("#checkFilesButton");
    clickOnAndReload("#butReports");
    FxAssert.verifyThat("#tab_reports", NodeMatchers.isNull());
  }
}

