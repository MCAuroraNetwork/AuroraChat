package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.BoundingBox;

public class NameTagManager {

  private static final HashMap<UUID, NameTag> NAMETAGS = new HashMap<>();
  private static long time = Long.MIN_VALUE;

  public static void init() {
    reloadNameTags();
    Bukkit.getScheduler()
        .runTaskTimer(
            AuroraChat.getInstance(), () -> NAMETAGS.values().forEach(NameTag::update), 0, 1);

    Bukkit.getScheduler()
        .runTaskTimer(
            AuroraChat.getInstance(),
            () ->
                Bukkit.getWorlds()
                    .forEach(
                        world ->
                            world
                                .getEntities()
                                .forEach(
                                    entity -> {
                                      if (!(entity instanceof TextDisplay textDisplay)
                                          || !textDisplay
                                              .getPersistentDataContainer()
                                              .has(NameTag.KEY)) {
                                        return;
                                      }

                                      for (NameTag nametag : NAMETAGS.values()) {
                                        if (nametag.getEntity() == textDisplay) {
                                          return;
                                        }
                                      }
                                      textDisplay.remove();
                                    })),
            100,
            100);

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            AuroraChat.getInstance(),
            () -> {
              time++;

              NAMETAGS
                  .values()
                  .forEach(
                      nameTag -> {
                        DisplayContent content = nameTag.getContent();

                        if (content == null
                            || content.getRefreshRate() <= 0
                            || time % content.getRefreshRate() != 0) {
                          return;
                        }

                        content.advanceFrame();
                      });
            },
            1,
            1);
  }

  public static void onPlayerJoin(PlayerJoinEvent event) {
    UUID joinerUuid = event.getPlayer().getUniqueId();
    DisplayContent displayContent = DisplayContent.createDisplayContent();
    if (displayContent == null) {
      return;
    }

    NAMETAGS.put(joinerUuid, new NameTag(joinerUuid, displayContent));
  }

  public static void onPlayerTeleport(PlayerTeleportEvent event) {
    NameTag nametag = NAMETAGS.get(event.getPlayer().getUniqueId());
    if (nametag == null) {
      return;
    }

    nametag.remove();
  }

  public static void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();

    if (!event.hasChangedPosition()) {
      return;
    }

    NameTag nametag = NAMETAGS.get(event.getPlayer().getUniqueId());

    if (nametag == null) {
      return;
    }

    BoundingBox box = player.getBoundingBox();

    World world = player.getWorld();
    Location loc1 = new Location(world, box.getMaxX(), box.getMaxY(), box.getMaxZ());
    Location loc2 = loc1.clone().subtract(0, 1, 0);
    Location loc3 = new Location(world, box.getMinX(), box.getMinY(), box.getMinZ());
    Location loc4 = loc3.clone().add(0, 1, 0);

    boolean inPortal = false;
    for (Location loc : new Location[] {loc1, loc2, loc3, loc4}) {
      Block block = loc.getBlock();
      if (block.getType() == Material.NETHER_PORTAL
          || block.getType() == Material.END_PORTAL
          || block.getType() == Material.END_GATEWAY) {
        inPortal = true;
        break;
      }
    }

    if (inPortal) {
      nametag.remove();
      nametag.hide = true;
      return;
    }

    nametag.hide = false;
  }

  public static void reloadNameTags() {
    NAMETAGS.values().forEach(NameTag::remove);
    NAMETAGS.clear();
    for (Player player : Bukkit.getOnlinePlayers()) {
      NAMETAGS.put(
          player.getUniqueId(),
          new NameTag(player.getUniqueId(), DisplayContent.createDisplayContent()));
    }
  }
}
