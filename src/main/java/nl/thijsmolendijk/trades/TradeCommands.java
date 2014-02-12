package nl.thijsmolendijk.trades;

import java.util.Collection;

import nl.thijsmolendijk.trades.trade.Trade;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class TradeCommands {
    @Command(aliases = {"trades", "tr"}, desc = "Gets all the trades available in this map", usage = "[name]", min = 0, max = -1)
    @CommandPermissions({"trades.list"})
    public static void trades(CommandContext args, CommandSender sender) throws CommandException {
        Validate.isTrue(sender instanceof Player, "Only players can do this!");
        Player player = (Player) sender;

        if (Trades.getInstance().getTradeManager().getAllTrades().size() == 0) throw new CommandException("There are no currently loaded trades!");
        if (!Trades.getInstance().getTradeManager().checkTriggers(player.getWorld())) throw new CommandException("Trades are not enabled on this map!");
        
        Collection<Trade> trades = Trades.getInstance().getTradeManager().getAllTrades();
        if (args.argsLength() > 0) trades = Trades.getInstance().getTradeManager().getAllTrades(args.getJoinedStrings(0));
        
        Trades.getInstance().showTrades(player, trades);
    }
}
