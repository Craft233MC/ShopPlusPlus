package shopplusplus.core.config;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import shopplusplus.Plugin;
import shopplusplus.console.Console;

public class LanguageLoader {
    public static Plugin plugin;
    public static FileConfiguration language;

    public LanguageLoader(Plugin plugin) {
        LanguageLoader.plugin = plugin;

        File directory = new File(plugin.getDataFolder(), "languages/");
        File defaultPath = new File(plugin.getDataFolder(), "languages/en-US.yml");

        String locale = Plugin.config.get("language");

        if (!directory.isDirectory()) {
            directory.mkdir();

            try {
                InputStream stream = plugin.getResource("en-US.yml");
                FileUtils.copyInputStreamToFile(stream, defaultPath);
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        if (locale != null) {
            File localefile = new File(plugin.getDataFolder(),
                    "languages/" + locale + (locale.endsWith(".yml") ? "" : ".yml"));

            FileConfiguration loaded = YamlConfiguration.loadConfiguration(localefile);

            LanguageLoader.language = loaded;
        } else {
            FileConfiguration loaded = YamlConfiguration.loadConfiguration(defaultPath);

            LanguageLoader.language = loaded;
        }

        Console.info("Loaded language file: " + locale);
    }

    public File getLanguageFile() {
        String locale = Plugin.config.get("language");

        return new File(plugin.getDataFolder(),
                "languages/" + locale + (locale.endsWith(".yml") ? "" : ".yml"));
    }

    public void reloadLanguage(Plugin plugin) {
        Plugin.language = new LanguageLoader(plugin);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) language.get(path);
    }
}
