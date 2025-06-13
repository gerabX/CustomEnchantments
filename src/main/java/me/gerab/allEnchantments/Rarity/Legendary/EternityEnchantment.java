package me.gerab.allEnchantments.Rarity.Legendary;

import me.gerab.allEnchantments.SettingsForCE.Commonness;
import me.gerab.allEnchantments.SettingsForCE.CustomEnchantment;
import me.gerab.allEnchantments.SettingsForCE.EnchantTargets;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EternityEnchantment implements CustomEnchantment {

    @Override
    public String getName() {
        return "§5§lÖrokkévalóság";
    }

    @Override
    public Commonness getRarity() {
        return Commonness.LEGENDARY;
    }

    @Override
    public List<Material> getApplicableItems() {
        return EnchantTargets.EnchantableItems; // <- újralétrehozás nélkül hivatkozik
    }

    @Override
    public ItemStack createBook(int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(getName());
            List<String> lore = new ArrayList<>();
            lore.add("§5Ez az enchantment lehetővé teszi, hogy halálkor ne tűnjön el az eszközöd.");
            meta.setLore(lore);
            book.setItemMeta(meta);
        }
        return book;

    }


}
