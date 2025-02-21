package shopplusplus.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import shopplusplus.console.Console;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

public class YmlConfigValidator {
    private final FileConfiguration defaultConfig;
    private final FileConfiguration targetConfig;
    private final File targetFile;

    public YmlConfigValidator(String resourceFileName, File targetFile) {
        defaultConfig = loadResourceConfig(resourceFileName);

        this.targetFile = targetFile;

        targetConfig = YamlConfiguration.loadConfiguration(targetFile);
    }

    public YmlConfigValidator(String resourceFileName, String targetFileName) {
        defaultConfig = loadResourceConfig(resourceFileName);

        File targetFile = new File(targetFileName);
        this.targetFile = targetFile;

        targetConfig = YamlConfiguration.loadConfiguration(targetFile);
    }

    private FileConfiguration loadResourceConfig(String resourceFileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceFileName);

        if (inputStream == null) {
            throw new RuntimeException("Resource file '" + resourceFileName + "' is missing in plugin resources");
        }

        return YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
    }

    public boolean validate() {
        Console.warning("Verifying if the file " + targetFile.getPath() + " has invalid keys...");

        return checkKeys(defaultConfig, targetConfig, "");
    }

    private boolean checkKeys(ConfigurationSection defaultSection, ConfigurationSection targetSection, String path) {
        if (defaultSection == null)
            return true;

        Set<String> keys = defaultSection.getKeys(false);

        boolean allKeysPresent = true;

        for (String key : keys) {
            String fullPath = path.isEmpty() ? key : path + "." + key;

            if (targetSection == null || !targetSection.contains(key)) {
                Console.warning("Key not found in the file " + targetFile.getPath() + ": " + key);

                allKeysPresent = false;
            }

            if (defaultSection.isConfigurationSection(key)) {
                ConfigurationSection defaultSubSection = defaultSection.getConfigurationSection(key);
                ConfigurationSection targetSubSection = (targetSection != null)
                        ? targetSection.getConfigurationSection(key)
                        : null;

                if (!checkKeys(defaultSubSection, targetSubSection, fullPath)) {
                    allKeysPresent = false;
                }
            }
        }

        return allKeysPresent;
    }

    public boolean fix() {
        fixKeys(defaultConfig, targetConfig, "");

        try {
            targetConfig.save(targetFile);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void fixKeys(ConfigurationSection defaultSection, ConfigurationSection targetSection, String path) {
        if (defaultSection == null)
            return;

        Set<String> keys = defaultSection.getKeys(false);
        for (String key : keys) {
            String fullPath = path.isEmpty() ? key : path + "." + key;

            if (!targetSection.contains(key)) {
                targetSection.set(key, defaultSection.get(key));

                Console.warning("Added missing key to the file " + targetFile.getPath() + ": " + key);
            }

            if (defaultSection.isConfigurationSection(key)) {
                ConfigurationSection defaultSubSection = defaultSection.getConfigurationSection(key);
                ConfigurationSection targetSubSection = targetSection.getConfigurationSection(key);

                if (targetSubSection == null) {
                    targetSubSection = targetSection.createSection(key);
                }

                fixKeys(defaultSubSection, targetSubSection, fullPath);
            }
        }
    }
}
