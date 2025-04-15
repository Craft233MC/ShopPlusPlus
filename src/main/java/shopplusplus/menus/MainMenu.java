package shopplusplus.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import shopplusplus.core.ShopManager;
import shopplusplus.core.gui.Menu;
import shopplusplus.core.structures.Category;
import shopplusplus.core.structures.PlayerData;
import shopplusplus.utils.formatters.Formatters;
import shopplusplus.utils.items.GUIUtils;
import shopplusplus.utils.players.PlayerUtils;

public class MainMenu {
    public MainMenu(Player player) {
        Menu gui = new Menu(GUIUtils.getTitle("main-menu"), 9 * 3);

        ItemStack buildingBlocksButton = GUIUtils.getItem("building-blocks");

        gui.addItem(10, buildingBlocksButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificCategoryMenu(player, Category.BUILDING_BLOCKS);
        });

        ItemStack toolsButton = GUIUtils.getItem("tools");

        gui.addItem(11, toolsButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificCategoryMenu(player, Category.TOOLS);
        });

        ItemStack foodButton = GUIUtils.getItem("food");

        gui.addItem(12, foodButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificCategoryMenu(player, Category.FOOD);
        });

        ItemStack mineralsButton = GUIUtils.getItem("minerals");

        gui.addItem(13, mineralsButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificCategoryMenu(player, Category.MINERALS);
        });

        ItemStack naturalButton = GUIUtils.getItem("natural");

        gui.addItem(14, naturalButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificCategoryMenu(player, Category.NATURAL);
        });

        ItemStack redstoneButton = GUIUtils.getItem("redstone");

        gui.addItem(15, redstoneButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificCategoryMenu(player, Category.REDSTONE);
        });

        ItemStack miscellaneousButton = GUIUtils.getItem("miscellaneous");

        gui.addItem(16, miscellaneousButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificCategoryMenu(player, Category.MISCELLANEOUS);
        });

        gui.addItem(18, GUIUtils.getBackButton(), (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            player.closeInventory();
        });

        PlayerData data = ShopManager.getPlayerDataByPlayer(player);

        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("{player}", player.getName());
        replacements.put("{player-balance}", Formatters.getBalance(PlayerUtils.getPlayerBalance(player)));
        replacements.put("{player-totalearned}", Formatters.getBalance(data.getTotalEarned()));
        replacements.put("{player-rank}", String.valueOf(ShopManager.getPlayerRank(player)));

        ItemStack playerInfoButton = GUIUtils.getItem("player-info", replacements);

        gui.addItem(26, playerInfoButton, (_player, event) -> {
            // open list items menu (/listed)
            if (!event.isLeftClick()) {
                return;
            }

            new ListedItemsMenu(player);
        });

        gui.open(player, GUIUtils.getEmptySlot());
    }
}