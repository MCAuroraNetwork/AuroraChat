package club.aurorapvp.aurorachat.util;

import java.util.List;
import java.util.stream.Stream;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.Index;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

public final class ExtendedTextColor implements TextColor {
  private static final int WHITE_VALUE = 0xFFFFFF;
  private static final int ORANGE_VALUE = 0xF9801D;
  private static final int MAGENTA_VALUE = 0xC74EBD;
  private static final int LIGHT_BLUE_VALUE = 0x3AB3DA;
  private static final int YELLOW_VALUE = 0xFED83D;
  private static final int LIME_VALUE = 0x80C71F;
  private static final int PINK_VALUE = 0xF38BAA;
  private static final int GRAY_VALUE = 0x474F52;
  private static final int LIGHT_GRAY_VALUE = 0x9D9D97;
  private static final int CYAN_VALUE = 0x169C9C;
  private static final int PURPLE_VALUE = 0x8932B8;
  private static final int BLUE_VALUE = 0x3C44AA;
  private static final int BROWN_VALUE = 0x835432;
  private static final int GREEN_VALUE = 0x5E7C16;
  private static final int RED_VALUE = 0xB02E26;
  private static final int BLACK_VALUE = 0x1D1D21;

  public static final ExtendedTextColor WHITE = new ExtendedTextColor("white", WHITE_VALUE);
  public static final ExtendedTextColor ORANGE = new ExtendedTextColor("orange", ORANGE_VALUE);
  public static final ExtendedTextColor MAGENTA = new ExtendedTextColor("magenta", MAGENTA_VALUE);
  public static final ExtendedTextColor LIGHT_BLUE =
      new ExtendedTextColor("light_blue", LIGHT_BLUE_VALUE);
  public static final ExtendedTextColor YELLOW = new ExtendedTextColor("yellow", YELLOW_VALUE);
  public static final ExtendedTextColor LIME = new ExtendedTextColor("lime", LIME_VALUE);
  public static final ExtendedTextColor PINK = new ExtendedTextColor("pink", PINK_VALUE);
  public static final ExtendedTextColor GRAY = new ExtendedTextColor("gray", GRAY_VALUE);
  public static final ExtendedTextColor LIGHT_GRAY =
      new ExtendedTextColor("light_gray", LIGHT_GRAY_VALUE);
  public static final ExtendedTextColor CYAN = new ExtendedTextColor("cyan", CYAN_VALUE);
  public static final ExtendedTextColor PURPLE = new ExtendedTextColor("purple", PURPLE_VALUE);
  public static final ExtendedTextColor BLUE = new ExtendedTextColor("blue", BLUE_VALUE);
  public static final ExtendedTextColor BROWN = new ExtendedTextColor("brown", BROWN_VALUE);
  public static final ExtendedTextColor GREEN = new ExtendedTextColor("green", GREEN_VALUE);
  public static final ExtendedTextColor RED = new ExtendedTextColor("red", RED_VALUE);
  public static final ExtendedTextColor BLACK = new ExtendedTextColor("black", BLACK_VALUE);

  private static final List<ExtendedTextColor> VALUES =
      List.of(
          WHITE,
          ORANGE,
          MAGENTA,
          LIGHT_BLUE,
          YELLOW,
          LIME,
          PINK,
          GRAY,
          LIGHT_GRAY,
          CYAN,
          PURPLE,
          BLUE,
          BROWN,
          GREEN,
          RED,
          BLACK);

  public static final Index<String, ExtendedTextColor> NAMES =
      Index.create(ExtendedTextColor::name, VALUES);

  private final String name;
  private final int value;
  private final HSVLike hsv;

  private ExtendedTextColor(final String name, final int value) {
    this.name = name;
    this.value = value;
    this.hsv = HSVLike.fromRGB(this.red(), this.green(), this.blue());
  }

  @Override
  public int value() {
    return this.value;
  }

  @Override
  public @NotNull HSVLike asHSV() {
    return this.hsv;
  }

  public String name() {
    return this.name;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
        Stream.of(ExaminableProperty.of("name", this.name)),
        TextColor.super.examinableProperties());
  }
}
