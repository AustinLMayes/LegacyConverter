package me.austinlm.legacy.region;

import lombok.Getter;
import lombok.ToString;
import me.austinlm.legacy.general.Coordinate;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

@ToString
public class RegionCuboid extends Region {

  @Getter
  Coordinate min;
  @Getter
  Coordinate max;

  public RegionCuboid(String name, Config config) {
    super(name, config);

    if (!config.contains("min")) {
      throw new RuntimeException("Min value not specified for cuboid region, " + name);
    }
    if (!config.contains("max")) {
      throw new RuntimeException("Max value not specified for cuboid region, " + name);
    }

    // Cuboid coordinates
    Coordinate min = new Coordinate(config.getString("min"));
    Coordinate max = new Coordinate(config.getString("max"));

    this.min = min;
    this.max = max;

    this.min.setX(Math.min(min.getX(), max.getX()));
    this.min.setY(Math.min(min.getY(), max.getY()));
    this.min.setZ(Math.min(min.getZ(), max.getZ()));

    this.max.setX(Math.max(min.getX(), max.getX()));
    this.max.setY(Math.max(min.getY(), max.getY()));
    this.max.setZ(Math.max(min.getZ(), max.getZ()));
  }

  @Override
  public void toXML(Element element) {
    Element region = new Element("cuboid");
    region.setAttribute("min", this.min.toXML(false));
    region.setAttribute("max", this.max.toXML(false));

    element.addContent(wrapInvert(region));
  }
}