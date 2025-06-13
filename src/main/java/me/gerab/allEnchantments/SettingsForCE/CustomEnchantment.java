package me.gerab.allEnchantments.SettingsForCE;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface CustomEnchantment {
    String getName();
    Commonness getRarity();
    List<Material> getApplicableItems();
    ItemStack createBook(int level);

}
