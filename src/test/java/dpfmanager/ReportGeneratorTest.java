package dpfmanager;

import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;

import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by Easy on 20/07/2015.
 */
public class ReportGeneratorTest extends TestCase {
  TiffReader tr;

  /**
   * Pre test.
   */
  @Before
  public void PreTest() {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  public void testHTMLTags() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String path = "output";
    int idx=1;
    while (new File(path).exists()) path = "output" + idx++;

    String[] args = new String[4];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        for (int i=0;i<args.length;i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        for (int i=0;i<args.length;i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    for (String file : directori.list()){
      if (file.equals("Bilevel.tif.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    assertEquals(html.contains("<td>256</td><td>ImageWidth</td><td>999</td>"), true);
    assertEquals(html.contains("<td>257</td><td>ImageLength</td><td>662</td>"), true);
    assertEquals(html.contains("<td>305</td><td>Software</td><td>Adobe Photoshop CS6 (Macintosh)</td>"), true);

    FileUtils.deleteDirectory(new File(path));

    Platform.exit();
  }

  public void testReportsFile() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[2];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    String path = getPath();

    File directori = new File(path);
    assertEquals(6, directori.list().length);
    Platform.exit();
  }

  public void testReportsFolder() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[2];
    args[0] = "src/test/resources/Small";
    args[1] = "-s";

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    String path = getPath();

    File directori = new File(path);
    assertEquals(16, directori.list().length);
    Platform.exit();
  }

  public void testReportsZip() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[2];
    args[0] = "src/test/resources/Small.zip";
    args[1] = "-s";

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    String path = getPath();

    File directori = new File(path);
    assertEquals(12, directori.list().length);
    Platform.exit();
  }

  public void testReportsURL() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String dirWeb="http://dpfmanager.org/img/Bilevel.tif";
    try{

      Socket s = new Socket("www.google.com", 80);
      String[] args = new String[2];
      args[0] =dirWeb;
      args[1] = "-s";

      Application.Parameters params=new Application.Parameters() {
        @Override
        public List<String> getRaw() {
          ArrayList<String> listRaw=new ArrayList<String>();
          listRaw.add(args[0]);
          listRaw.add(args[1]);
          return listRaw;
        }

        @Override
        public List<String> getUnnamed() {
          ArrayList<String> listRaw=new ArrayList<String>();
          listRaw.add(args[0]);
          listRaw.add(args[1]);
          return listRaw;
        }

        @Override
        public Map<String, String> getNamed() {
          return null;
        }
      };

      CommandLine cl = new CommandLine(params);
      cl.launch();

      String path = getPath();

      File directori = new File(path);
      assertEquals(6, directori.list().length);
      Platform.exit();
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public void testReportsFormat() throws Exception {
    assertReportsFormat("html");
    assertReportsFormat("xml");
    assertReportsFormat("json");
    assertReportsFormat("xml,html");
    assertReportsFormat("xml,json");
    assertReportsFormat("json,html");
    assertReportsFormat("xml,json,html");
  }

  private void assertReportsFormat(String formats) throws Exception {
    String[] args = new String[4];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-reportformat";
    args[3] = formats;

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        listRaw.add(args[3]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        listRaw.add(args[3]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    String path = getPath();

    File directori = new File(path);
    int filesExpect=(formats.split(",").length)*2;

    assertEquals(directori.list().length,filesExpect);

    String extension="";
    boolean isXML=false;
    boolean isHTML=false;
    boolean isJSON=false;
    for(File file:directori.listFiles()){
      extension=file.getAbsolutePath();
      if(!file.isDirectory()) {
        if (formats.contains("xml")&&!isXML) {
          isXML=extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".xml");
        }
        if (formats.contains("json")&&!isJSON) {
          isJSON=extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".json");
        }
        if (formats.contains("html")&&!isHTML) {
         isHTML=extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".html");
        }
      }
    }
    if (formats.contains("xml")) {
      assertEquals(true,isXML);
    }
    if (formats.contains("json")) {
      assertEquals(true,isJSON);
    }
    if (formats.contains("html")) {
      assertEquals(true,isHTML);
    }
    Platform.exit();
  }

  private String getPath() {
    String path = "reports";
    File theDir = new File(path);
    // date folder
    path += "/" + FastDateFormat.getInstance("yyyyMMdd").format(new Date());
    theDir = new File(path);

    // index folder
    int index = 1;
    File file = new File(path + "/" + index);
    while (file.exists()) {
      index++;
      file = new File(path + "/" + index);
      if (!file.exists()) {
        file = new File(path + "/" + (index - 1));
        break;
      }
    }
    path += "/" + (index - 1);
    return path;
  }
}
