package shopplusplus.utils.players;

import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import shopplusplus.Plugin;
import shopplusplus.console.Console;
import shopplusplus.core.ShopManager;
import shopplusplus.core.structures.PlayerData;
import shopplusplus.utils.chat.ChatColorTranslator;
import shopplusplus.utils.formatters.Formatters;

public class PlayerUtils {
    public static boolean hasAvailableSlot(Player player) {
        return player.getInventory().firstEmpty() == -1 ? false : true;
    }

    public static void sendMessage(Player player, String... messages) {
        player.sendMessage(ChatColorTranslator.translate(String.join("", messages)));
    }

    public static void sendMessageFromConfig(Player player, String path, Map<String, String> replacements) {
        String message = Plugin.language.get(path);

        if (message == null) {
            player.sendMessage("String not found from the language file: " + path);
            return;
        }

        message = Formatters.replace(message, replacements);

        player.sendMessage(ChatColorTranslator.translate(message));
    }

    public static void sendMessageFromConfig(Player player, String path) {
        String message = Plugin.language.get(path);

        if (message == null) {
            player.sendMessage("String not found from the language file: " + path);
            return;
        }

        player.sendMessage(ChatColorTranslator.translate(message));
    }

    public static boolean hasReachedLimit(Player player, String limitType) {
        String group = "default";

        try {
            group = Plugin.vault.getPermissions().getPrimaryGroup(player);
        } catch (UnsupportedOperationException e) {
            Console.error(
                    "Unable to find a service provider for permissions and groups, using the default group \"default\".");
            Console.error(
                    "Please install a plugin that supports permissions and groups. We recommend installing the LuckPerms plugin.");
        }

        int limit = Plugin.config.get("limits." + group + "." + limitType) == null ? 0 : Plugin.config.get("limits." + group + "." + limitType);

        switch (limitType) {
            case "max-items":
                PlayerData data = ShopManager.getPlayerDataByPlayer(player);

                if (data == null) {
                    return false;
                }

                if (data.getItems().size() >= limit) {
                    return true;
                }

                break;
            default:
                break;
        }

        return false;
    }

    public static double getPlayerBalance(OfflinePlayer player) {
        return Plugin.vault.getEconomy().getBalance(player);
    }

    public static void addMoneyToPlayer(OfflinePlayer player, double amount) {
        Plugin.vault.getEconomy().depositPlayer(player, amount);
    }

    public static void removeMoneyFromPlayer(OfflinePlayer player, double amount) {
        Plugin.vault.getEconomy().withdrawPlayer(player, amount);
    }
}
