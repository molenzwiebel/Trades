package nl.thijsmolendijk.trades.trade;

import org.bukkit.Material;

import com.google.common.base.Preconditions;

/**
 * Represents a block that is suitable of "trading", or transforming an input into an output
 * @author molenzwiebel
 */
public class TradeBlock {
    private Material block;
    private String name;
    
    public TradeBlock(Material mat, String name) {
        this.block = Preconditions.checkNotNull(mat, "trade block material");
        this.name = Preconditions.checkNotNull(name, "name of trade block");
    }

    /**
     * Gets the material that the trade block is made from
     * @return the block
     */
    public Material getBlock() {
        return block;
    }

    /**
     * Gets the special name of the trade block
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TradeBlock [block=" + block + ", name=" + name + "]";
    }
}
