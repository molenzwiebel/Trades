package nl.thijsmolendijk.trades.trade;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

/**
 * Represents a single trade with an input, a block and an output
 * @author molenzwiebel
 */
public class Trade {
    private ItemStack input;
    private TradeBlock block;
    private ItemStack output;

    public Trade(ItemStack in, TradeBlock block, ItemStack out) {
        this.input = Preconditions.checkNotNull(in, "input item");
        this.block = Preconditions.checkNotNull(block, "transform block");
        this.output = Preconditions.checkNotNull(out, "ouput item");
    }

    /**
     * Gets the input item that the user needs to place
     * @return the input
     */
    public ItemStack getInput() {
        return input;
    }

    /**
     * Gets the block the input needs to be placed on in order for it to be transformed to the output item
     * @return the block
     */
    public TradeBlock getBlock() {
        return block;
    }

    /**
     * Gets the output of the trade, given to the player when they place the input
     * @return the output
     */
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "Trade [input=" + input + ", block=" + block + ", output="
                + output + "]";
    }
}
