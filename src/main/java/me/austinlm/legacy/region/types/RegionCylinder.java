package me.austinlm.legacy.region.types;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.austinlm.legacy.general.Coordinate;
import me.austinlm.legacy.region.Region;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

@ToString
public class RegionCylinder extends Region {

  @Getter
  @Setter
  Coordinate center;
  @Getter
  @Setter
  int radius;
  @Getter
  @Setter
  int height;
  @Getter
  @Setter
  boolean hollow;

  public RegionCylinder(String name, Config config) {
    super(name, config);

    if (!config.contains("center")) {
      throw new RuntimeException("Center not specified for cylinder region, " + name);
    }
    if (!config.contains("radius")) {
      throw new RuntimeException("Radius not specified for cylinder region, " + name);
    }
    if (!config.contains("height")) {
      throw new RuntimeException("Height not specified for cylinder region, " + name);
    }

    this.center = new Coordinate(config.getString("center"));
    this.radius = config.getInt("radius");
    this.height = config.getInt("height");
    this.hollow = config.getBoolean("hollow", false);

    if (this.radius <= 0) {
      throw new RuntimeException("Invalid radius provided for cylinder region, " + name);
    }
    if (this.height <= 0) {
      throw new RuntimeException("Invalid height provided for cylinder region, " + name);
    }
  }

  @Override
  public void toXML(Element element) {
    Element region = new Element("cylinder");
    region.setAttribute("base", this.getCenter().toXML(false));
    region.setAttribute("radius", Integer.toString(this.getRadius()));
    region.setAttribute("height", Integer.toString(this.getHeight()));

    element.addContent(wrapInvert(region));
  }
}
