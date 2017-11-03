package me.austinlm.legacy.util.inventory;

import lombok.Getter;
import me.austinlm.legacy.XMLSerializable;
import org.jdom2.Element;

public class EffectType implements XMLSerializable {

  @Getter
  String type;
  @Getter
  int duration;
  @Getter
  int level;

  public static EffectType load(String raw) {
    String input = raw;

    input = input.toLowerCase();

    int duration = -1;
    int level = 1;

    if (input.contains(",")) {
      duration = Integer.valueOf(input.split(",")[1]) * 20;
      input = input.split(",")[0];
    }

    if (input.contains(":")) {
      level = Integer.valueOf(input.split(":")[1]) - 1;
      input = input.split(":")[0];
    }

    EffectType effect = new EffectType();
    effect.type = input;
    effect.level = level;
    effect.duration = duration;
    return effect;
  }

  @Override
  public void toXML(Element element) {
    Element effect = new Element("effect");
    effect.setText(this.getType());
    effect.setAttribute("duration",
        this.getDuration() == -1 ? "oo" : Integer.toString(this.getDuration()));
    if (this.getLevel() != 0) {
      effect.setAttribute("level", Integer.toString(this.getLevel()));
    }

    element.addContent(effect);
  }
}
