package shopplusplus.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import shopplusplus.core.ShopManager;
import shopplusplus.core.gui.MenuPagination;
import shopplusplus.core.structures.Category;
import shopplusplus.utils.formatters.Formatters;
import shopplusplus.utils.items.GUIUtils;
import shopplusplus.utils.items.ItemUtils;

public class SpecificCategoryMenu {
    public SpecificCategoryMenu(Player player, long category) {
        List<ItemStack> items = new ArrayList<>();

        HashMap<String, String> replacements = new HashMap<>();

        replacements.put("{category}", Category.getFromLong(category));

        List<Material> materials = ShopManager.getMaterialsFromCategory(category);

        for (int i = 0; i < materials.size(); i++) {
            Material material = materials.get(i);

            replacements.put("{material}", Formatters.formatMaterialName(material));
            replacements.put("{material-count}", String.valueOf(ShopManager.getItemsByMaterial(material).size()));

            ItemStack categoryMaterial = ItemUtils.getItemByMaterial(material, replacements);

            items.add(categoryMaterial);
        }

        MenuPagination gui = new MenuPagination(GUIUtils.getTitle("category-menu", replacements), 9 * 6,
                GUIUtils.getNextPageButton(),
                GUIUtils.getPreviousPageButton(), items, (_player, event) -> {
                    new MainMenu(player);
                }, (_player, context) -> {
                    if (!context.getEvent().isLeftClick()) {
                        return;
                    }

                    if (context.getIndex() >= materials.size()) {
                        return;
                    }

                    Material material = materials.get(context.getIndex());

                    new SpecificMaterialMenu(player, material);
                });

        gui.open(player, GUIUtils.getEmptySlot());
    }
}
