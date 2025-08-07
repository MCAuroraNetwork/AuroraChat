package club.aurorapvp.aurorachat.modules;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.joml.Vector3f;

import java.util.List;

public class DisplayFrame {
    private final String text;
    private final Color backgroundColor;
    private final Vector3f scale;
    private final Vector3f offset;
    private final boolean shadowed;
    private final byte textOpacity;
    private final List<TextColor> displayNameColors;

    public DisplayFrame(String text, Color backgroundColor, Vector3f scale, Vector3f offset, boolean shadowed, byte textOpacity, List<TextColor> displayNameColors) {
        this.text = text;
        this.backgroundColor = backgroundColor;
        this.scale = scale;
        this.offset = offset;
        this.shadowed = shadowed;
        this.textOpacity = textOpacity;
        this.displayNameColors = displayNameColors;
    }

    public String text() { return text; }
    public Color backgroundColor() { return backgroundColor; }
    public Vector3f scale() { return scale; }
    public Vector3f offset() { return offset; }
    public boolean shadowed() { return shadowed; }
    public byte textOpacity() { return textOpacity; }
    public List<TextColor> displayNameColors() { return displayNameColors; }
}