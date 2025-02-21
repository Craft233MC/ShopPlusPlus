package shopplusplus.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import shopplusplus.Plugin;
import shopplusplus.core.ShopManager;
import shopplusplus.utils.others.NumberUtils;
import shopplusplus.utils.players.PlayerUtils;

public class SellCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot use this command via the console.");
            return false;
        }

        Player player = (Player) sender;

        if (PlayerUtils.hasReachedLimit(player, "max-items")) {
            PlayerUtils.sendMessageFromConfig(player, "commands.limitReached");
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            PlayerUtils.sendMessageFromConfig(player, "commands.noItemInHand");
            return true;
        }

        if (args.length == 0) {
            PlayerUtils.sendMessageFromConfig(player, "commands.noPriceProvided");
            return true;
        }

        if (!NumberUtils.isValidDouble(args[0])) {
            PlayerUtils.sendMessageFromConfig(player, "commands.priceIsNotNumber");
            return true;
        }

        double price = Double.parseDouble(args[0]);

        if (price <= 0) {
            PlayerUtils.sendMessageFromConfig(player, "commands.priceIsNotNumber");
            return true;
        }

        if (price > (int) Plugin.config.get("shop-config.max-price")) {
            PlayerUtils.sendMessageFromConfig(player, "commands.priceTooExpensive");
            return true;
        }

        ItemStack itemStack = itemInHand.clone();

        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

        ShopManager.addItemToShop(player, itemStack, price);

        PlayerUtils.sendMessageFromConfig(player, "commands.itemListed");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
