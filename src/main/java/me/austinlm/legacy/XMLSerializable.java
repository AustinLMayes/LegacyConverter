package me.austinlm.legacy;

import org.jdom2.Element;

public interface XMLSerializable {

  void toXML(Element element);
}
