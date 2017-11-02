package me.austinlm.legacy.region.types;

import lombok.Getter;
import lombok.ToString;
import me.austinlm.legacy.general.Coordinate;
import me.austinlm.legacy.region.Region;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

@ToString
public class RegionPoint extends Region {

  @Getter
  Coordinate point;

  public RegionPoint(String name, Config config) {
    super(name, config);

    if (!config.contains("point")) {
      throw new RuntimeException("Point not specified for point region, " + name);
    }

    this.point = new Coordinate(config.getString("point"));
  }

  @Override
  public void toXML(Element element) {
    Element region = new Element("point");
    region.setText(this.getPoint().toXML(true));

    element.addContent(wrapInvert(region));
  }
}