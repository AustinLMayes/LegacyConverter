package me.austinlm.legacy;

import org.jdom2.Element;

public class RegionUtils {

  public static void regionRef(Element root, String id) {
    root.addContent(new Element("region").setAttribute("id", id));
  }
}
