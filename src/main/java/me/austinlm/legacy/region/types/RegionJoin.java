package me.austinlm.legacy.region.types;

import java.util.List;
import lombok.ToString;
import me.austinlm.legacy.region.Region;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

@ToString
public class RegionJoin extends Region {

  List<String> list;

  public RegionJoin(String name, Config config) {
    super(name, config);

    if (!config.contains("regions")) {
      throw new RuntimeException("List of regions not provided in join region");
    }

    this.list = config.getStringList("regions");
  }

  @Override
  public void toXML(Element element) {
    Element region = new Element("join");
    this.list.forEach(l -> region.addContent(new Element("region").setAttribute("id", l)));

    element.addContent(wrapInvert(region));
  }
}