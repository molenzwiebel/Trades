package nl.thijsmolendijk.trades.trade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.thijsmolendijk.trades.util.ItemUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.minecraft.util.commands.ChatColor;

/**
 * Represents an (paginated) inventory with trades
 * @author molenzwiebel
 */
public class TradeInventory {
    public static final String NEXT = "Next page";
    public static final String PREV = "Previous page";

    private Inventory inventory;
    private List<Trade> trades;
    private int page;
    private int maxPages;

    public TradeInventory(Collection<Trade> trades) {
        this.trades = new ArrayList<Trade>(trades);
        this.inventory = Bukkit.createInventory(null, 9*3, "Trades");
        this.page = 0;
        this.maxPages = trades.size() / 8;

        updateInventoryContents();
    }

    /**
     * Updates the inventory contents to reflect the current page
     */
    private void updateInventoryContents() {
        boolean moreTrades = (page * 8) + 8 < this.trades.size();
        List<Trade> trades = this.trades.subList(8 * page, (!moreTrades ? this.trades.size() : (8 * page) + 8));

        inventory.setContents(new ItemStack[9*3]);
        
        int i = 0; //index
        for (Trade tr : trades) {
            inventory.setItem(i, tr.getInput());
            inventory.setItem(i + 9, ItemUtils.getNamedItem(tr.getBlock().getBlock(), ChatColor.RESET + tr.getBlock().getName()));
            inventory.setItem(i + 9 + 9, tr.getOutput());
            i++;
        }

        if (page != maxPages) inventory.setItem(8, ItemUtils.getNamedItem(Material.EYE_OF_ENDER, NEXT));
        if (page > 0) inventory.setItem(26, ItemUtils.getNamedItem(Material.EYE_OF_ENDER, PREV));
    }

    /**
     * Called when the user clicks on an item, used for detecting page changes
     * @param st the item stack clicked
     */
    public void onClick(ItemStack st) {
        int old = page;
        if (!st.hasItemMeta() || !st.getItemMeta().hasDisplayName()) return;
        if (st.getItemMeta().getDisplayName().equals(NEXT)) page++;
        if (st.getItemMeta().getDisplayName().equals(PREV)) page--;

        if (old != page) updateInventoryContents();
    }

    /**
     * Shows the inventory to a user
     * @param p the user to show the inventory to
     */
    public void show(Player p) {
        p.openInventory(inventory);
    }
}
