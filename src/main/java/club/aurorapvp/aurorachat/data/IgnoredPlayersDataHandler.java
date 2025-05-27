package club.aurorapvp.aurorachat.data;

import club.aurorapvp.aurorachat.AuroraChat;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class IgnoredPlayersDataHandler {

  private static final MongoCollection<Document> collection =
          AuroraChat.getInstance().getDatabase().getCollection("ignored_players");

  public static Set<UUID> getIgnoredPlayers(Player player) {
    String playerId = player.getUniqueId().toString();
    Document doc = collection.find(Filters.eq("_id", playerId)).first();

    if (doc == null || !doc.containsKey("ignored")) {
      return new HashSet<>();
    }

    List<String> ids = doc.getList("ignored", String.class);
    return ids.stream().map(UUID::fromString).collect(Collectors.toSet());
  }

  public static void setIgnoredPlayers(Player player, Set<UUID> ignored) {
    String playerId = player.getUniqueId().toString();

    List<String> ignoredStrings = ignored.stream()
            .map(UUID::toString)
            .collect(Collectors.toList());

    Document update = new Document("_id", playerId)
            .append("ignored", ignoredStrings);

    collection.updateOne(
            Filters.eq("_id", playerId),
            new Document("$set", update),
            new UpdateOptions().upsert(true)
    );
  }

  public static void addIgnoredPlayer(Player player, UUID toIgnore) {
    String playerId = player.getUniqueId().toString();
    collection.updateOne(
            Filters.eq("_id", playerId),
            Updates.addToSet("ignored", toIgnore.toString()),
            new UpdateOptions().upsert(true)
    );
  }

  public static void removeIgnoredPlayer(Player player, UUID toUnignore) {
    String playerId = player.getUniqueId().toString();
    collection.updateOne(
            Filters.eq("_id", playerId),
            Updates.pull("ignored", toUnignore.toString())
    );
  }
}