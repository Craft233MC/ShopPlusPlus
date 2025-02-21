package shopplusplus.utils.items;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import com.google.common.collect.Lists;

import shopplusplus.Plugin;
import shopplusplus.utils.chat.ChatColorTranslator;
import shopplusplus.utils.formatters.Formatters;

public class ItemUtils {
    public static ItemStack getItem(String displayname, List<String> lore, Material material) {
        List<String> loreCopy = (lore != null) ? new ArrayList<>(lore) : null;

        if (material == null) {
            material = Material.BARRIER;
        }

        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColorTranslator.translate(displayname));

        if (loreCopy != null) {
            ArrayList<String> lorelist = new ArrayList<>();
            for (String each : loreCopy) {
                if (!each.contains("&")) {
                    each = "&f" + each;
                }
                lorelist.add(ChatColorTranslator.translate(each));
            }
            meta.setLore(lorelist);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItem(String displayname, List<String> lore, Material material,
            Map<String, String> replacements) {
        displayname = Formatters.replace(displayname, replacements);

        List<String> loreCopy = (lore != null) ? new ArrayList<>(lore) : null;

        if (loreCopy != null) {
            for (int i = 0; i < loreCopy.size(); i++) {
                String string = loreCopy.get(i);
                string = Formatters.replace(string, replacements);
                loreCopy.set(i, string);
            }
        }

        return getItem(displayname, loreCopy, material);
    }

    public static ItemStack getPlayerHead(String displayname, List<String> lore, String texture) {
        List<String> loreCopy = (lore != null) ? new ArrayList<>(lore) : null;

        ItemStack item = getCustomHeadTexture(texture);
        if (item == null || item.getType() != Material.PLAYER_HEAD) {
            throw new IllegalStateException("Failed to create a valid Player Head!");
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColorTranslator.translate(displayname));

        if (loreCopy != null) {
            ArrayList<String> lorelist = new ArrayList<>();
            for (String each : loreCopy) {
                if (!each.contains("&")) {
                    each = "&f" + each;
                }
                lorelist.add(ChatColorTranslator.translate(each));
            }
            meta.setLore(lorelist);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getPlayerHead(String displayname, List<String> lore, String texture,
            Map<String, String> replacements) {
        displayname = Formatters.replace(displayname, replacements);

        List<String> loreCopy = (lore != null) ? new ArrayList<>(lore) : null;

        if (loreCopy != null) {
            for (int i = 0; i < loreCopy.size(); i++) {
                String string = loreCopy.get(i);
                string = Formatters.replace(string, replacements);
                loreCopy.set(i, string);
            }
        }

        return getPlayerHead(displayname, loreCopy, texture);
    }

    private static ItemStack getCustomHeadTexture(String texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setOwnerProfile(
                getProfile("https://textures.minecraft.net/texture/" + texture));
        head.setItemMeta(meta);

        return head;
    }

    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        URL urlobject;

        try {
            urlobject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }

        textures.setSkin(urlobject);
        profile.setTextures(textures);

        return profile;
    }

    public static ItemStack getPlayerListedItem(ItemStack item, Map<String, String> replacements) {
        List<String> lore = Plugin.language.get("menu-others.material-item-specific-player.lore");

        List<String> loreCopy = (lore != null) ? new ArrayList<>(lore) : null;

        ItemMeta meta = item.getItemMeta();

        if (loreCopy != null) {
            ArrayList<String> lorelist = new ArrayList<>();
            for (String each : loreCopy) {
                if (!each.contains("&")) {
                    each = "&f" + each;
                }
                lorelist.add(ChatColorTranslator.translate(Formatters.replace(each, replacements)));
            }

            meta.setLore(lorelist);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getListedItem(ItemStack item, Map<String, String> replacements) {
        List<String> lore = Plugin.language.get("menu-others.material-item.lore");

        List<String> loreCopy = (lore != null) ? new ArrayList<>(lore) : null;

        ItemMeta meta = item.getItemMeta();

        if (loreCopy != null) {
            ArrayList<String> lorelist = new ArrayList<>();
            for (String each : loreCopy) {
                if (!each.contains("&")) {
                    each = "&f" + each;
                }
                lorelist.add(ChatColorTranslator.translate(Formatters.replace(each, replacements)));
            }

            meta.setLore(lorelist);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItemByMaterial(Material material, Map<String, String> replacements) {
        List<String> lore = Plugin.language.get("menu-others.category-item.lore");

        List<String> loreCopy = (lore != null) ? new ArrayList<>(lore) : null;

        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();

        if (loreCopy != null) {
            ArrayList<String> lorelist = new ArrayList<>();
            for (String each : loreCopy) {
                if (!each.contains("&")) {
                    each = "&f" + each;
                }
                lorelist.add(ChatColorTranslator.translate(Formatters.replace(each, replacements)));
            }

            meta.setLore(lorelist);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static List<List<Object>> getItemsFromShulkerBox(ItemStack shulkerBoxItem) {
        List<List<Object>> items = Lists.newArrayList();

        if (shulkerBoxItem == null || !shulkerBoxItem.getType().name().endsWith("SHULKER_BOX")) {
            return items;
        }

        if (!(shulkerBoxItem.getItemMeta() instanceof BlockStateMeta)) {
            return items;
        }

        BlockStateMeta blockStateMeta = (BlockStateMeta) shulkerBoxItem.getItemMeta();

        if (!(blockStateMeta.getBlockState() instanceof ShulkerBox)) {
            return items;
        }

        ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();

        ItemStack[] shulkerItems = shulkerBox.getInventory().getContents();
        for (int i = 0; i < shulkerItems.length; i++) {
            ItemStack item = shulkerItems[i];

            if (item != null) {
                List<Object> itemData = new ArrayList<Object>();

                itemData.add(item);
                itemData.add(i);

                items.add(itemData);
            }
        }

        return items;
    }
}