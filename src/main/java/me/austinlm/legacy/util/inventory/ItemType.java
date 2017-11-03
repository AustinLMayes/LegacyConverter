package me.austinlm.legacy.util.inventory;

import lombok.Getter;
import me.austinlm.legacy.XMLSerializable;
import org.jdom2.Element;

public class ItemType implements XMLSerializable {

  @Getter
  String material;
  @Getter
  int data;
  @Getter
  boolean dataSpecified;

  public ItemType(String type, int data) {
    this.material = type;
    this.data = data;
  }

  public ItemType() {

  }

  public static ItemType load(String raw) {
    String input = raw;
    ItemType result = new ItemType();

    input = input.toLowerCase();

    // Damage value
    if (input.contains(":")) {
      result.dataSpecified = true;
      result.data = Integer.valueOf(input.split(":")[1]);
      input = input.split(":")[0];
    }

    result.material = input;
    return result;
  }

  @Override
  public void toXML(Element element) {
    element.setText(
        this.getMaterial() + ":" + (this.isDataSpecified() ? Integer.toString(this.getData())
            : ""));
  }
}
