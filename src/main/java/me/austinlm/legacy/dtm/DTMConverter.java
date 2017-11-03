package me.austinlm.legacy.dtm;

import java.util.Map;
import me.austinlm.legacy.LegacyConverter;
import me.austinlm.legacy.util.Coordinate;
import me.austinlm.legacy.general.GeneralConverter;
import me.austinlm.legacy.general.Loadout;
import me.austinlm.legacy.general.TeamConverter;
import me.austinlm.legacy.util.XmlUtils;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

public class DTMConverter implements LegacyConverter {

  @Override
  public void convert(Config config, Element root) {
    if (config.getConfig("info").get("type", String.class).equalsIgnoreCase("dtm")) {
      new GeneralConverter().convert(config, root);

      Element objectives = XmlUtils.getOrCreate(root, "objectives");
      Element monuments = new Element("monuments");
      monuments.setAttribute("materials", "obsidian");
      new TeamConverter((c, i) -> {
        Element teamMons = new Element("monuments");
        teamMons.setAttribute("owner", i);
        c.getConfig("monuments").getData().values().forEach(v -> {
          Config mon = new Config((Map<Object, Object>) v);
          String title = mon.getString("name");
          Coordinate coord = new Coordinate(mon.getString("location"));
          Element monument = new Element("monument");
          monument.setAttribute("name", title);
          Element region = new Element("region");
          Element block = new Element("block");
          block.setText(coord.toXML(false));

          region.addContent(block);
          monument.addContent(region);
          teamMons.addContent(monument);
        });

        monuments.addContent(teamMons);
      }, "default").convert(config.getConfig("teams"), root);
      objectives.addContent(monuments);
      Element loadouts = new Element("loadouts");
      new Loadout("default", config.getConfig("loadout")).toXML(loadouts);
      root.addContent(loadouts);
    }
  }
}






