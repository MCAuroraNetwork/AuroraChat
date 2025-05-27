package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.data.IgnoredPlayersDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class IgnoredPlayers {
  public static HashMap<UUID, Set<Player>> IGNORED_PLAYERS = new HashMap<>();

  public static void onJoin(PlayerJoinEvent event) {
    Set<UUID> ignoredPlayers = IgnoredPlayersDataHandler.getIgnoredPlayers(event.getPlayer());

    Player player = event.getPlayer();

    Set<Player> players = IGNORED_PLAYERS.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());

    for (UUID ignoredPlayer : ignoredPlayers) {
      players.add(Bukkit.getPlayer(ignoredPlayer));
    }

    ChatGroup.getChatGroup(event.getPlayer()).addDisallowedPlayers(players);
  }

  public static Set<Player> getIgnoredPlayers(Player player) {
    return IGNORED_PLAYERS.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
  }

  public static boolean isIgnoredPlayer(Player player, Player ignoredPlayer) {
    return getIgnoredPlayers(player).contains(ignoredPlayer);
  }

  public static void addIgnoredPlayer(Player player, Player ignoredPlayer) {
    getIgnoredPlayers(player).add(ignoredPlayer);

    ChatGroup.getChatGroup(player).addDisallowedPlayer(ignoredPlayer);

    IgnoredPlayersDataHandler.addIgnoredPlayer(player, ignoredPlayer.getUniqueId());

    player.sendMessage(AuroraChat.getInstance().getLang().getComponent("player-ignored"));
  }

  public static void removeIgnoredPlayer(Player player, Player ignoredPlayer) {
    getIgnoredPlayers(player).remove(ignoredPlayer);

    ChatGroup.getChatGroup(player).removeDisallowedPlayer(ignoredPlayer);

    IgnoredPlayersDataHandler.removeIgnoredPlayer(player, ignoredPlayer.getUniqueId());

    player.sendMessage(AuroraChat.getInstance().getLang().getComponent("player-unignored"));
  }
}
