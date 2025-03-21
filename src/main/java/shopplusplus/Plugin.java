package shopplusplus;

import java.io.File;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import shopplusplus.commands.ListedCommand;
import shopplusplus.commands.SellCommand;
import shopplusplus.commands.ShopCommand;
import shopplusplus.console.Console;
import shopplusplus.core.ShopManager;
import shopplusplus.core.config.ConfigLoader;
import shopplusplus.core.config.LanguageLoader;
import shopplusplus.core.config.YmlConfigValidator;
import shopplusplus.integrations.Vault;

public class Plugin extends JavaPlugin {
	public static ConfigLoader config;
	public static YmlConfigValidator configValidator;
	public static LanguageLoader language;
	public static YmlConfigValidator languageValidator;
	public static Vault vault;
	private static FoliaLib foliaLib;

	public void onEnable() {
		long startTime = System.currentTimeMillis();
		foliaLib = new FoliaLib(this);

		Console.pluginBanner();

		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}

		File playersDataFolder = new File(getDataFolder(), "players");
		if (!playersDataFolder.exists()) {
			playersDataFolder.mkdir();
		}

		saveDefaultConfig();

		config = new ConfigLoader(this);

		language = new LanguageLoader(this);

		configValidator = new YmlConfigValidator("config.yml", new File(getDataFolder(), "config.yml"));

		if (!configValidator.validate()) {
			Console.error("The file config.yml has missing keys. Fixing the file...");

			boolean fixed = configValidator.fix();

			if (fixed) {
				Console.info("The file config.yml has been fixed.");

				config = new ConfigLoader(this);
			} else {
				Console.error("Unable to fix the file, disabling plugin...");

				disablePlugin();

				return;
			}
		} else {
			Console.info("The file config.yml has no missing keys, OK.");
		}

		languageValidator = new YmlConfigValidator("en-US.yml", language.getLanguageFile());

		if (!languageValidator.validate()) {
			Console.error("The language file " + language.getLanguageFile().getName()
					+ " has missing keys. Fixing the file...");

			boolean fixed = languageValidator.fix();

			if (fixed) {
				Console.info("The file " + language.getLanguageFile().getName() + " has been fixed.");

				language = new LanguageLoader(this);
			} else {
				Console.error("Unable to fix the file, disabling plugin...");

				disablePlugin();

				return;
			}
		} else {
			Console.info("The language file " + language.getLanguageFile().getName() + " has no missing keys, OK.");
		}

		if (!isVaultInstalled()) {
			Console.error("Unable to find the plugin: Vault");
			Console.error("Disabling the plugin...");
			disablePlugin();
			return;
		} else {
			Console.info("Found plugin: Vault");
		}

		Plugin.vault = new Vault(this);

		if (!Plugin.vault.setupEconomy()) {
			Console.error("Unable to load Vault API service provider: Economy");
			Console.error("Disabling the plugin...");
			disablePlugin();
			return;
		} else {
			Console.info("Loaded Vault API service provider: Economy");
		}

		if (!Plugin.vault.setupPermissions()) {
			Console.error("Unable to load Vault API service provider: Permissions");
			Console.error("Disabling the plugin...");
			disablePlugin();
			return;
		} else {
			Console.info("Loaded Vault API service provider: Permissions");
		}

		Console.info("Loading players data, path: " + playersDataFolder.getAbsolutePath());

		int loadedPlayersData = ShopManager.loadPlayersData();

		Console.info("Loaded " + loadedPlayersData + " players data.");

		getCommand("shop").setExecutor(new ShopCommand());
		getCommand("sell").setExecutor(new SellCommand());
		getCommand("listed").setExecutor(new ListedCommand());

		long endTime = System.currentTimeMillis();

		Console.info("Shop++ is now ready, took " + (endTime - startTime) + " milliseconds to load.");
	}

	public void onDisable() {
		Console.info("Plugin has been disabled.");
	}

	public void disablePlugin() {
		getServer().getPluginManager().disablePlugin(this);
	}

	public static String getVersion() {
		return "2.0.0";
	}

	public boolean isVaultInstalled() {
		return Bukkit.getServer().getPluginManager().getPlugin("Vault") != null
				&& Bukkit.getServer().getPluginManager().getPlugin("Vault").isEnabled();
	}

	public static FoliaLib getFoliaLib() {
		return foliaLib;
	}
}
