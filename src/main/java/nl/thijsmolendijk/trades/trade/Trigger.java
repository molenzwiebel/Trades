package nl.thijsmolendijk.trades.trade;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.google.common.base.Preconditions;

/**
 * Represents a condition in a world that must be true before Trades activates
 * @author molenzwiebel
 */
public class Trigger {
    private Material material;
    private Vector location;
    
    public Trigger(Material material, Vector location) {
        this.material = Preconditions.checkNotNull(material, "material");
        this.location = Preconditions.checkNotNull(location, "location");
    }
    
    /**
     * Checks if the trigger matches (eg, is true)
     * @param world the world to check in
     * @return if the trigger is true
     */
    public boolean matches(World world) {
        if (world.getBlockAt(location.toLocation(world)).getType() == material) return true;
        return false;
    }
}
