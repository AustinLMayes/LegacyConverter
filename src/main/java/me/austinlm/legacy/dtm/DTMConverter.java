package me.austinlm.legacy.dtm;

import me.austinlm.legacy.LegacyConverter;
import me.austinlm.legacy.general.GeneralConverter;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

public class DTMConverter implements LegacyConverter {

  @Override
  public void convert(Config config, Element root) {
    if (config.getConfig("info").get("type", String.class).equalsIgnoreCase("dtm")) {
      new GeneralConverter().convert(config, root);
    }
  }
}






