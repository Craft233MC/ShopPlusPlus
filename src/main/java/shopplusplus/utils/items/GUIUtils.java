package shopplusplus.utils.items;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import shopplusplus.Plugin;
import shopplusplus.utils.formatters.Formatters;

public class GUIUtils {
    public static String getTitle(String guiName) {
        return Plugin.language.get("menu-titles." + guiName);
    }

    public static String getTitle(String guiName, Map<String, String> replacements) {
        return Formatters.replace(Plugin.language.get("menu-titles." + guiName), replacements);
    }

    public static ItemStack getItem(String itemName) {
        String path = "menu-items." + itemName;

        String displayname = Plugin.language.get(path + ".displayname");
        List<String> lore = Plugin.language.get(path + ".lore");
        String icon = Plugin.language.get(path + ".icon");

        if (icon.startsWith("PLAYERHEAD-")) {
            String texture = icon.split("-")[1];

            return ItemUtils.getPlayerHead(displayname, lore, texture);
        } else {
            return ItemUtils.getItem(displayname, lore, Material.getMaterial(icon));
        }
    }

    public static ItemStack getItem(String itemName, Map<String, String> replacements) {
        String path = "menu-items." + itemName;

        String displayname = Plugin.language.get(path + ".displayname");
        List<String> lore = Plugin.language.get(path + ".lore");
        String icon = Plugin.language.get(path + ".icon");

        if (icon == null) {
            icon = "BARRIER";
        }

        if (icon.startsWith("PLAYERHEAD-")) {
            String texture = icon.split("-")[1];

            return ItemUtils.getPlayerHead(displayname, lore, texture, replacements);
        } else {
            return ItemUtils.getItem(displayname, lore, Material.getMaterial(icon), replacements);
        }
    }

    public static ItemStack getBackButton() {
        String displayname = Plugin.language.get("menu-others.back-button.displayname");
        List<String> lore = Plugin.language.get("menu-others.back-button.lore");
        String icon = Plugin.language.get("menu-others.back-button.icon");

        ItemStack button = ItemUtils.getItem(displayname, lore,
                Material.getMaterial(icon));

        return button;
    }

    public static ItemStack getPreviousPageButton() {
        String displayname = Plugin.language.get("menu-others.previous-page-button.displayname");
        List<String> lore = Plugin.language.get("menu-others.previous-page-button.lore");
        String icon = Plugin.language.get("menu-others.previous-page-button.icon");

        ItemStack button = ItemUtils.getItem(displayname, lore,
                Material.getMaterial(icon));

        return button;
    }

    public static ItemStack getNextPageButton() {
        String displayname = Plugin.language.get("menu-others.next-page-button.displayname");
        List<String> lore = Plugin.language.get("menu-others.next-page-button.lore");
        String icon = Plugin.language.get("menu-others.next-page-button.icon");

        ItemStack button = ItemUtils.getItem(displayname, lore,
                Material.getMaterial(icon));

        return button;
    }

    public static ItemStack getEmptySlot() {
        String displayname = Plugin.language.get("menu-others.filler-item.displayname");
        String icon = Plugin.language.get("menu-others.filler-item.icon");

        ItemStack button = ItemUtils.getItem(displayname, null, Material.getMaterial(icon));

        return button;
    }
}