package shopplusplus.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import shopplusplus.core.ShopManager;
import shopplusplus.core.gui.MenuPagination;
import shopplusplus.core.structures.PlayerData;
import shopplusplus.core.structures.SerializablePlayerItem;
import shopplusplus.utils.formatters.Formatters;
import shopplusplus.utils.items.GUIUtils;
import shopplusplus.utils.items.ItemUtils;
import shopplusplus.utils.players.PlayerUtils;

public class ListedItemsMenu {
    public ListedItemsMenu(Player player) {
        List<ItemStack> items = new ArrayList<>();

        PlayerData data = ShopManager.getPlayerDataByPlayer(player);

        List<SerializablePlayerItem> listedItems = data.getItems();

        for (int i = 0; i < listedItems.size(); i++) {
            SerializablePlayerItem serializablePlayerItem = listedItems.get(i);

            HashMap<String, String> replacements = new HashMap<>();

            replacements.put("{item-seller}", player.getName());
            replacements.put("{item-price}", Formatters.getBalance(serializablePlayerItem.getPrice()));
            replacements.put("{item-expires}", Formatters.getExpiredAt(serializablePlayerItem));

            ItemStack listedItem = ItemUtils.getPlayerListedItem(serializablePlayerItem.getItem(), replacements);

            items.add(listedItem);
        }

        MenuPagination gui = new MenuPagination(GUIUtils.getTitle("player-listed-items"), 9 * 6,
                GUIUtils.getNextPageButton(),
                GUIUtils.getPreviousPageButton(), items, (_player, event) -> {
                    new MainMenu(_player); // command: /shop
                }, (_player, context) -> {
                    if (!context.getEvent().isRightClick()) {
                        return;
                    }

                    if (context.getIndex() >= listedItems.size()) {
                        return;
                    }

                    SerializablePlayerItem serializablePlayerItem = listedItems.get(context.getIndex());

                    if (serializablePlayerItem == null) {
                        return;
                    }

                    if (!ShopManager.stillExists(serializablePlayerItem)) {
                        PlayerUtils.sendMessageFromConfig(player, "commands.itemNotFound");
                        return;
                    }

                    if (!PlayerUtils.hasAvailableSlot(player)) {
                        PlayerUtils.sendMessageFromConfig(player, "commands.noSlotInInventory");
                        return;
                    }

                    player.getInventory().addItem(serializablePlayerItem.getItem());

                    ShopManager.removeItemFromShop(serializablePlayerItem);

                    new ListedItemsMenu(player);
                });

        gui.open(player, GUIUtils.getEmptySlot());
    }
}
