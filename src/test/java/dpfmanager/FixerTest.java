package dpfmanager;

import static java.io.File.separator;

import dpfmanager.shell.modules.classes.Schematron;
import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by easy on 13/10/2015.
 */
public class FixerTest extends TestCase {
  TiffReader tr;

  @After
  public static void afterClass() {
    Platform.exit();
  }

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

  public void testAddRemoveTag() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String configfile = "xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "ISO\tTiff/EP\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "RULE\tImageWidth,>,1000\n" +
        "FIX\tCopyright,Remove Tag,\n" +
        "FIX\tImageDescription,Add Tag,description\n");
    bw.close();

    String path = "output";
    idx=1;
    while (new File(path).exists()) path = "output" + idx++;

    String[] args = new String[6];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

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
    Platform.exit();

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()){
      if (file.equals("Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_modif != null, true);
    assertEquals(xml_orig.contains("<name>Copyright</name>"), true);
    assertEquals(xml_modif.contains("<name>Copyright</name>"), false);
    assertEquals(xml_orig.contains("<name>ImageDescription</name>"), false);
    assertEquals(xml_modif.contains("<name>ImageDescription</name>"), true);

    FileUtils.deleteDirectory(new File(path));

    Platform.exit();

    new File(configfile).delete();
  }

  public void testAddExistingTag() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String configfile = "xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "FIX\tArtist,Add Tag,NewArtist\n");
    bw.close();

    String path = "output";
    idx=1;
    while (new File(path).exists()) path = "output" + idx++;

    String[] args = new String[6];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

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
    Platform.exit();

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()){
      if (file.equals("Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_modif != null, true);
    assertEquals(xml_orig.contains("NewArtist"), false);
    assertEquals(xml_modif.contains("NewArtist"), true);

    FileUtils.deleteDirectory(new File(path));

    Platform.exit();

    new File(configfile).delete();
  }

  public void testAutofixPrivateData() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String configfile = "xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "FIX\tclearPrivateData,Yes\n");
    bw.close();

    String path = "output";
    idx=1;
    while (new File(path).exists()) path = "output" + idx++;

    String[] args = new String[6];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

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
    Platform.exit();

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()){
      if (file.equals("Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_modif != null, true);
    assertEquals(xml_modif.contains("GPS"), false);

    FileUtils.deleteDirectory(new File(path));

    Platform.exit();

    new File(configfile).delete();
  }

  public void testReports() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String configfile = "xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "ISO\tTiff/EP\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "RULE\tImageWidth,>,1000\n" +
        "FIX\tCopyright,Remove Tag,\n" +
        "FIX\tImageDescription,Add Tag,description\n");
    bw.close();

    String path = "output";
    idx=1;
    while (new File(path).exists()) path = "output" + idx++;

    String[] args = new String[6];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

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
    Platform.exit();

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()){
      if (file.equals("Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_modif != null, true);

    directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html_orig = null;
    String html_modif = null;
    for (String file : directori.list()){
      if (file.equals("Bilevel.tif.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html_orig = new String(encoded);
      }
      if (file.equals("Bilevel.tif_fixed.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html_modif = new String(encoded);
      }
    }
    assertEquals(html_orig != null, true);
    assertEquals(html_modif != null, true);
    assertEquals(html_orig.contains("Bilevel.tif_fixed.html"), true);
    assertEquals(html_modif.contains("Bilevel.tif.html"), true);

    FileUtils.deleteDirectory(new File(path));

    Platform.exit();

    new File(configfile).delete();
  }
}