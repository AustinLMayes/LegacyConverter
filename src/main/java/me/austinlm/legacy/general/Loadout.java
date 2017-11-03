package me.austinlm.legacy.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.austinlm.legacy.XMLSerializable;
import me.austinlm.legacy.util.inventory.EffectType;
import me.austinlm.legacy.util.inventory.ItemSet;
import net.avicus.compendium.config.Config;
import org.jdom2.Element;

@ToString
public class Loadout implements XMLSerializable {

  @Getter
  String name;

  @Getter
  @Setter
  HashMap<Integer, ItemSet> items = new HashMap<Integer, ItemSet>();
  @Getter
  @Setter
  HashMap<String, ItemSet> armor = new HashMap<>();
  @Getter
  @Setter
  List<EffectType> effects = new ArrayList<EffectType>();
  @Getter
  @Setter
  float walkSpeed = 1.0F;
  @Getter
  @Setter
  float flySpeed = 1.0F;
  @Getter
  @Setter
  boolean flying;
  @Getter
  @Setter
  String disguise;

  public Loadout(String name) {
    this.name = name;
  }

  public Loadout(String name, Config input) {
    this.name = name;

    for (Map.Entry<Object, Object> data : input.getData().entrySet()) {
      String key = data.getKey().toString();
      if (key.equalsIgnoreCase("effects")) {
        List<String> list = input.getStringList("effects");
        for (String raw : list) {
          addEffect(EffectType.load(raw));
        }
      } else if (key.equalsIgnoreCase("walk-speed")) {
        setWalkSpeed(Float.parseFloat(input.get("walk-speed") + ""));
      } else if (key.equalsIgnoreCase("disguise")) {
        setDisguise(input.getString("disguise"));
      } else {
        Config item = new Config((HashMap<Object, Object>) data.getValue());
        ItemSet itemSet = ItemSet.load(item);

        try {
          int index = Integer.valueOf(key);
          addItem(itemSet, index);
        } catch (Exception e) {
          addArmor(key, itemSet);
        }
      }
    }
  }

  public Loadout addItem(ItemSet item, int index) {
    items.put(index, item);
    return this;
  }

  public void addArmor(String where, ItemSet piece) {
    armor.put(where, piece);
  }

  public void addEffect(EffectType effect) {
    effects.add(effect);
  }

  @Override
  public void toXML(Element element) {
    Element loadout = new Element("loadout");
    loadout.setAttribute("id", this.name);
    this.items.forEach((s, i) -> {
      Element item = new Element("item");
      item.setAttribute("slot", Integer.toString(s));
      i.toXML(item);
      loadout.addContent(item);
    });

    this.armor.forEach((w, i) -> {
      Element armor = new Element(w);
      if (i.getType().contains("leather")) {
        armor.setAttribute("team-color", "true");
      }

      i.toXML(armor);

      loadout.addContent(armor);
    });

    this.effects.forEach(e -> {
      e.toXML(loadout);
    });

    element.addContent(loadout);
  }
}
