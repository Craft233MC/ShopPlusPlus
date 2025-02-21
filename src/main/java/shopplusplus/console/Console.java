package shopplusplus.console;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import shopplusplus.Plugin;

public class Console {
    private static final Logger logger = Logger.getLogger("Shop++");

    public static void pluginBanner() {
        String line_splitter = "";

        for (int i = 0; i < 38; i++) {
            line_splitter += "-";
        }

        String banner = " ____  _                             \r\n" + //
                        "/ ___|| |__   ___  _ __    _     _   \r\n" + //
                        "\\___ \\| '_ \\ / _ \\| '_ \\ _| |_ _| |_ \r\n" + //
                        " ___) | | | | (_) | |_) |_   _|_   _|\r\n" + //
                        "|____/|_| |_|\\___/| .__/  |_|   |_|  \r\n" + //
                        "                  |_|                " +
                "\nVersion: " + Plugin.getVersion() + "\nRunning on " + Bukkit.getName() + ": " + Bukkit.getVersion();

        logger.info(line_splitter);

        for (String line : banner.split("\n")) {
            logger.info(line);
        }

        logger.info(line_splitter);

        if (Plugin.getVersion().contains("prerelease")) {
            warning("The version you are currently using is a pre-release version.");
            warning("Exploits, Glitches, and Bugs may exist in pre-release versions. Proceed at your own risk!");
        }
    }

    public static void info(String message) {
        logger.info(message);

        return;
    }

    public static void warning(String message) {
        logger.warning(ConsoleColors.YELLOW + message + ConsoleColors.RESET);

        return;
    }

    public static void error(String message) {
        logger.severe(ConsoleColors.RED + message + ConsoleColors.RESET);

        return;
    }
}