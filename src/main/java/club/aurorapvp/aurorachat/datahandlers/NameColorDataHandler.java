package club.aurorapvp.aurorachat.datahandlers;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.modules.NameColor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.kyori.adventure.text.format.TextColor;
import org.bson.Document;

public class NameColorDataHandler {

  private final MongoCollection<Document> collection;
  private final NameColor color;
  private final String playerId;

  public NameColorDataHandler(NameColor color) {
    this.color = color;
    this.collection = AuroraChat.getInstance().getDatabase().getCollection("name_colors");
    this.playerId = color.getPlayer().getUniqueId().toString();
  }

  public List<String> getColorCodes() {
    Document playerData = collection.find(Filters.eq("_id", playerId)).first();

    if (playerData == null || !playerData.containsKey("colorCodes")) {
      return List.of("WHITE");
    }

    String colorCodes = playerData.getString("colorCodes");
    return Arrays.asList(colorCodes.split(","));
  }

  public void save() {
    String colorCodes =
        color.getColors().stream().map(TextColor::asHexString).collect(Collectors.joining(","));

    Document playerData = new Document().append("_id", playerId).append("colorCodes", colorCodes);

    collection.updateOne(
        Filters.eq("_id", playerId),
        new Document("$set", playerData),
        new com.mongodb.client.model.UpdateOptions().upsert(true));
  }

  public boolean exists() {
    return collection.find(Filters.eq("_id", playerId)).first() != null;
  }
}