package me.austinlm.legacy.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import me.austinlm.legacy.LegacyConverter;
import me.austinlm.legacy.util.Coordinate;
import me.austinlm.legacy.util.XmlUtils;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

public class TeamConverter implements LegacyConverter {

  private final BiConsumer<Config, String> nestedInfoConsumer;
  private final String loadout;

  public TeamConverter(
      BiConsumer<Config, String> nestedInfoConsumer, String loadout) {
    this.nestedInfoConsumer = nestedInfoConsumer;
    this.loadout = loadout;
  }

  @Override
  public void convert(Config config, Element root) {
    Element teams = XmlUtils.getOrCreate(root, "teams");
    config.getData().forEach((k, v) -> {
      Element team = new Element("team");
      Config teamConfig = new Config((HashMap<Object, Object>) v);
      String color = k.toString();
      String title = teamConfig.getString("title");
      int max = teamConfig.getInt("max", 1);
      int overfill = teamConfig.getInt("hard-max", (int) Math.floor((double) max * 1.25));
      int min = teamConfig.getInt("min", (int) Math.floor((double) max * 0.15));
      team.setAttribute("id", color);
      team.setAttribute("color", color);
      team.setAttribute("min", Integer.toString(min));
      team.setAttribute("max", Integer.toString(max));
      team.setAttribute("max-overfill", Integer.toString(overfill));
      team.setText(title);
      spawn(root, teamConfig, color, loadout);
      this.nestedInfoConsumer.accept(teamConfig, color);
      teams.addContent(team);
    });
  }

  private void spawn(Element root, Config team, String name, String loadout) {
    Element spawns = XmlUtils.getOrCreate(root, "spawns");
    Element tSpawn = new Element("spawn");
    tSpawn.setAttribute("team", name);
    if (loadout != null) {
      tSpawn.setAttribute("loadout", loadout);
    }

    Object ob = team.get("spawn");
    if (ob == null) {
      ob = team.get("spawns");
    }

    List<String> raw = new ArrayList<String>();
    if (ob instanceof String) {
      raw.add((String) ob);
    } else if (ob instanceof List<?>) {
      for (Object item : (List<?>) ob) {
        raw.add(item.toString());
      }
    }

    if (raw.isEmpty()) {
      return;
    }

    Element regions = new Element("regions");
    for (String item : raw) {
      regions.addContent(parseCoord(item));
    }
    tSpawn.addContent(regions);


    spawns.addContent(tSpawn);
  }

  private Element parseCoord(String item) {
    Element element = null;
    if (item.contains(",")) {
      element = new Element("point");
      new Coordinate(item).toXML(element);
    } else {
      element = new Element("region");
      element.setAttribute("id", item);
    }

    return element;
  }
}
