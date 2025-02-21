package shopplusplus.menus;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import shopplusplus.core.ShopManager;
import shopplusplus.core.gui.Menu;
import shopplusplus.core.structures.PlayerData;
import shopplusplus.core.structures.SerializablePlayerItem;
import shopplusplus.utils.items.GUIUtils;
import shopplusplus.utils.items.ItemUtils;
import shopplusplus.utils.players.PlayerUtils;

public class PurchaseShulkerBoxConfirmMenu {
    public PurchaseShulkerBoxConfirmMenu(Player player, SerializablePlayerItem item) {
        Menu gui = new Menu(GUIUtils.getTitle("purchase-confirm"), 9 * 6);

        List<List<Object>> shulkerBoxItems = ItemUtils.getItemsFromShulkerBox(item.getItem());

        for (List<Object> each : shulkerBoxItems) {
            gui.addItem((int) each.get(1), (ItemStack) each.get(0), (_player, event) -> {
                // Do nothing
            });
        }

        ItemStack cancelButton = GUIUtils.getItem("cancel-purchase");

        gui.addItem(38, cancelButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificMaterialMenu(player, item.getItem().getType());
        });

        gui.addItem(40, item.getItem(), (_player, event) -> {
            // Do nothing
        });

        ItemStack confirmButton = GUIUtils.getItem("confirm-purchase");

        gui.addItem(42, confirmButton, (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            if (!ShopManager.stillExists(item)) {
                PlayerUtils.sendMessageFromConfig(player, "commands.itemNotFound");
                return;
            }

            PlayerData sellerData = ShopManager.getPlayerDataByItem(item);

            if (sellerData.getPlayerId().equals(player.getUniqueId())) {
                PlayerUtils.sendMessageFromConfig(player, "commands.itemBuyerIsSeller");
                return;
            }

            if (item.getPrice() > PlayerUtils.getPlayerBalance(player)) {
                PlayerUtils.sendMessageFromConfig(player, "commands.notEnoughMoneyToPurchase");
                return;
            }

            if (!PlayerUtils.hasAvailableSlot(player)) {
                PlayerUtils.sendMessageFromConfig(player, "commands.noSlotInInventory");
                return;
            }

            PlayerUtils.removeMoneyFromPlayer(player, item.getPrice());
            PlayerUtils.addMoneyToPlayer(sellerData.getPlayer(), item.getPrice());

            sellerData.addToTotalEarned(item.getPrice());
            
            player.getInventory().addItem(item.getItem());

            ShopManager.removeItemFromShop(item);

            new MainMenu(player);
        });

        gui.addItem(45, GUIUtils.getBackButton(), (_player, event) -> {
            if (!event.isLeftClick()) {
                return;
            }

            new SpecificMaterialMenu(player, item.getItem().getType());
        });

        gui.open(player, GUIUtils.getEmptySlot(), 27);
    }
}
