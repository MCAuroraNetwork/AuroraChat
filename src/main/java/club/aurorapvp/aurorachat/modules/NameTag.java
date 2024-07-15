package club.aurorapvp.aurorachat.modules;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NameTag {

  private static final Map<Player, NameTag> NAME_TAGS = new HashMap<>();
  private static Team team;
  private final NameColor color;
  private final Entity nametag;

  public NameTag(Player player, NameColor color) {
    this.color = color;
    this.nametag = player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

    team.addPlayer(player);
    nametag.setCustomNameVisible(true);
    nametag.setGravity(false);
    nametag.setNoPhysics(true);
    nametag.setInvisible(true);
    nametag.setSilent(true);
    nametag.setInvulnerable(true);
    nametag.customName(color.getDisplayName());



    NAME_TAGS.put(player, this);
  }

  public static void init() {
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    team = scoreboard.getTeam("AuroraChat");

    if (team == null) {
      team = scoreboard.registerNewTeam("AuroraChat");
    }

    team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
  }

  public void update() {
    nametag.customName(color.getDisplayName());
  }

  public void onMove(PlayerMoveEvent event) {
    nametag.teleportAsync(event.getPlayer().getLocation());
  }

  public void onSneak(PlayerToggleSneakEvent event) {
    nametag.setSneaking(event.isSneaking());
  }

  public static void remove(Player player) {
    NAME_TAGS.remove(player).nametag.remove();
  }

  public static NameTag getNameTag(Player player) {
    return NAME_TAGS.get(player);
  }
}
