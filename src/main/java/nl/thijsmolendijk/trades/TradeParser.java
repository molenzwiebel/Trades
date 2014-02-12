package nl.thijsmolendijk.trades;

import java.io.File;

import nl.thijsmolendijk.trades.trade.Trade;
import nl.thijsmolendijk.trades.trade.TradeBlock;
import nl.thijsmolendijk.trades.trade.Trigger;
import nl.thijsmolendijk.trades.util.XMLUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class TradeParser {
    /**
     * Helper method for {@link #parse(Document)}. Converts the specified xml file to a Document
     * @param tradeManager the trade manager to put the loaded trades in
     * @param xmlLoc the location of the xml file
     * @throws Exception if anything unexpected is encountered during parsing
     */
    public static void parse(TradeManager tradeManager, String xmlLoc) throws Exception {
        File file = new File(xmlLoc);
        if (!file.exists()) throw new IllegalArgumentException("XML File "+xmlLoc+" does not exist!");
        SAXBuilder b = new SAXBuilder();
        parse(tradeManager, b.build(file));
    }
    
    /**
     * Parses all the trades from an xml file
     * @param tradeManager the trade manager to put the loaded trades in
     * @param xmlLoc the location of the xml file
     * @throws Exception if anything unexpected is encountered during parsing
     */
    public static void parse(TradeManager tradeManager, Document doc) throws Exception {
        Element root = doc.getRootElement();
        for (Element trade : XMLUtils.flattenElements(root, "trades", "trade")) {
            if (trade.getAttributeValue("name") == null) throw new IllegalArgumentException("Trade is missing a name for the converting block");
            if (trade.getAttributeValue("on") == null) throw new IllegalArgumentException("Trade is missing an material for the converting block");
            if (trade.getChildren("in").size() == 0) throw new IllegalArgumentException("Trade is missing an input item");
            if (trade.getChildren("out").size() == 0) throw new IllegalArgumentException("Trade is missing an output item");

            Material blockMaterial = XMLUtils.parseMaterial(trade.getAttributeValue("on"));
            String blockName = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('`', trade.getAttributeValue("name")));
            TradeBlock block = new TradeBlock(blockMaterial, blockName);
            
            ItemStack in = XMLUtils.parseItem(trade.getChild("in"));
            ItemStack out = XMLUtils.parseItem(trade.getChild("out"));
            
            tradeManager.addTrade(new Trade(in, block, out));
        }
        
        for (Element trigger : XMLUtils.flattenElements(root, "lookingfor", "lookfor")) {
            if (trigger.getAttributeValue("block") == null) throw new IllegalArgumentException("Look for is missing a block type");
            if (trigger.getAttributeValue("location") == null) throw new IllegalArgumentException("Look for is missing a location");

            Material block = XMLUtils.parseMaterial(trigger.getAttributeValue("block"));
            String[] parts = trigger.getAttributeValue("location").split(",");
            if (parts.length != 3) throw new IllegalArgumentException("Location does not exist of 3 parts: "+trigger.getAttributeValue("location"));
            Vector location = new Vector(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
            
            tradeManager.addTrigger(new Trigger(block, location));
        }
    }
}
