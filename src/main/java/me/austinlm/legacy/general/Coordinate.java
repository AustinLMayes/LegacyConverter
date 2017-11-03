package me.austinlm.legacy.general;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.austinlm.legacy.XMLSerializable;
import org.jdom2.Element;

@ToString
public class Coordinate implements XMLSerializable {

  @Getter
  @Setter
  double x;
  @Getter
  @Setter
  double y;
  @Getter
  @Setter
  double z;
  @Getter
  @Setter
  float yaw;
  @Getter
  @Setter
  float pitch;

  public Coordinate(double x, double y, double z, float yaw, float pitch) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public Coordinate(String input) {
    String[] parts = input.replaceAll(" ", "").split(",");
    if (parts.length < 3 || parts.length > 5) {
      throw new RuntimeException("Invalid arguments provided.");
    }

    x = Double.parseDouble(parts[0]);
    y = Double.parseDouble(parts[1]);
    z = Double.parseDouble(parts[2]);

    if (parts.length > 3) {
      yaw = Float.parseFloat(parts[3]);
    }
    if (parts.length > 4) {
      pitch = Float.parseFloat(parts[4]);
    }
  }

  @Override
  public void toXML(Element element) {
    element.setText(this.getX() + ", " + this.getY() + ", " + this.getZ());
    if (this.getYaw() != 0) {
      element.setAttribute("yaw", Float.toString(this.getYaw()).split("\\.")[0]);
    }
    if (this.getPitch() != 0) {
      element.setAttribute("pitch", Float.toString(this.getPitch()).split("\\.")[0]);
    }
  }

  public String toXML(boolean decimal) {
    if (decimal) {
      return this.getX() + ", " + this.getY() + ", " + this.getZ();
    } else {
      return stripDecimal(Double.toString(this.getX())) + ", " + stripDecimal(
          Double.toString(this.getY())) + ", " + stripDecimal(Double.toString(this.getZ()));
    }
  }

  private String stripDecimal(String s) {
    return s.split("\\.")[0];
  }
}
