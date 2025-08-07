package club.aurorapvp.aurorachat.data;

import club.aurorapvp.aurorachat.AuroraChat;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.kyori.adventure.text.format.TextColor;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DisplayNameDataHandler {
  private final MongoCollection<Document> collection;
  private final String playerId;
  private Document playerData;
  private final UUID id = UUID.randomUUID();

  public DisplayNameDataHandler(Player player) {
    this.collection = AuroraChat.getInstance().getDatabase().getCollection("player_cosmetics");
    this.playerId = player.getUniqueId().toString();
    this.playerData = collection.find(Filters.eq("_id", playerId)).first();
    if (this.playerData == null) {
      this.playerData = new Document("_id", playerId);
    }
  }

  public UUID getId() {
    return id;
  }

  public boolean isPrefixEnabled() {
    return playerData.getBoolean("prefixEnabled", true);
  }

  public void setPrefixEnabled(boolean enabled) {
    playerData.put("prefixEnabled", enabled);
  }

  public boolean isSuffixEnabled() {
    return playerData.getBoolean("suffixEnabled", true);
  }

  public void setSuffixEnabled(boolean enabled) {
    playerData.put("suffixEnabled", enabled);
  }

  public List<List<TextColor>> getFrameDisplayNameColors() {
    if (!playerData.containsKey("frameDisplayNameColors")) {
      return new ArrayList<>();
    }
    List<?> rawList = playerData.getList("frameDisplayNameColors", List.class, new ArrayList<>());
    return rawList.stream()
        .map(
            list ->
                ((List<?>) list)
                    .stream()
                        .map(obj -> TextColor.fromHexString(obj.toString()))
                        .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  public void setFrameDisplayNameColors(List<List<TextColor>> colors) {
    List<List<String>> colorStrings =
        colors.stream()
            .map(list -> list.stream().map(TextColor::asHexString).collect(Collectors.toList()))
            .collect(Collectors.toList());
    playerData.put("frameDisplayNameColors", colorStrings);
  }

  public int getRefreshRate() {
      return playerData.getInteger("refreshRate", 10);
  }

  public void setRefreshRate(int rate) {
    int newRate = Math.max(rate, 1);
    playerData.put("refreshRate", newRate);
  }

  public void save() {
    collection.replaceOne(
        Filters.eq("_id", playerId),
        playerData,
        new com.mongodb.client.model.ReplaceOptions().upsert(true));
  }
}
