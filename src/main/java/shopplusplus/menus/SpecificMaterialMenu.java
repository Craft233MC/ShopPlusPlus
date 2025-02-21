package shopplusplus.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import shopplusplus.core.ShopManager;
import shopplusplus.core.gui.MenuPagination;
import shopplusplus.core.structures.PlayerData;
import shopplusplus.core.structures.SerializablePlayerItem;
import shopplusplus.utils.formatters.Formatters;
import shopplusplus.utils.items.GUIUtils;
import shopplusplus.utils.items.ItemUtils;
import shopplusplus.utils.items.MaterialCategorizer;

public class SpecificMaterialMenu {
    public SpecificMaterialMenu(Player player, Material material) {
        List<ItemStack> items = new ArrayList<>();

        List<SerializablePlayerItem> filteredItems = ShopManager.getItemsByMaterial(material);

        HashMap<String, String> replacements = new HashMap<>();

        replacements.put("{material}", Formatters.formatMaterialName(material));

        for (int i = 0; i < filteredItems.size(); i++) {
            SerializablePlayerItem serializablePlayerItem = filteredItems.get(i);

            PlayerData sellerData = ShopManager.getPlayerDataByItem(serializablePlayerItem);

            replacements.put("{item-seller}", sellerData.getPlayer().getName());
            replacements.put("{item-price}", Formatters.getBalance(serializablePlayerItem.getPrice()));
            replacements.put("{item-expires}", Formatters.getExpiredAt(serializablePlayerItem));

            ItemStack itemStack = ItemUtils.getListedItem(serializablePlayerItem.getItem(), replacements);

            items.add(itemStack);
        }

        MenuPagination gui = new MenuPagination(GUIUtils.getTitle("material-menu", replacements), 9 * 6,
                GUIUtils.getNextPageButton(),
                GUIUtils.getPreviousPageButton(), items, (_player, event) -> {
                    new SpecificCategoryMenu(player, MaterialCategorizer.getCategory(material));
                }, (_player, context) -> {
                    if (!context.getEvent().isLeftClick()) {
                        return;
                    }

                    if (context.getIndex() >= filteredItems.size()) {
                        return;
                    }

                    SerializablePlayerItem item = filteredItems.get(context.getIndex());

                    boolean isShulkerBox = item.getItem().getType().name().endsWith("SHULKER_BOX");

                    if (isShulkerBox) {
                        new PurchaseShulkerBoxConfirmMenu(player, item);
                    } else {
                        new PurchaseConfirmMenu(player, item);
                    }
                });

        gui.open(player, GUIUtils.getEmptySlot());
    }
}
