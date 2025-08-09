package club.aurorapvp.aurorachat.modules;

import club.aurorapvp.aurorachat.AuroraChat;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatGroup {
  public static Map<UUID, ChatGroup> chatGroups = new HashMap<>();
  private final Set<Player> allowedPlayers = new HashSet<>();
  private final Set<Player> disallowedPlayers = new HashSet<>();
  private final Player player;
  private ChatMode mode;

  public ChatGroup(Player player) {
    this.player = player;
    this.mode = ChatMode.ALL;
  }

  public static void checkDisabled(AsyncChatEvent event) {
    ChatGroup chatGroup = chatGroups.get(event.getPlayer().getUniqueId());

    if (chatGroup == null || chatGroup.getMode() != ChatMode.DISABLED) {
      return;
    }

    event.getPlayer().sendMessage(AuroraChat.getInstance().getLang().getComponent("chat-disabled"));

    event.setCancelled(true);
  }

  public static void onDiscordChat(GameChatMessagePreProcessEvent event) {
    ChatGroup chatGroup = ChatGroup.getChatGroup(event.getPlayer());

    if (chatGroup.getMode() != ChatMode.ALL) {
      event.setCancelled(true);
    }
  }

  public static void onChat(AsyncChatEvent event) {
    Player sender = event.getPlayer();
    ChatGroup senderGroup = getChatGroup(sender);
    Set<Player> allowedRecipients = new HashSet<>();

    for (Player receiver : Bukkit.getOnlinePlayers()) {
      if (receiver.equals(sender)) {
        allowedRecipients.add(receiver);
        continue;
      }

      ChatGroup receiverGroup = getChatGroup(receiver);

      boolean receiverAllowsSender = false;
      if (receiverGroup.getMode() == ChatMode.ALL && !receiverGroup.getDisallowedPlayers().contains(sender)) {
        receiverAllowsSender = true;
      } else if (receiverGroup.getMode() != ChatMode.DISABLED
              && receiverGroup.getAllowedPlayers().contains(sender)
              && !receiverGroup.getDisallowedPlayers().contains(sender)) {
        receiverAllowsSender = true;
      }

      boolean senderAllowsReceiver = true;
      if (senderGroup.getMode() == ChatMode.SELECT) {
        senderAllowsReceiver = senderGroup.getAllowedPlayers().contains(receiver);
      }

      if (receiverAllowsSender && senderAllowsReceiver) {
        allowedRecipients.add(receiver);
      }
    }

    event.viewers().retainAll(allowedRecipients);
  }

  public static ChatGroup getChatGroup(Player player) {
    return chatGroups.computeIfAbsent(player.getUniqueId(), k -> new ChatGroup(player));
  }

  public Player getPlayer() {
    return player;
  }

  public void addAllowedPlayer(Player player) {
    allowedPlayers.add(player);
  }

  public void addDisallowedPlayer(Player player) {
    disallowedPlayers.add(player);
  }

  public void removeAllowedPlayer(Player player) {
    allowedPlayers.remove(player);
  }

  public void removeDisallowedPlayer(Player player) {
    disallowedPlayers.remove(player);
  }

  public void addAllowedPlayers(Collection<Player> players) {
    allowedPlayers.addAll(players);
  }

  public void addDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.addAll(players);
  }

  public void removeAllowedPlayers(Collection<Player> players) {
    allowedPlayers.removeAll(players);
  }

  public void removeDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.removeAll(players);
  }

  public void setAllowedPlayers(Collection<Player> players) {
    allowedPlayers.clear();

    allowedPlayers.addAll(players);
  }

  public void setDisallowedPlayers(Collection<Player> players) {
    disallowedPlayers.clear();

    disallowedPlayers.addAll(players);
  }

  public Set<Player> getAllowedPlayers() {
    return allowedPlayers;
  }

  public Set<Player> getDisallowedPlayers() {
    return disallowedPlayers;
  }

  public void setMode(ChatMode mode) {
    this.mode = mode;
  }

  public ChatMode getMode() {
    return mode;
  }

  public enum ChatMode {
    ALL,
    SELECT,
    DISABLED
  }
}