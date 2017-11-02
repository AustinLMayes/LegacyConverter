package me.austinlm.legacy.region;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.austinlm.legacy.general.Coordinate;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

@ToString
public class RegionSphere extends Region {

  @Getter
  @Setter
  Coordinate center;
  @Getter
  @Setter
  int radius;
  @Getter
  @Setter
  boolean hollow;

  public RegionSphere(String name, Config config) {
    super(name, config);

    if (config == null) {
      return;
    }

    if (!config.contains("center")) {
      throw new RuntimeException("Center specified for sphere region, " + name);
    }
    if (!config.contains("radius")) {
      throw new RuntimeException("Radius not specified for sphere region, " + name);
    }

    this.center = new Coordinate(config.getString("center"));
    this.radius = config.getInt("radius");
    this.hollow = config.getBoolean("hollow", false);

    if (this.radius <= 0) {
      throw new RuntimeException("Invalid radius provided for sphere region, " + name);
    }
  }

  @Override
  public void toXML(Element element) {
    Element region = new Element("sphere");
    element.setAttribute("origin", this.getCenter().toXML(false));
    element.setAttribute("radius", Integer.toString(this.getRadius()));

    element.addContent(wrapInvert(region));
  }
}
