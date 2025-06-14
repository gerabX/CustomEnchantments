package me.gerab.allEnchantments.Rarity.Common;

import me.gerab.allEnchantments.SettingsForCE.Commonness;
import me.gerab.allEnchantments.SettingsForCE.CustomEnchantment;
import me.gerab.allEnchantments.SettingsForCE.EnchantTargets;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpikeHelmetEnchant implements CustomEnchantment {

    @Override
    public String getName() {
        return "§cVisszaszúrás";
    }

    @Override
    public Commonness getRarity() {
        return Commonness.COMMON;
    }

    @Override
    public List<Material> getApplicableItems() {
        return EnchantTargets.Helmets; // <- újralétrehozás nélkül hivatkozik
    }

    @Override
    public ItemStack createBook(int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(getName());
            List<String> lore = new ArrayList<>();
            lore.add("§cErősen szúrós sisak!");
            lore.add("§cSebződés esetén + ba fogsz sebződni.");
            meta.setLore(lore);
            book.setItemMeta(meta);
        }
        return book;

    }
}
