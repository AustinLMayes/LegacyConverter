package me.austinlm.legacy.general;

import java.util.ArrayList;
import java.util.List;
import me.austinlm.legacy.LegacyConverter;
import me.austinlm.legacy.RegionUtils;
import me.austinlm.legacy.XmlUtils;
import me.austinlm.legacy.region.RegionConverter;
import net.avicus.compendium.config.Config;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Element;

public class GeneralConverter implements LegacyConverter {

  @Override
  public void convert(Config config, Element root) {
    Config info = config.getConfig("info");

    generalInfo(root, info);
    authors(root, info);
    obsSpawn(root, info);
    checkDuration(root, info);
    Element regions = new Element("regions");
    new RegionConverter(root).convert(config.getConfig("regions"), regions);
    root.addContent(regions);
  }

  private String typeToGenre(String type) {
    switch (type) {
      case "dtm":
        return "nebula";
    }

    throw new RuntimeException("Unknown map type: " + type);
  }

  private void generalInfo(Element root, Config info) {
    root.setAttribute("spec", "1.0.5");
    root.setAttribute("name", info.getString("title"));
    root.setAttribute("version", info.getAsString("version") + ".0");
    root.setAttribute("genre", typeToGenre(info.getString("type")));
    root.addContent(new Element("gametype").setText(info.getString("type")));
    if (info.getAsString("time") != null) {
      root.addContent(new Comment(
          "Legacy versions supported setting specific world time, we'll just lock it for now."));
      root.addContent(
          new Element("world")
              .addContent(
                  new Element("gamerules")
                      .addContent(
                          new Element("doDaylightCycle").setText("false")
                      )
              )
      );
    }
  }

  private void checkDuration(Element root, Config info) {
    if (info.getAsString("duration") != null) {
      String duration = info.getAsString("duration");
      root.addContent(
          new Element("results").addContent(
              new Element("win")
                  .setAttribute("scenario", "objectives")
                  .addContent(
                      new Element("check")
                          .addContent(
                              new Element("time")
                                  .setText(duration)
                          )
                  )
          )
      );
    }
  }

  private void authors(Element root, Config info) {
    Element authorRoot = new Element("authors");

    List<Content> authors = new ArrayList<>();
    if (info.contains("creators")) {
      for (String username : info.getStringList("creators")) {
        Comment comment = new Comment(username);
        authors.add(comment);
        Element el = new Element("author");
        el.setAttribute("uuid", "");
        authors.add(el);
      }
    } else if (info.contains("authors")) {
      for (Config cs : info.getList("authors", Config.class)) {
        String uuid = cs.getString("uuid");
        String role = cs.getString("role");
        Element el = new Element("author");
        el.setAttribute("uuid", uuid);
        el.setAttribute("role", role);
        authors.add(el);
      }
    }

    authorRoot.addContent(authors);
    root.addContent(authorRoot);
  }

  private void obsSpawn(Element root, Config info) {
    Element spawns = XmlUtils.getOrCreate(root, "spawns");
    Element obs = new Element("spawn");

    String spawn = info.getAsString("spawn");
    if (spawn.contains(",")) {
      Element block = new Element("block");
      new Coordinate(spawn).toXML(block);
      obs.addContent(block);
    } else {
      RegionUtils.regionRef(obs, spawn);
    }

    spawns.addContent(obs);
  }
}