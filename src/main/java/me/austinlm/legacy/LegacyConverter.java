package me.austinlm.legacy;

import net.avicus.compendium.config.Config;
import org.jdom2.Element;

public interface LegacyConverter {

  void convert(Config config, Element root);
}
