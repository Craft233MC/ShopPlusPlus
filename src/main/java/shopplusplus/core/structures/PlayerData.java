package shopplusplus.core.structures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import shopplusplus.Plugin;
import shopplusplus.console.Console;

public class PlayerData {
    public static final Map<UUID, PlayerData> playersdata = new HashMap<>();

    public UUID playerId;
    public List<SerializablePlayerItem> items = new ArrayList<SerializablePlayerItem>();
    public double totalEarned = 0.0;

    public PlayerData(OfflinePlayer player) {
        this.playerId = player.getUniqueId();
    }

    public PlayerData(OfflinePlayer player, boolean saveToFile) {
        this.playerId = player.getUniqueId();

        if (saveToFile) {
            saveToFile();
        }
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(playerId);
    }

    public List<SerializablePlayerItem> getItems() {
        return items;
    }

    public double getTotalEarned() {
        return totalEarned;
    }

    public void addToTotalEarned(double amount) {
        this.totalEarned += amount;
        saveToFile();
    }

    public void addItem(ItemStack item, double price) {
        items.add(new SerializablePlayerItem(item, price));

        saveToFile();
    }

    public boolean hasItem(SerializablePlayerItem item) {
        for (int i = 0; i < items.size(); i++) {
            SerializablePlayerItem _item = items.get(i);

            if (_item.getId().equals(item.getId())) {
                return true;
            }
        }

        return false;
    }

    public void removeItem(SerializablePlayerItem item) {
        for (int i = 0; i < items.size(); i++) {
            SerializablePlayerItem _item = items.get(i);

            if (_item.getId().equals(item.getId())) {
                items.remove(i);

                saveToFile();

                break;
            }
        }
    }

    // Other required functions
    public void saveToFile() {
        File playersFolder = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "players");
        if (!playersFolder.exists())
            playersFolder.mkdirs();

        File file = new File(playersFolder, "playerdata_" + playerId.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        config.set("playerId", playerId.toString());

        List<String> itemStrings = new ArrayList<String>();
        for (SerializablePlayerItem item : items) {
            itemStrings.add(item.toString());
        }
        config.set("items", itemStrings);

        config.set("totalEarned", totalEarned);

        playersdata.put(playerId, this);

        try {
            config.save(file);
        } catch (IOException e) {
            Console.error("Unable to write player\'s data.");
            e.printStackTrace();
        }
    }
}
