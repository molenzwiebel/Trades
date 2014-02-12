package nl.thijsmolendijk.trades.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
    /**
     * Creates an item from the specified material, with the specified name
     * @param material the material
     * @param name the name
     * @return the created item
     */
    public static ItemStack getNamedItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        item.setItemMeta(m);
        return item;
    }
}
