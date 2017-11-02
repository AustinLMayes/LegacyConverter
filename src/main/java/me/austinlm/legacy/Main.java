package me.austinlm.legacy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.austinlm.legacy.dtm.DTMConverter;
import net.avicus.compendium.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Main {

  private final static List<LegacyConverter> CONVERTERS = new ArrayList<>();

  static {
    CONVERTERS.add(new DTMConverter());
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out
          .println("Invalid argument! The argument should be the path to the YML to convert.");
      System.exit(0);
      return;
    }
    String filePath = StringUtils.join(args, " ");
    System.out.println("Using YML File: " + filePath);
    File file = new File(filePath);

    if (!file.exists()) {
      System.out.println("File not found!");
      System.exit(0);
      return;
    }

    Config configMaybe = null;
    try {
      configMaybe = new Config(new FileInputStream(file));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }

    final Config config = configMaybe;

    System.out.println("Beginning Conversion...");
    Document document = new Document();
    Element root = new Element("map");
    for (LegacyConverter c : CONVERTERS) {
      try {
        c.convert(config, root);
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(0);
        return;
      }
    }
    if (root.getContent().isEmpty()) {
      System.out.println("Unsupported map type specified!");
      System.exit(1);
      return;
    }
    document.addContent(root);
    System.out.println("Done!");
    final String dir = System.getProperty("user.dir");
    try {
      XMLOutputter xmlOutput = new XMLOutputter();
      // display nice nice
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(document, new FileWriter(dir + "/out.xml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
      return;
    }
    System.out.println("File saved to " + dir + "/out.xml");
  }
}
