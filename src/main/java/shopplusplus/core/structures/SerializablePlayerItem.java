package shopplusplus.core.structures;

import java.util.Map;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import shopplusplus.Plugin;
import shopplusplus.utils.items.ItemStackSerializer;

public class SerializablePlayerItem {
    private UUID id;
    private String serializedItem;
    private double price;
    private long listedAt;

    public SerializablePlayerItem(String serializedItem, double price) {
        this.id = UUID.randomUUID();
        this.serializedItem = serializedItem;
        this.price = price;
        this.listedAt = System.currentTimeMillis();
    }

    public SerializablePlayerItem(ItemStack item, double price) {
        this.id = UUID.randomUUID();
        this.serializedItem = ItemStackSerializer.serialize(item);
        this.price = price;
        this.listedAt = System.currentTimeMillis();;
    }

    public SerializablePlayerItem(String serializedItem, double price, long listedAt) {
        this.id = UUID.randomUUID();
        this.serializedItem = serializedItem;
        this.price = price;
        this.listedAt = listedAt;
    }

    public SerializablePlayerItem(ItemStack item, double price, long listedAt) {
        this.id = UUID.randomUUID();
        this.serializedItem = ItemStackSerializer.serialize(item);
        this.price = price;
        this.listedAt = listedAt;
    }

    public SerializablePlayerItem(UUID id, String serializedItem, double price, long listedAt) {
        this.id = id;
        this.serializedItem = serializedItem;
        this.price = price;
        this.listedAt = listedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getSerializedItem() {
        return serializedItem;
    }

    public ItemStack getItem() {
        return ItemStackSerializer.deserialize(serializedItem);
    }

    public void setSerializedItem(String serializedItem) {
        this.serializedItem = serializedItem;
    }

    public double getPrice() {
        return price;
    }

    public long getListedAt() {
        return listedAt;
    }

    public long getExpiresAt() {
        long days = 86400000L * (int) Plugin.config.get("shop-config.remove-items-after");

        return getListedAt() + days;
    }

    public boolean isExpired() {
        long expiresAt = getExpiresAt();

        return System.currentTimeMillis() > expiresAt;
    }

    public PlayerData getPlayerData() {
        for (Map.Entry<UUID, PlayerData> entry : PlayerData.playersdata.entrySet()) {
            PlayerData playerdata = entry.getValue();

            for (SerializablePlayerItem eachItem : playerdata.getItems()) {
                if (eachItem.getId().equals(getId())) {
                    return playerdata;
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return (id + "," + serializedItem + "," + price + "," + listedAt);
    }

    public static SerializablePlayerItem fromString(String string) {
        String[] splitted = string.split(",");

        return new SerializablePlayerItem(UUID.fromString(splitted[0]), splitted[1], Double.parseDouble(splitted[2]), Long.parseLong(splitted[3]));
    }
}