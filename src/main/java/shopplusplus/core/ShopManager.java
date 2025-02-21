package shopplusplus.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import shopplusplus.Plugin;
import shopplusplus.console.Console;
import shopplusplus.core.structures.PlayerData;
import shopplusplus.core.structures.SerializablePlayerItem;
import shopplusplus.utils.items.MaterialCategorizer;

public class ShopManager {
    public static void addItemToShop(Player player, ItemStack item, double price) {
        PlayerData data = ShopManager.getPlayerDataByPlayer(player);

        data.addItem(item, price);
    }

    public static void removeItemFromShop(SerializablePlayerItem item) {
        PlayerData data = getPlayerDataByItem(item);

        data.removeItem(item);
    }

    public static boolean stillExists(SerializablePlayerItem item) {
        PlayerData data = getPlayerDataByItem(item);

        return data != null;
    }

    public static PlayerData getPlayerDataByPlayer(Player player) {
        if (PlayerData.playersdata.containsKey(player.getUniqueId())) {
            return PlayerData.playersdata.get(player.getUniqueId());
        } else {
            new PlayerData(player, true);

            return ShopManager.getPlayerDataByPlayer(player);
        }
    }

    public static PlayerData getPlayerDataByItem(SerializablePlayerItem item) {
        for (Map.Entry<UUID, PlayerData> entry : PlayerData.playersdata.entrySet()) {
            PlayerData playerdata = entry.getValue();

            for (SerializablePlayerItem eachItem : playerdata.getItems()) {
                if (eachItem.getId().equals(item.getId())) {
                    return playerdata;
                }
            }
        }

        return null;
    }

    public static List<SerializablePlayerItem> getItemsFromCategory(long category) {
        List<SerializablePlayerItem> items = getAllItems();
        List<SerializablePlayerItem> filteredItems = new ArrayList<>();

        for (SerializablePlayerItem item : items) {
            if (MaterialCategorizer.getCategory(item.getItem().getType()) == category) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    public static List<SerializablePlayerItem> getItemsByMaterial(Material material) {
        List<SerializablePlayerItem> items = getAllItems();
        List<SerializablePlayerItem> filteredItems = new ArrayList<>();

        for (SerializablePlayerItem item : items) {
            if (item.getItem().getType().equals(material)) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    public static List<Material> getMaterialsFromCategory(long category) {
        List<SerializablePlayerItem> items = getItemsFromCategory(category);
        List<Material> filteredMaterials = new ArrayList<>();

        for (SerializablePlayerItem item : items) {
            if (!filteredMaterials.contains(item.getItem().getType())) {
                filteredMaterials.add(item.getItem().getType());
            }
        }

        return filteredMaterials;
    }

    public static List<PlayerData> getAllPlayersData() {
        List<PlayerData> playersdata = new ArrayList<PlayerData>();

        for (Map.Entry<UUID, PlayerData> entry : PlayerData.playersdata.entrySet()) {
            PlayerData playerdata = entry.getValue();

            playersdata.add(playerdata);
        }

        return playersdata;
    }

    public static List<SerializablePlayerItem> getAllItems() {
        List<SerializablePlayerItem> items = new ArrayList<SerializablePlayerItem>();

        for (Map.Entry<UUID, PlayerData> entry : PlayerData.playersdata.entrySet()) {
            PlayerData playerdata = entry.getValue();

            for (SerializablePlayerItem item : playerdata.getItems()) {
                if (!item.isExpired()) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    public static List<PlayerData> getTopPlayers() {
        return getAllPlayersData().stream()
                .sorted(Comparator.comparingDouble(PlayerData::getTotalEarned).reversed())
                .collect(Collectors.toList());
    }

    public static int getPlayerRank(Player player) {
        List<PlayerData> sortedPlayersData = getTopPlayers();

        for (int i = 0; i < sortedPlayersData.size(); i++) {
            PlayerData playerdata = sortedPlayersData.get(i);

            if (playerdata.getPlayerId().equals(player.getUniqueId())) {
                return i + 1;
            }
        }

        return 0;
    }

    public static PlayerData loadFromFile(UUID id) {
        File playersFolder = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "players");
        File file = new File(playersFolder, "playerdata_" + id.toString() + ".yml");

        if (!file.exists()) {
            Console.error("Player data file not found: playerdata_" + id + ".yml");
            return null;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String playerUuid = config.getString("playerId");
        double totalEarned = config.getDouble("totalEarned");

        PlayerData playerData = new PlayerData(Bukkit.getOfflinePlayer(UUID.fromString(playerUuid)));
        playerData.totalEarned = totalEarned;

        List<String> items = config.getStringList("items");

        for (String itemString : items) {
            playerData.items.add(SerializablePlayerItem.fromString(itemString));
        }

        return playerData;
    }

    public static int loadPlayersData() {
        File playersFolder = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "players");
        File[] playersDataFiles = playersFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (playersDataFiles == null)
            return 0;

        for (File file : playersDataFiles) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                UUID id = UUID.fromString(config.getString("playerId"));

                PlayerData playerData = loadFromFile(id);

                if (playerData != null) {
                    PlayerData.playersdata.put(id, playerData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return PlayerData.playersdata.size();
    }
}
