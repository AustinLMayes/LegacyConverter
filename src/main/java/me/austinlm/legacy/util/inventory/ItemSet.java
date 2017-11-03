package me.austinlm.legacy.util.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import me.austinlm.legacy.XMLSerializable;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

public class ItemSet implements XMLSerializable {

  @Getter
  protected String type;
  @Getter
  protected int amount;
  @Getter
  protected int data;
  @Getter
  protected int minDamage = -1;
  @Getter
  protected int maxDamage = -1;
  @Getter
  protected HashMap<String, Integer> enchantments = new HashMap<String, Integer>();
  @Getter
  protected String title;
  @Getter
  protected List<String> lore = new ArrayList<String>();
  @Getter
  protected boolean glowing;
  @Getter
  protected String color;
  @Getter
  protected boolean unbreakable;
  @Getter
  protected boolean hideAttributes;

  public ItemSet() {

  }

  public ItemSet(String type) {
    this(type, 1);
  }

  public ItemSet(String type, int amount) {
    this(type, amount, 0);
  }

  public ItemSet(String type, int amount, int data) {
    this.type = type;
    this.amount = amount;
    this.data = data;
  }

  public static ItemSet load(Config input) {
    ItemSet set = new ItemSet();

    if (!input.contains("item")) {
      throw new RuntimeException("Item type not specified");
    }

    String item = input.get("item") + "";
    ItemType type = ItemType.load(item);

    set.type = type.getMaterial();
    set.data = type.getData();

    set.amount = input.getInt("amount", 1);
    set.glowing = input.getBoolean("glow", false);
    set.unbreakable = input.getBoolean("unbreakable", false);

    if (input.contains("enchants") || input.contains("enchant") || input.contains("enchantments")) {
      for (String raw : input.getStringList("enchants")) {
        EnchantType enchant = EnchantType.load(raw);
        set.enchantments.put(enchant.getEnchantment(), enchant.getLevel());
      }
    }

    if (input.contains("damage")) {
      String raw = input.getString("damage");
      if (raw.contains("-")) {
        set.minDamage = Integer.valueOf(raw.split("-")[0]);
        set.maxDamage = Integer.valueOf(raw.split("-")[1]);
      } else {
        set.minDamage = Integer.valueOf(raw);
        set.maxDamage = Integer.valueOf(raw);
      }
    }

    if (input.contains("title")) {
      set.title = input.getString("title").replaceAll("&", "§");
    }

    if (input.contains("lore")) {
      for (String line : input.getStringList("lore")) {
        set.lore.add(line.replaceAll("&", "§"));
      }
    }

    if (input.contains("color")) {
      set.color = input.getString("color");
    }

    return set;
  }

  @Override
  public void toXML(Element element) {
    element.setAttribute("material", this.getType());
    if (this.amount != 1) {
      element.setAttribute("amount", Integer.toString(this.amount));
    }

    if (this.data != 0) {
      element.setAttribute("damage", Integer.toString(this.data));
    } else if (this.minDamage != -1) {
      element.setAttribute("damage", Integer.toString(this.minDamage));
    }

    if (this.unbreakable) {
      element.setAttribute("unbreakable", "true");
    }

    if (this.title != null) {
      element.setAttribute("name", this.title);
    }

    if (!this.lore.isEmpty()) {
      Element lore = new Element("lore");
      this.lore.forEach(l -> lore.addContent(new Element("line").setText(l)));
      element.addContent(lore);
    }

    if (this.color != null) {
      element.setAttribute("color", this.color);
    }

    if (!this.enchantments.isEmpty()) {
      Element effects = new Element("enchantments");
      this.enchantments.forEach((e, l) -> effects.addContent(
          new Element("enchantment").setText(e).setAttribute("level", Integer.toString(l))));

      element.addContent(effects);
    }
  }
}