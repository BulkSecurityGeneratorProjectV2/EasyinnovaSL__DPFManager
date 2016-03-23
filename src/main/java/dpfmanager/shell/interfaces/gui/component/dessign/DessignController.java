package dpfmanager.shell.interfaces.gui.component.dessign;

import dpfmanager.conformancechecker.tiff.ProcessInput;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.LogMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class DessignController extends DpfController<DessignModel, DessignView> {

  public void mainCheckFiles() {
    ArrayList<String> files = new ArrayList<>();
    ArrayList<String> extensions = getModel().getExtensions();

    if (getView().getInputText().getText().equals("Select a file")) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Alert");
      alert.setHeaderText("Please select a file");
      alert.showAndWait();
      return;
    }

    RadioButton radio = getView().getSelectedConfig();
    if (radio == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Alert");
      alert.setHeaderText("Please select a configuration file");
      alert.initOwner(GuiWorkbench.getMyStage());
      alert.showAndWait();
      return;
    }
    if (!getModel().readConfig(getFileByPath(radio.getText()).getAbsolutePath())) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Error reading configuration file");
      alert.initOwner(GuiWorkbench.getMyStage());
      alert.showAndWait();
      return;
    } else {
      // Everything OK!
      getView().showLoading();
      Label lblLoading = getView().getLblLoading();

      // Create a background task, because otherwise the loading message is not shown
      Task<Integer> task = new Task<Integer>() {
        @Override
        protected Integer call() throws Exception {
          try {
            TextField txtFile = getView().getInputText();
            if (new File(txtFile.getText()).isDirectory()) {
              File[] listOfFiles = new File(txtFile.getText()).listFiles();
              for (int j = 0; j < listOfFiles.length; j++) {
                if (listOfFiles[j].isFile()) {
                  files.add(listOfFiles[j].getPath());
                }
              }
            } else {
              for (String sfile : txtFile.getText().split(";")) {
                files.add(sfile);
              }
            }

            ProcessInput pi = new ProcessInput();
            pi.setLabelLoading(lblLoading);
            ArrayList<String> formats = getModel().getConfig().getFormats();

            String filefolder = pi.ProcessFiles(files, getModel().getConfig(), true);
            if (pi.isOutOfmemory()) {
              Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occured");
                alert.setContentText("Out of memory");
                alert.showAndWait();
              });
            }

            // When finish, show report
            String type = "";
            String path = "";
            if (formats.contains("HTML")) {
              type = "html";
              path = filefolder + "report.html";
            } else if (formats.contains("XML")) {
              type = "xml";
              path = filefolder + "summary.xml";
            } else if (formats.contains("JSON")) {
              type = "json";
              path = filefolder + "summary.json";
            } else if (formats.contains("PDF")) {
              type = "pdf";
              path = filefolder + "report.pdf";
            }

            // Show reports
            if (!type.isEmpty()) {
              ArrayMessage am = new ArrayMessage();
              am.add(GuiConfig.PRESPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
              am.add(GuiConfig.PRESPECTIVE_SHOW, new UiMessage());
              am.add(GuiConfig.PRESPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
              getContext().send(GuiConfig.PRESPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, am);
            } else {
              // No format
              Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("No output format file was selected");
                alert.setContentText(formats.toString());
                alert.showAndWait();
              });
            }

            getView().hideLoading();

          } catch (Exception ex) {
            Platform.runLater(() -> {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Error");
              alert.setHeaderText("An error occured");
              alert.setContentText(ex.toString());
              alert.showAndWait();
            });
          } catch (OutOfMemoryError er) {
            Platform.runLater(() -> {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Error");
              alert.setHeaderText("An error occured");
              alert.setContentText("Out of memory");
              alert.showAndWait();
            });
          }
          return 0;
        }
      };

      //start the background task
      Thread th = new Thread(task);
      th.setDaemon(true);
      th.start();
    }
  }

  public void selectInputAction() {
    String txtFile = null;
    ComboBox c = getView().getComboChoice();
    String configDir = DPFManagerProperties.getConfigDir();
    if (c.getValue() == "File") {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open File");
      fileChooser.setInitialDirectory(new File(configDir));
      List<File> files = fileChooser.showOpenMultipleDialog(GuiWorkbench.getMyStage());
      if (files != null) {
        String sfiles = "";
        for (File file : files) {
          if (sfiles.length() > 0) sfiles += ";";
          sfiles += file.getPath();
        }
        txtFile = sfiles;
        if (new File(sfiles).exists() && new File(sfiles).getParent() != null && new File(new File(sfiles).getParent()).exists() && new File(new File(sfiles).getParent()).isDirectory()) {
          String path = new File(sfiles).getParent();
          DPFManagerProperties.setDefaultDir(path);
        }
      }
    } else {
      DirectoryChooser folderChooser = new DirectoryChooser();
      folderChooser.setTitle("Open Folder");
      folderChooser.setInitialDirectory(new File(configDir));
      File directory = folderChooser.showDialog(GuiWorkbench.getMyStage());
      if (directory != null) {
        txtFile = directory.getPath();
        DPFManagerProperties.setDefaultDir(directory.getPath());
      }
    }
    if (txtFile != null) {
      getView().getInputText().setText(txtFile);
    }
  }

  public void performEditConfigAction() {
    if (getView().getSelectedConfig() != null) {
      String text = getView().getSelectedConfig().getText();
      String path = getFileByPath(text).getAbsolutePath();
      ArrayMessage am = new ArrayMessage();
      am.add(GuiConfig.PRESPECTIVE_CONFIG, new UiMessage());
      am.add(GuiConfig.PRESPECTIVE_CONFIG + "." + GuiConfig.COMPONENT_CONFIG, new ConfigMessage(ConfigMessage.Type.EDIT, path));
      getContext().send(GuiConfig.PRESPECTIVE_CONFIG, am);
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Alert");
      alert.setHeaderText("Please select a configuration file");
      alert.initOwner(GuiWorkbench.getMyStage());
      alert.showAndWait();
    }
  }

  public void performImportConfigAction() {
    File file;
    String value = GuiWorkbench.getTestParams("import");
    if (value != null) {
      //Test mode
      file = new File(value);
    } else {
      //Ask for file
      String configDir = DPFManagerProperties.getConfigDir();
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Config");
      fileChooser.setInitialDirectory(new File(configDir));
      fileChooser.setInitialFileName("config.dpf");
      file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
    }

    if (file != null) {
      getView().addConfigFile(file.getAbsolutePath());
    }
  }

  public void performDeleteConfigAction(String text) {
    File file = getFileByPath(text);
    if (file.delete()) {
      getView().deleteSelectedConfig();
    } else {
      Alert alert2 = new Alert(Alert.AlertType.ERROR);
      alert2.setTitle("Error");
      alert2.setHeaderText("There was an error deleting the configuration file");
      alert2.initOwner(GuiWorkbench.getMyStage());
      alert2.showAndWait();
    }
  }

  public void testAction() {
    getContext().send(GuiConfig.PRESPECTIVE_DESSIGN + "." + BasicConfig.MODULE_LOGS, new LogMessage(this.getClass(), Level.INFO, "Count: "));

//    String hola = null;
//    hola.isEmpty();

//    // Simulate show report affter check
//    String path = "C:\\Users\\Roser/DPF Manager/reports/20160317/9/report.html";
//    String type = "html";
//    // Create the messages list
//    ArrayMessage am = new ArrayMessage();
//    am.add(GuiConfig.PRESPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
//    am.add(GuiConfig.PRESPECTIVE_SHOW, new UiMessage());
//    am.add(GuiConfig.PRESPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
//    getContext().send(GuiConfig.PRESPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, am);
  }

  private File getFileByPath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      String configDir = DPFManagerProperties.getConfigDir();
      file = new File(configDir + "/" + path);
    }
    return file;
  }
}
