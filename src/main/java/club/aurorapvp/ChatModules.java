package club.aurorapvp;

import club.aurorapvp.modules.AutoMessages;
import club.aurorapvp.modules.ChatModule;
import java.util.HashSet;
import java.util.Set;

public class ChatModules {
  private static final Set<ChatModule> modules = new HashSet<>();

  public static void init() {
    ChatModules.registerModule(new AutoMessages());
  }

  public static ChatModule getModule(String name) {
    for (ChatModule module : modules) {
      if (module.getName().equalsIgnoreCase(name)) {
        return module;
      }
    }
    return null;
  }

  public static void registerModule(ChatModule module) {
    module.enableModule();
    modules.add(module);
  }
}
