package me.austinlm.legacy.region;

import java.util.Map;
import lombok.Getter;
import me.austinlm.legacy.region.types.RegionCuboid;
import me.austinlm.legacy.region.types.RegionCylinder;
import me.austinlm.legacy.region.types.RegionGlobal;
import me.austinlm.legacy.region.types.RegionJoin;
import me.austinlm.legacy.region.types.RegionPoint;
import me.austinlm.legacy.region.types.RegionSphere;
import me.austinlm.legacy.util.XmlUtils;
import net.avicus.compendium.config.Config;
import org.jdom2.Comment;
import org.jdom2.Element;

/**
 * Regions used to be regions+zones+executors.
 * This makes life hard.
 */
public class RegionConverter {

  @Getter
  private final Element root;

  public RegionConverter(Element root) {
    this.root = root;
  }

  public void convert(Config regionSect, Element regionEl) {
    regionSect.getData().forEach((n, c) -> {
      parseRegion(regionEl, new Config((Map<Object, Object>) c), n.toString());
    });
  }

  private void parseRegion(Element regionEl, Config input, String name) {
    String type = input.getString("type");

    Region region;
    if (type.startsWith("cuboid")) {
      region = new RegionCuboid(name, input);
    } else if (type.equals("sphere")) {
      region = new RegionSphere(name, input);
    } else if (type.equals("cylinder")) {
      region = new RegionCylinder(name, input);
    } else if (type.equals("global")) {
      region = new RegionGlobal(name, input);
    } else if (type.equals("point")) {
      region = new RegionPoint(name, input);
    } else if (type.equals("join")) {
      region = new RegionJoin(name, input);
    } else {
      throw new RuntimeException("Invalid region type provided: " + type);
    }

    if (input.contains("flags")) {
      parseFlags(name, input.getConfig("flags").getData());
    }

    region.toXML(regionEl);
  }

  private void parseFlags(String region, Map<Object, Object> flags) {
    Element zones = XmlUtils.getOrCreate(this.getRoot(), "zones");
    flags.forEach(
        (n, d) -> parseFlag(zones, region, new Config((Map<Object, Object>) d), n.toString()));
  }

  private void parseFlag(Element zones, String region, Config config, String name) {
    if (name == null) {
      name = config.getString("type");
    }
    name = name.toLowerCase();

    if (name.equals("move") || name.equals("enter") || name.equals("leave")) {
      move(name, region, config, zones);
    } else if (name.equals("build")) {
      build(region, config, zones);
    } else if (name.equals("interact")) {
      interact(region, config, zones);
    } else if (name.equals("kill_player")) {
      killPlayer(region, config, zones);
    } else if (name.equals("shoot_bow")) {
      shootBow(region, config, zones);
    } else if (name.equals("drop_item")) {
      dropItem(region, config, zones);
    } else if (name.equals("pickup_item")) {
      pickup(region, config, zones);
    } else if (name.equals("decay")) {
      decay(region, config, zones);
    } else if (name.equals("dispense")) {
      dispense(region, config, zones);
    } else if (name.contains("damage")) {
      damage(region, config, zones);
    } else {
      throw new RuntimeException("Invalid flag type provided: " + name);
    }
  }

  private Element getBaseElement(String region, Config config) {
    Element toApply = new Element("zone");
    toApply.setAttribute("region", region);
    if (config.getString("message") != null) {
      String msg = config.getString("message");
      msg = msg.replaceAll("&", "§");
      toApply.addContent(new Comment("TODO: Translate from legacy."));
      toApply.addContent(
          new Element("message").setText(msg)
      );
    }
    return toApply;
  }

  private Element getWhoCheck(String who, boolean allow) {
    if (who.equals("*")) {
      return new Element(allow ? "always" : "never");
    } else {
      Element check = new Element("team").setText(who);
      if (allow) {
        return check;
      } else {
        return new Element("not").addContent(check);
      }
    }
  }

  private void move(String type, String region, Config config, Element root) {
    Element zone = getBaseElement(region, config);
    switch (type) {
      case "move":
        zone.addContent(new Element("enter")
            .addContent(getWhoCheck(config.getAsString("who"), config.getBoolean("allow", false))));
        zone.addContent(new Element("leave")
            .addContent(getWhoCheck(config.getAsString("who"), config.getBoolean("allow", false))));
        break;
      case "enter":
        zone.addContent(new Element("enter")
            .addContent(getWhoCheck(config.getAsString("who"), config.getBoolean("allow", false))));
        break;
      case "leave":
        zone.addContent(new Element("leave")
            .addContent(getWhoCheck(config.getAsString("who"), config.getBoolean("allow", false))));
        break;
    }

    root.addContent(zone);
  }

  private void build(String region, Config config, Element root) {
    Element zone = getBaseElement(region, config);

    zone.addContent(new Element("modify")
        .addContent(getWhoCheck(config.getAsString("who"), config.getBoolean("allow", false))));

    root.addContent(zone);
  }

  private void interact(String region, Config config, Element root) {
    Element zone = getBaseElement(region, config);

    zone.addContent(new Element("use")
        .addContent(getWhoCheck(config.getAsString("who"), config.getBoolean("allow", false))));

    root.addContent(zone);
  }

  private void killPlayer(String region, Config config, Element root) {
    root.addContent(new Comment("TODO: Implement kill_player"));
  }

  private void shootBow(String region, Config config, Element root) {
    root.addContent(new Comment("TODO: Implement shoot_bow"));

  }

  private void dropItem(String region, Config config, Element root) {
    root.addContent(new Comment("TODO: Implement drop_item"));
  }

  private void pickup(String region, Config config, Element root) {
    root.addContent(new Comment("TODO: Implement item_pickup"));
  }

  private void decay(String region, Config config, Element root) {
    root.addContent(new Comment("TODO: Implement decay"));
  }

  private void dispense(String region, Config config, Element root) {
    root.addContent(new Comment("TODO: Implement dispense"));
  }

  private void damage(String region, Config config, Element root) {
    root.addContent(new Comment("TODO: Implement damage"));
  }
}
