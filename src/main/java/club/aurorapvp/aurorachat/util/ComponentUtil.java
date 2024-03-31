package club.aurorapvp.aurorachat.util;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class ComponentUtil {

  public static Component createGradient(String text, List<TextColor> colors) {
    int numSegments = text.length();
    int numColors = colors.size();

    TextComponent.Builder gradientBuilder = Component.text();

    for (int i = 0; i < numSegments; i++) {
      double t = (double) i / (numSegments - 1);
      int colorIndex1 = (int) (t * (numColors - 1));
      int colorIndex2 = Math.min(colorIndex1 + 1, numColors - 1);

      TextColor interpolatedColor = lerpColor(colors.get(colorIndex1), colors.get(colorIndex2), t);

      gradientBuilder.append(Component.text(text.charAt(i)).color(interpolatedColor));
    }

    return gradientBuilder.build();
  }

  private static TextColor lerpColor(TextColor startColor, TextColor endColor, double t) {
    int red = (int) (startColor.red() + t * (endColor.red() - startColor.red()));
    int green = (int) (startColor.green() + t * (endColor.green() - startColor.green()));
    int blue = (int) (startColor.blue() + t * (endColor.blue() - startColor.blue()));

    return TextColor.color(red, green, blue);
  }
}
