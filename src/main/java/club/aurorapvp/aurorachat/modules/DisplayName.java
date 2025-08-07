package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.data.DisplayNameDataHandler;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisplayName {
    private static final Map<UUID, DisplayName> displayNames = new HashMap<>();
    private final DisplayNameDataHandler dataHandler;
    private List<List<TextColor>> frameColors;
    private int refreshRate;
    private boolean prefixEnabled;
    private boolean suffixEnabled;
    private int currentFrameIndex = 0;

    private DisplayName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            throw new IllegalArgumentException("Player not found for UUID: " + uuid);
        }
        this.dataHandler = new DisplayNameDataHandler(player);
        loadData();
    }

    public static DisplayName getDisplayName(UUID uuid) {
        return displayNames.computeIfAbsent(uuid, DisplayName::new);
    }

    private void loadData() {
        this.frameColors = dataHandler.getFrameDisplayNameColors();
        this.refreshRate = dataHandler.getRefreshRate();
        this.prefixEnabled = dataHandler.isPrefixEnabled();
        this.suffixEnabled = dataHandler.isSuffixEnabled();
    }

    public void save() {
        dataHandler.setFrameDisplayNameColors(frameColors);
        dataHandler.setRefreshRate(refreshRate);
        dataHandler.setPrefixEnabled(prefixEnabled);
        dataHandler.setSuffixEnabled(suffixEnabled);
        dataHandler.save();
    }

    public List<List<TextColor>> getFrameColors() {
        return new ArrayList<>(frameColors);
    }

    public void setFrameColors(List<List<TextColor>> colors) {
        this.frameColors = new ArrayList<>(colors);
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int rate) {
        this.refreshRate = Math.max(rate, 1);
    }

    public boolean isPrefixEnabled() {
        return prefixEnabled;
    }

    public void setPrefixEnabled(boolean enabled) {
        this.prefixEnabled = enabled;
    }

    public boolean isSuffixEnabled() {
        return suffixEnabled;
    }

    public void setSuffixEnabled(boolean enabled) {
        this.suffixEnabled = enabled;
    }

    public void advanceFrame() {
        if (!frameColors.isEmpty()) {
            currentFrameIndex = (currentFrameIndex + 1) % frameColors.size();
        }
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }
}