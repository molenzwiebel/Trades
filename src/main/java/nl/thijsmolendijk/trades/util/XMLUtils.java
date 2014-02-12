package nl.thijsmolendijk.trades.util;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jdom2.Attribute;
import org.jdom2.Element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class XMLUtils {
    private static HashMap<String, Enchantment> enchantmentNamings = Maps.newHashMap();
    static {
        enchantmentNamings.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantmentNamings.put("fire protection", Enchantment.PROTECTION_FIRE);
        enchantmentNamings.put("feather falling", Enchantment.PROTECTION_FALL);
        enchantmentNamings.put("projectile protection", Enchantment.PROTECTION_PROJECTILE);
        enchantmentNamings.put("blast protection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantmentNamings.put("power", Enchantment.ARROW_DAMAGE);
        enchantmentNamings.put("flame", Enchantment.ARROW_FIRE);
        enchantmentNamings.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchantmentNamings.put("infinity", Enchantment.ARROW_INFINITE);
        enchantmentNamings.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantmentNamings.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchantmentNamings.put("bane of arthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchantmentNamings.put("efficiency", Enchantment.DIG_SPEED);
        enchantmentNamings.put("unbreaking", Enchantment.DURABILITY);
        enchantmentNamings.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchantmentNamings.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchantmentNamings.put("knockback", Enchantment.KNOCKBACK);
        enchantmentNamings.put("fire aspect", Enchantment.FIRE_ASPECT);
        enchantmentNamings.put("thorns", Enchantment.THORNS);
        enchantmentNamings.put("aqua affinity", Enchantment.WATER_WORKER);
        enchantmentNamings.put("respiration", Enchantment.OXYGEN);
    }

    // ARMOR COLORS
    /**
     * Converts a color hex to a {@link java.awt.Color} object
     * @param colorStr the hex color
     * @return the converted color
     */
    public static Color hex2Rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    /**
     * Applies the color to the item stack (only if leather armor)
     * @param item the item to apply the color to
     * @param color the color (hex)
     * @return the new itemstack
     */
    private static ItemStack applyColor(ItemStack item, String color) {
        ItemMeta m = item.getItemMeta();
        if (m instanceof LeatherArmorMeta) {
            LeatherArmorMeta lm = (LeatherArmorMeta) m;
            Color javaColor = hex2Rgb("#" + color);
            lm.setColor(org.bukkit.Color.fromRGB(javaColor.getRed(), javaColor.getGreen(),
                    javaColor.getBlue()));
            item.setItemMeta(lm);
        }
        return item;
    }

    //ELEMENT FLATTENING
    /**
     * Copies all the attributes from the old element to the new element
     * @param from the old element
     * @param to the new element
     * @return the new element
     */
    public static Element copyAttributes(Element from, Element to) {
        Element result = to.clone();
        for (Attribute attribute : from.getAttributes()) {
            if (result.getAttribute(attribute.getName()) == null) {
                result.setAttribute(attribute.getName(), attribute.getValue());
            }
        }
        return result;
    }

    /**
     * Flattens a list of elements
     * @param root the root
     * @param parentTagName the parent tag
     * @param childTagName the child tag
     * @return the list of flattened elements
     */
    public static List<Element> flattenElements(Element root, String parentTagName, String childTagName) {
        List<Element> result = Lists.newArrayList();
        for (Element parent : root.getChildren(parentTagName)) {
            result.addAll(flattenElements(copyAttributes(root, parent), parentTagName, childTagName));
        }
        for (Element child : root.getChildren(childTagName)) {
            result.add(copyAttributes(root, child));
        }
        return result;
    }

    // ITEM PARSING
    /**
     * Converts an enchantment to the {@link org.bukkit.enchantments.Enchantment} version
     * @param name the enchantment to max
     * @return the matched enchant
     */
    public static Enchantment parseEnchantment(String name) {
        if (Enchantment.getByName(name.replace(" ", "_").toUpperCase()) != null)
            return Enchantment.getByName(name.replace(" ", "_").toUpperCase());
        if (enchantmentNamings.containsKey(name.replace("_", " ").toLowerCase()))
            return enchantmentNamings.get(name.replace("_", " ").toLowerCase());
        throw new IllegalArgumentException("Could not find enchantment " + name + "!");
    }
    
    /**
     * Parses a material, checking for both text and item ids
     * @param mat the material
     * @return the parsed material
     */
    public static Material parseMaterial(String mat) {
        Material m = Material.matchMaterial(mat);
        if (m == null) throw new IllegalArgumentException("Material "+mat+" not found!");
        return m;
    }

    /**
     * Parses an item from xml
     * @param e the element
     * @return the item
     */
    public static ItemStack parseItem(Element e) {
        Material mat = parseMaterial(e.getTextNormalize());

        if (mat == null) return null;
        ItemStack toReturn = new ItemStack(mat, 1);

        ItemMeta im = Bukkit.getItemFactory().getItemMeta(mat);
        if (e.getAttributeValue("name") != null)
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('`', e.getAttributeValue("name"))));
        
        if (e.getAttributeValue("lore") != null) 
            im.setLore(Arrays.asList(e.getAttributeValue("lore").split("|")));
        
        toReturn.setItemMeta(im);

        if (e.getAttributeValue("amount") != null)
            toReturn.setAmount(Integer.parseInt(e.getAttributeValue("amount")));
        else
            toReturn.setAmount(1);

        if (e.getAttributeValue("damage") != null)
            toReturn.setDurability((short) Integer.parseInt(e.getAttributeValue("damage")));
        else
            toReturn.setDurability((short) 0);

        if (e.getAttributeValue("enchantment") != null) {
            for (int i = 0; i < e.getAttributeValue("enchantment").split(";").length; i++) {
                String[] rawEnch = e.getAttributeValue("enchantment").split(";")[i].split(":");
                String enchantment = rawEnch[0].replace(" ", "_").toUpperCase();
                String level = "1";
                if (rawEnch.length == 2) level = rawEnch[1];
                Enchantment enchant = parseEnchantment(enchantment);
                toReturn.addUnsafeEnchantment(enchant, Integer.parseInt(level));
            }
        }

        if (e.getAttributeValue("color") != null)
            toReturn = applyColor(toReturn, e.getAttributeValue("color"));
        return toReturn;
    }
}
