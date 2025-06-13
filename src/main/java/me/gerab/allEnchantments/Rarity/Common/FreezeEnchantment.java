package me.gerab.allEnchantments.Rarity.Common;

import me.gerab.allEnchantments.SettingsForCE.Commonness;
import me.gerab.allEnchantments.SettingsForCE.CustomEnchantment;
import me.gerab.allEnchantments.SettingsForCE.EnchantTargets;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FreezeEnchantment implements CustomEnchantment {

    @Override
    public String getName() {
        return "§bFagyasztás";
    }

    @Override
    public Commonness getRarity() {
        return Commonness.COMMON;
    }

    @Override
    public List<Material> getApplicableItems() {
        return EnchantTargets.SWORDS; // <- újralétrehozás nélkül hivatkozik
    }

    @Override
    public ItemStack createBook(int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(getName() + " " + level);
            List<String> lore = new ArrayList<>();
            if (level == 1) {
                lore.add("§7Megfagyasztja az összes mobot §c2 másodpercre§7.");
            } else if (level == 2) {
                lore.add("§7Megfagyasztja az összes mobot §c5 másodpercre§7.");
            }
            meta.setLore(lore);
            book.setItemMeta(meta);
        }
        return book;
    }

}



