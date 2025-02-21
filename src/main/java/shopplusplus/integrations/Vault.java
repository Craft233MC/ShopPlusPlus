package shopplusplus.integrations;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import shopplusplus.Plugin;

public class Vault {
    private Plugin plugin;
    public Chat chat;
    public Economy economy;
    public Permission permissions;

    public Vault(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = this.plugin.getServer().getServicesManager().getRegistration(Chat.class);

        if (rsp == null) {
            return false;
        }

        chat = rsp.getProvider();

        return chat != null;
    }

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = this.plugin.getServer().getServicesManager()
                .getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();

        return economy != null;
    }

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = this.plugin.getServer().getServicesManager()
                .getRegistration(Permission.class);

        if (rsp == null) {
            return false;
        }

        permissions = rsp.getProvider();

        return permissions != null;
    }

    public Chat getChat() {
        return chat;
    }

    public Economy getEconomy() {
        return economy;
    }

    public Permission getPermissions() {
        return permissions;
    }
}