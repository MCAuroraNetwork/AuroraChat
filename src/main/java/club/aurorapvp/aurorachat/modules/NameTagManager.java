package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.purpurmc.purpur.event.entity.EntityTeleportHinderedEvent;

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

  public static void onPlayerTeleportHindered(EntityTeleportHinderedEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }

    if (event.getReason() != EntityTeleportHinderedEvent.Reason.IS_VEHICLE) {
      return;
    }

    NameTag nameplate = NAMETAGS.get(player.getUniqueId());

    if (nameplate == null) {
      return;
    }

    nameplate.remove();
    event.setShouldRetry(true);
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
