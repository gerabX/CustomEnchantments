package me.gerab.allEnchantments.Rarity.Common;

import me.gerab.allEnchantments.SettingsForCE.Commonness;
import me.gerab.allEnchantments.SettingsForCE.CustomEnchantment;
import me.gerab.allEnchantments.SettingsForCE.EnchantTargets;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlindnessEnchant implements CustomEnchantment {

    @Override
    public String getName() {
        return "§8Vakítás";
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
            switch (level) {
                case 1:
                    lore.add("§7Elvakítja az összes mobot §c5 másodpercre§7. (3% esély)");
                    break;
                case 2:
                    lore.add("§7Elvakítja az összes mobot §c5 másodpercre§7. (6% esély)");
                    break;
                case 3:
                    lore.add("§7Elvakítja az összes mobot §c5 másodpercre§7. (9% esély)");
                    break;
                case 4:
                    lore.add("§7Elvakítja az összes mobot §c5 másodpercre§7. (12% esély)");
                    break;
                case 5:
                    lore.add("§7Elvakítja az összes mobot §c5 másodpercre§7. (15% esély)");
                    break;
                default:
                    lore.add("§7Ismeretlen szint: " + level);
            }

            meta.setLore(lore);
            book.setItemMeta(meta);
        }
        return book;
    }
}
