package nl.thijsmolendijk.trades;

import java.util.Collection;
import java.util.HashMap;

import nl.thijsmolendijk.trades.trade.Trade;
import nl.thijsmolendijk.trades.trade.TradeInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Maps;

/**
 * Manages the open inventories and makes sure that players can't interact with them
 * @author molenzwiebel
 */
public class TradeListener implements Listener {
    private HashMap<Player, TradeInventory> openInventories = Maps.newHashMap();

    /**
     * Opens the (paginated) inventory to the specified player
     * @param player the player
     * @param trades the trades to show in the inventory
     */
    public void openInventory(Player player, Collection<Trade> trades) {
        if (openInventories.containsKey(player)) throw new RuntimeException();

        TradeInventory inventory = new TradeInventory(trades);
        openInventories.put(player, inventory);
        inventory.show(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!openInventories.containsKey(event.getWhoClicked())) return;
        
        if (event.getRawSlot() > 26) return; //Own player inventory
        
        event.setCancelled(true);
        openInventories.get(event.getWhoClicked()).onClick(event.getCurrentItem());
    }
    
    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        if (openInventories.containsKey(event.getPlayer()))
            openInventories.remove(event.getPlayer());
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (openInventories.containsKey(event.getPlayer()))
            openInventories.remove(event.getPlayer());
    }
}
