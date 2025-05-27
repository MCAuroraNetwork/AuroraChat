package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import club.aurorapvp.aurorachat.data.IgnoredPlayersDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class IgnoredPlayers {
  public static HashMap<Player, Set<Player>> IGNORED_PLAYERS = new HashMap<>();

  public static void onJoin(PlayerJoinEvent event) {
    Set<UUID> ignoredPlayers = IgnoredPlayersDataHandler.getIgnoredPlayers(event.getPlayer());

    Player player = event.getPlayer();

    Set<Player> players = IGNORED_PLAYERS.get(player);

    for (UUID ignoredPlayer : ignoredPlayers) {
      players.add(Bukkit.getPlayer(ignoredPlayer));
    }

    ChatGroup.getChatGroup(event.getPlayer()).addDisallowedPlayers(players);
  }

  public static boolean isIgnoredPlayer(Player player, Player ignoredPlayer) {
    return IGNORED_PLAYERS.get(player).contains(ignoredPlayer);
  }

  public static void addIgnoredPlayer(Player player, Player ignoredPlayer) {
    IGNORED_PLAYERS.get(player).add(ignoredPlayer);

    ChatGroup.getChatGroup(player).addDisallowedPlayer(ignoredPlayer);

    IgnoredPlayersDataHandler.addIgnoredPlayer(player, ignoredPlayer.getUniqueId());

    player.sendMessage(AuroraChat.getInstance().getLang().getComponent("ignored-player"));
  }

  public static void removeIgnoredPlayer(Player player, Player ignoredPlayer) {
    IGNORED_PLAYERS.get(player).remove(ignoredPlayer);

    ChatGroup.getChatGroup(player).removeAllowedPlayer(ignoredPlayer);

    IgnoredPlayersDataHandler.removeIgnoredPlayer(player, ignoredPlayer.getUniqueId());

    player.sendMessage(AuroraChat.getInstance().getLang().getComponent("unignored-player"));
  }
}
