package club.aurorapvp.aurorachat.modules;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;

public class TeamManager implements Listener {

  private static Team TEAM;

  public static void init() {
    TEAM = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("AuroraChat");

    if (TEAM == null) {
      TEAM = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("AuroraChat");
    }

    TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public static void onPlayerJoin(PlayerJoinEvent event) {
    TEAM.addPlayer(event.getPlayer());
  }
}
