package dpfmanager.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.commons.lang.SystemUtils;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationFixture;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

  //Set properties for headless mode (Windows only)
  static {
    if (SystemUtils.IS_OS_WINDOWS) {
//      System.setProperty("testfx.robot", "glass");
//      System.setProperty("testfx.headless", "true");
    }
  }

  final static int width = 1280;
  final static int height = 1024;

  static Stage stage;
  protected Scene scene;
  static SpreadsheetView view;

  public static Stage launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
    stage = FxToolkit.registerPrimaryStage();
    FxToolkit.setupStage(stage -> {
      view = new SpreadsheetView();
      StackPane sceneRoot = new StackPane(view);

      stage.setScene(new Scene(sceneRoot, 1280, 1024));
      stage.setX(0);
      stage.setY(0);
      stage.setMinHeight(1024);

      stage.show();
      stage.toBack();
      stage.toFront();
    });
    FxToolkit.setupApplication(appClass, appArgs);
    FxToolkit.toolkitContext().getRegisteredStage().setWidth(1280);
    FxToolkit.toolkitContext().getRegisteredStage().setHeight(1024);
    return stage;
  }

  @Before
  public final void internalBefore() throws Exception {
    FxToolkit.setupApplication(this);
  }

  @After
  public final void internalAfter() throws Exception {
    FxToolkit.cleanupStages();
    FxToolkit.cleanupApplication(this);
  }

  @Override
  public void init() throws Exception {
  }

  @Override
  public void start(Stage stage) throws Exception {
    FxToolkit.showStage();
  }

  @Override
  public void stop() throws Exception {
    FxToolkit.hideStage();
  }

  public void clickOnAndReload(String id){
    clickOn(id);
    scene = stage.getScene();
  }

  public void reloadScene(){
    scene = stage.getScene();
  }

  protected void clickOnImportedConfig(String path){
    AnchorPane ap = (AnchorPane) scene.lookup("#pane1");  //Get Anchor Pane
    VBox vbox = (VBox) ap.getChildren().get(0);           //Get VBox
    String idToClick = "#";
    String search = path.replaceAll("/","_").replaceAll("\\\\","_");
    for (Node node : vbox.getChildren()){
      RadioButton rb = (RadioButton) node;
      String text = rb.getText().replaceAll("/", "_").replaceAll("\\\\","_");
      if (text.endsWith(search)){
        idToClick += rb.getId();
      }
    }
    clickOn(idToClick);
  }
  protected void writeText(String id, String text){
    TextField txtField = (TextField) scene.lookup(id);
    int length = txtField.getText().length();
    clickOn(id).eraseText(length).write(text);
  }

  protected void waitForCheckFiles(int maxTimeout){
    sleep(1000);
    int timeout = 0;
    boolean finish = false;
    while (!finish && timeout < maxTimeout) {
      System.out.println("Inside timeout "+timeout);
      reloadScene();
      Node node = scene.lookup("#loadingPane");
      if (node != null) {
        timeout++;
        sleep(1000);
      } else {
        finish = true;
      }
    }
    sleep(1000);
    Assert.assertNotEquals("Check files reached timeout! ("+maxTimeout+"s)", maxTimeout, timeout);
  }

}
