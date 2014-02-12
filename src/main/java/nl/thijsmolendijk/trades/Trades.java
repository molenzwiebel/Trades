package nl.thijsmolendijk.trades;

import java.util.Collection;

import nl.thijsmolendijk.trades.trade.Trade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

public class Trades extends JavaPlugin {
    private static Trades instance;
    
    private CommandsManager<CommandSender> commands;
    private TradeManager tradeManager;
    private TradeListener listener;

    @Override
    public void onEnable() {
        instance = this;
        setupCommands();
        
        listener = new TradeListener();
        Bukkit.getPluginManager().registerEvents(listener, this);
        
        try {
            load("trades.xml");
        } catch (Exception e) {
            System.out.println("[Trades] [SEVERE] Could not load trades xml!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }
    
    /**
     * Gets the shared {@link Trades} instance
     * @return the instance
     */
    public static Trades getInstance() {
        return instance;
    }

    /**
     * Loads all the trades from the specified xml file
     * @param location the xml file
     */
    public void load(String location) throws Exception {
        tradeManager = new TradeManager();
        TradeParser.parse(tradeManager, location);
    }
    
    /**
     * Shows the specified player the specified trades
     * @param p the player
     * @param trades the trades
     */
    public void showTrades(Player p, Collection<Trade> trades) {
        listener.openInventory(p, trades);
    }
    
    /**
     * Gets the current trade manager with trades
     * @return the manager
     */
    public TradeManager getTradeManager() {
        return tradeManager;
    }
    
    /**
     * Simple method that setups sk89q's command framework
     */
    private void setupCommands() {
        this.commands = new BukkitCommandsManager();
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        cmdRegister.register(TradeCommands.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else if (e.getCause() instanceof IllegalArgumentException) {
                sender.sendMessage(ChatColor.RED + e.getCause().getMessage());
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
