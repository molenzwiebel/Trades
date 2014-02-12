package nl.thijsmolendijk.trades;

import java.util.Collection;
import java.util.List;

import nl.thijsmolendijk.trades.trade.Trade;
import nl.thijsmolendijk.trades.trade.TradeBlock;
import nl.thijsmolendijk.trades.trade.Trigger;
import nl.thijsmolendijk.trades.util.LiquidMetal;
import nl.thijsmolendijk.trades.util.LiquidMetal.StringProvider;

import org.bukkit.ChatColor;
import org.bukkit.World;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Manages all available trades
 * @author molenzwiebel
 */
public class TradeManager {
    /**
     * A list of all the triggers needed for the trades to show up
     */
    private List<Trigger> requiredTriggers = Lists.newArrayList();
    
    /**
     * A list of all trades, indexed by the name of the {@link TradeBlock} the trade belongs to
     */
    private Multimap<String, Trade> trades = ArrayListMultimap.create();
    
    /**
     * Adds a trade to the manager
     * @param trade the trade to add
     */
    public void addTrade(Trade trade) {
        Preconditions.checkNotNull(trade, "trade");
        trades.put(trade.getBlock().getName(), trade);
    }
    
    /**
     * Adds a trigger
     * @param t the trigger
     */
    public void addTrigger(Trigger t) {
        requiredTriggers.add(t);
    }
    
    /**
     * Gets all the trades currently in the manager
     * @return the trades (immutable)
     */
    public Collection<Trade> getAllTrades() {
        return ImmutableList.copyOf(trades.values());
    }
    
    /**
     * Matches the blockName to the existing keys and returns all the trades for that block
     * @param blockName the block that transforms the trades (to filter on)
     * @return all the trades for that block (immutable)
     */
    public Collection<Trade> getAllTrades(String blockName) {
        String matchedName = LiquidMetal.fuzzyMatch(trades.keySet(), blockName, new StringProvider<String>() {
            @Override
            public String get(String c) {
                return ChatColor.stripColor(c); //In order for colored block names to match user input
            }            
        }, 0.8D);
        if (matchedName == null) throw new IllegalArgumentException("Trader "+blockName+" not found!");
        return ImmutableList.copyOf(trades.get(matchedName));
    }
    
    /**
     * Checks if every trigger required returns true
     * @param w the world to check in
     * @return if the trades are allowed to show up
     */
    public boolean checkTriggers(World w) {
        for (Trigger t : requiredTriggers)
            if (t.matches(w) == false) return false;
        return true;
    }
}
