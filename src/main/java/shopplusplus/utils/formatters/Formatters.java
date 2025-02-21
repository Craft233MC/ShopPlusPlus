package shopplusplus.utils.formatters;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import shopplusplus.Plugin;
import shopplusplus.core.structures.SerializablePlayerItem;
import shopplusplus.utils.chat.ChatColorTranslator;
import shopplusplus.utils.others.NumberUtils;

public class Formatters {
    public static String replace(String string, Map<String, String> replacements) {
        if (string == null) {
            return "Error: String is null";
        }

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            string = string.replace(entry.getKey(),
                    entry.getValue() == null ? "Error: received null instead of string" : entry.getValue());
        }

        return string;
    }

    public static String formatTitle(String title, int currentPage, int totalPages) {
        HashMap<String, String> replacements = new HashMap<String, String>();

        replacements.put("{title}", title);
        replacements.put("{current-page}", String.valueOf(currentPage));
        replacements.put("{total-pages}", String.valueOf(totalPages));

        return ChatColorTranslator
                .translate(replace(Plugin.config.get("formatters.gui-pagination-title"), replacements));
    }

    public static String getBalance(double number) {
        boolean isEconomyFormatterEnabled = Plugin.config.get("economy-formatter.format-balance");

        if (isEconomyFormatterEnabled) {
            String balance = NumberUtils.convertDoubleToBalance(number);
            String balanceFormat = Plugin.config.get("economy-formatter.display-balance");

            return balanceFormat.replace("{value}", balance);
        } else {
            return String.valueOf(number);
        }
    }

    public static String getExpiredAt(SerializablePlayerItem item) {
        if (item.isExpired()) {
            return Plugin.language.get("general.expired");
        } else {
            long expiresAt = item.getExpiresAt();

            return getTimeRemaining(expiresAt);
        }
    }

    public static String getTimeRemaining(double futureTimeMillis) {
        double currentTimeMillis = System.currentTimeMillis();

        double timeDifference = futureTimeMillis - currentTimeMillis;

        if (timeDifference < 0) {
            return Plugin.language.get("general.expired");
        }

        long days = (long) (timeDifference / (1000 * 60 * 60 * 24));
        timeDifference %= (1000 * 60 * 60 * 24);

        long hours = (long) (timeDifference / (1000 * 60 * 60));
        timeDifference %= (1000 * 60 * 60);

        long minutes = (long) (timeDifference / (1000 * 60));
        timeDifference %= (1000 * 60);

        long seconds = (long) (timeDifference / 1000);

        String format = Plugin.config.get("formatters.time-remaining");

        return format.replace("{seconds}", String.valueOf(seconds)).replace("{minutes}", String.valueOf(minutes))
                .replace("{hours}", String.valueOf(hours)).replace("{days}", String.valueOf(days));
    }

    public static String formatMaterialName(Material material) {
        String output = "";

        for (String string : material.name().split("_")) {
            output += string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase() + " ";
        }
        
        return output.substring(0, output.length() - 1);
    }
}
