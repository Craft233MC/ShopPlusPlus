package shopplusplus.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import shopplusplus.Plugin;
import shopplusplus.console.Console;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigLoader {
    private final Plugin plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigLoader(Plugin plugin) {
        this.plugin = plugin;
        initializeConfig();
    }

    private void initializeConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            Console.error("config.yml not found, the file was either renamed or deleted.");
            Console.error("Disabling the plugin...");

            plugin.disablePlugin();
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        Console.info("Loaded config file: config.yml");
    }

    public void reloadConfig(Plugin plugin) {
        Plugin.config = new ConfigLoader(plugin);
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            plugin.getLogger().warning("Cannot save config.yml because it is not initialized.");
            return;
        }

        try {
            config.save(configFile);
            plugin.getLogger().info("config.yml saved successfully.");
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config.yml: " + e.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public List<String> getKeysUnderPath(String path) {
        if (config.isConfigurationSection(path)) {
            Set<String> keys = config.getConfigurationSection(path).getKeys(false);

            return new ArrayList<String>(keys);
        }
        return new ArrayList<String>();
    }

    public void set(String path, Object value) {
        config.set(path, value);
        saveConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}