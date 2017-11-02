package me.austinlm.legacy.region.types;

import lombok.ToString;
import me.austinlm.legacy.region.Region;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

@ToString
public class RegionGlobal extends Region {

  public RegionGlobal(String name, Config config) {
    super(name, config);
  }

  @Override
  public void toXML(Element element) {
    Element region = new Element("everywhere");
    element.addContent(wrapInvert(region));
  }
}