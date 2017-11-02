package me.austinlm.legacy.region;

import lombok.Getter;
import me.austinlm.legacy.XMLSerializable;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

public abstract class Region implements XMLSerializable {

  @Getter
  final protected String name;
  @Getter
  protected boolean inverted;

  public Region(String name, Config config) {
    this.name = name;

    if (config == null) {
      return;
    }

    // Inverted region
    this.inverted = config.getBoolean("invert", false);
  }

  public Element wrapInvert(Element element) {
    element.setAttribute("id", this.getName());
    if (this.isInverted()) {
      element.detach();
      return new Element("invert").addContent(element);
    }
    return element;
  }
}
