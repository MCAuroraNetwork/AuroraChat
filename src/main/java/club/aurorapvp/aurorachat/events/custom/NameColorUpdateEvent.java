package club.aurorapvp.aurorachat.events.custom;

import java.util.Collection;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NameColorUpdateEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();
  private final Player player;
  private final Collection<TextColor> newColors;

  private NameColorUpdateEvent(Player player, Collection<TextColor> newColors) {
    this.player = player;
    this.newColors = newColors;
  }

  public Player getPlayer() {
    return player;
  }

  public Collection<TextColor> getNewColors() {
    return newColors;
  }

  @SuppressWarnings("unused")
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

}
