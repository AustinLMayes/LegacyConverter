package me.austinlm.legacy.util.inventory;

import lombok.Getter;
import me.austinlm.legacy.XMLSerializable;
import org.jdom2.Element;

public class EnchantType implements XMLSerializable {

  @Getter
  String enchantment;
  @Getter
  int level;

  public static EnchantType load(String raw) {
    String input = raw;
    input = input.toLowerCase();

    int level = 1;

    if (input.contains(":")) {
      level = Integer.valueOf(input.split(":")[1]);
      input = input.split(":")[0];
    }

    EnchantType ench = new EnchantType();
    ench.enchantment = input;
    ench.level = level;
    return ench;
  }

  @Override
  public void toXML(Element element) {
    Element enchant = new Element("enchant");
    enchant.setText(this.getEnchantment());
    if (this.getLevel() != 1) {
      enchant.setAttribute("level", Integer.toString(this.getLevel()));
    }

    element.addContent(enchant);
  }
}
