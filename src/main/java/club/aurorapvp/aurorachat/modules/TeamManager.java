package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeamManager implements Listener {

  private static final PlayerTeam team = new PlayerTeam(new Scoreboard(), "AuroraChat");

  private final Set<String> players = new HashSet<>();

  public static void init() {
    team.setNameTagVisibility(PlayerTeam.Visibility.NEVER);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    if (!players.add(player.getName())) {
      Bukkit.getOnlinePlayers().forEach(onlinePlayer -> sendAddPlayerToTeam(player, onlinePlayer));
    }

    CraftPlayer craftPlayer = (CraftPlayer) player;
    ClientboundSetPlayerTeamPacket teamCreatePacket =
        ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true);

    craftPlayer.getHandle().connection.send(teamCreatePacket);

    Bukkit.getScheduler()
        .runTaskAsynchronously(AuroraChat.getInstance(), () -> sendTeamMembers(player));
  }

  private void sendAddPlayerToTeam(Player player, Player target) {
    ClientboundSetPlayerTeamPacket packet =
        ClientboundSetPlayerTeamPacket.createPlayerPacket(
            team, player.getName(), ClientboundSetPlayerTeamPacket.Action.ADD);

    ((CraftPlayer) target).getHandle().connection.send(packet);
  }

  private void sendTeamMembers(Player target) {
    ClientboundSetPlayerTeamPacket packet =
        ClientboundSetPlayerTeamPacket.createMultiplePlayerPacket(
            team, players, ClientboundSetPlayerTeamPacket.Action.ADD);

    ((CraftPlayer) target).getHandle().connection.send(packet);
  }
}
