package dpfmanager.commandline;

import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Adrià Llorens on 12/04/2016.
 */
public class CommandLineTest {

  boolean feedback;
  String lastReport;

  @Before
  public void PreTest() {
    // Save feedback
    feedback = DPFManagerProperties.getFeedback();
    DPFManagerProperties.setFeedback(false);
    // Last report
    lastReport = ReportGenerator.getLastReportPath();
    // Backup conformance checkers configuration
    backUpCC();
  }

  @After
  public void PostTest() {
    // Set feedback
    DPFManagerProperties.setFeedback(feedback);
    // Delete all reports
    deleteReports();
    // Restore conformance checkers configuration
    restoreCC();
  }

  private void backUpCC(){
    File currentFile = new File(DPFManagerProperties.getConformancesConfig());
    File backupFile = new File(DPFManagerProperties.getConformancesConfig()+".backup");
    try {
      if (currentFile.exists()) {
        FileUtils.copyFile(currentFile, backupFile);
        currentFile.delete();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void restoreCC(){
    File currentFile = new File(DPFManagerProperties.getConformancesConfig());
    File backupFile = new File(DPFManagerProperties.getConformancesConfig()+".backup");
    currentFile.delete();
    backupFile.renameTo(currentFile);
  }

  public void waitForFinishMultiThred(int maxTimeout) throws InterruptedException {
    int timeout = 0;
    while (!DPFManagerProperties.isFinished() && timeout < maxTimeout) {
      Thread.sleep(1000);
      timeout++;
    }
    DPFManagerProperties.setFinished(false);
    Assert.assertNotEquals("Timeout for command line app reached! (" + maxTimeout + "s)", maxTimeout, timeout);
  }

  private void deleteReports() {
    try {
      if (endsWithDate(lastReport)) {
        // No reports before
        FileUtils.deleteDirectory(new File(lastReport));
      } else {
        // Delete new reports
        int index = getIndex(lastReport);
        String folder = getReportFolder(lastReport);
        index++;
        File file = new File(folder + index);
        while (file.exists()) {
          FileUtils.deleteDirectory(file);
          // Zip
          File zipFile = new File(file.getAbsolutePath()+".zip");
          if (zipFile.exists()){
            zipFile.delete();
          }
          index++;
          file = new File(folder + index);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      Assert.assertEquals(1, 0);
    }
  }

  private boolean endsWithDate(String path) {
    String last8 = path.substring(path.length() - 8);
    try {
      Integer.parseInt(last8);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private int getIndex(String path) {
    path = path.substring(0, path.length() - 1);
    int last = path.lastIndexOf("/");
    String aux = path.substring(last + 1);
    return Integer.parseInt(aux);
  }

  private String getReportFolder(String path) {
    path = path.substring(0, path.length() - 1);
    int last = path.lastIndexOf("/");
    String aux = path.substring(0, last + 1);
    return aux;
  }

  public void sendFtpCamel(String summaryXmlFile)
      throws NoSuchAlgorithmException, IOException {
    byte[] summaryXml = FileUtils.readFileToByteArray(new File(summaryXmlFile));
    String ftp = "84.88.145.109";
    String user = "preformaapp";
    String password = "2.eX#lh>";
    CamelContext contextcc = new DefaultCamelContext();

    try {
      contextcc.addRoutes(new RouteBuilder() {
        public void configure() {
          from("direct:sendFtp").to("sftp://" + user + "@" + ftp + "/?password=" + password);
        }
      });
      ProducerTemplate template = contextcc.createProducerTemplate();
      contextcc.start();
      template.sendBody("direct:sendFtp", summaryXml);
      contextcc.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
