package me.austinlm.legacy.util;

import org.jdom2.Element;

public class XmlUtils {

  public static Element getOrCreate(Element parent, String name) {
    Element ret = parent.getChild(name);
    if (ret == null) {
      ret = new Element(name);
      parent.addContent(ret);
    }

    return ret;
  }
}
