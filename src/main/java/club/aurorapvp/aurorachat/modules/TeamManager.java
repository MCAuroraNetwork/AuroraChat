package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeamManager implements Listener {

  private static final Scoreboard SCOREBOARD = new Scoreboard();
  private static final PlayerTeam TEAM = new PlayerTeam(SCOREBOARD, "AuroraChat");

  private static final Set<String> PLAYERS = new HashSet<>();

  public static void init() {
    TEAM.setNameTagVisibility(PlayerTeam.Visibility.NEVER);

    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getLastSeen() > System.currentTimeMillis() - 604800000L) {
        PLAYERS.add(player.getName());
      }
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public static void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!PLAYERS.add(player.getName())) {
      Bukkit.getOnlinePlayers().forEach(onlinePlayer -> sendAddPlayerToTeam(player, onlinePlayer));
    }
    sendTeamCreatePacket(player);
    Bukkit.getScheduler()
        .runTaskAsynchronously(AuroraChat.getInstance(), () -> sendTeamMembers(player));
  }

  public static void sendTeamCreatePacket(Player target) {
    CraftPlayer craftPlayer = (CraftPlayer) target;
    ClientboundSetPlayerTeamPacket teamCreatePacket =
        ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(TEAM, true);
    craftPlayer.getHandle().connection.send(teamCreatePacket);
  }

  private static void sendAddPlayerToTeam(Player player, Player target) {
    CraftPlayer craftPlayer = (CraftPlayer) target;
    ClientboundSetPlayerTeamPacket packet =
        ClientboundSetPlayerTeamPacket.createPlayerPacket(
            TEAM, player.getName(), ClientboundSetPlayerTeamPacket.Action.ADD);
    craftPlayer.getHandle().connection.send(packet);
  }

  private static void sendTeamMembers(Player target) {
    ClientboundSetPlayerTeamPacket packet =
        ClientboundSetPlayerTeamPacket.createMultiplePlayerPacket(
            TEAM, PLAYERS, ClientboundSetPlayerTeamPacket.Action.ADD);
    CraftPlayer craftPlayer = (CraftPlayer) target;
    craftPlayer.getHandle().connection.send(packet);
  }
}
