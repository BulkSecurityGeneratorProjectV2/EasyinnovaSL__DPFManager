package dpfmanager.gui;

import dpfmanager.MainApp;
import dpfmanager.shell.modules.reporting.ReportGenerator;
import javafx.stage.Stage;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public class FirstLaunchTest extends ApplicationTest {

  Stage stage = null;

  @Override
  public void init() throws Exception{
    stage = launch(MainApp.class);
    scene = stage.getScene();
  }

  @BeforeClass
  public final static void beforeClass() {
    // Backup the config file
    File reportFolder = new File(ReportGenerator.getReportsFolder());
    File configFileOld = new File(reportFolder.getParent()+"/dpfmanager.properties");
    File configFileNew = new File(reportFolder.getParent()+"/dpfmanager.properties-bak");
    if (configFileOld.exists()){
      configFileOld.renameTo(configFileNew);
    }
  }

  @AfterClass
  public final static void afterClass() {
    // Restore config file
    File reportFolder = new File(ReportGenerator.getReportsFolder());
    File configFileOld = new File(reportFolder.getParent()+"/dpfmanager.properties");
    File configFileNew = new File(reportFolder.getParent()+"/dpfmanager.properties-bak");
    if (configFileOld.exists()){
      configFileOld.delete();
    }
    if (configFileNew.exists()){
      configFileNew.renameTo(configFileOld);
    }
  }

  @Test
  public void testFirstScreen() throws Exception {
    //Now init app
    WaitForAsyncUtils.waitForFxEvents();
    FxAssert.verifyThat("#welcomeText", NodeMatchers.hasText("Welcome to DPF Manager!"));

    //Check config file now exists
    File reportFolder = new File(ReportGenerator.getReportsFolder());
    File configFile = new File(reportFolder.getParent()+"/dpfmanager.properties");
    Assert.assertTrue("Config file (dpfmanager.properties) does't exist", configFile.exists());
  }

}
