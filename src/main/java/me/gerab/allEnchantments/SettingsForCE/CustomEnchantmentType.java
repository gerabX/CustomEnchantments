package me.gerab.allEnchantments.SettingsForCE;

import me.gerab.allEnchantments.Rarity.Common.*;
import me.gerab.allEnchantments.Rarity.Legendary.EternityEnchant;
import me.gerab.allEnchantments.Rarity.Rare.ArmorBreakerEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

public enum CustomEnchantmentType {

    FREEZE(
            new FreezeEnchantment(), Commonness.COMMON, EnchantTargets.SWORDS, true // <- helyesen példányosítva // <- helyes enum használat  // <- listát közvetlenül használod// <- pl. hogy craftolható-e, ha ez boolean flag
    ),
    ETERNITY(
            new EternityEnchant(), Commonness.LEGENDARY, EnchantTargets.EnchantableItems, true
    ),
    BLINDNESS(
            new BlindnessEnchant(), Commonness.COMMON, EnchantTargets.SWORDS, true
    ),
    MONEYGRABBER(
            new MoneyGrabberEnchant(), Commonness.COMMON, EnchantTargets.Tools, true
    ),
    WITHER(
            new WitherEnchant(), Commonness.COMMON, EnchantTargets.Weapons, true
    ),
    SPIKEHELMET(
            new SpikeHelmetEnchant(), Commonness.COMMON, EnchantTargets.Helmets, true
    ),
    ARMORBREAKER(
            new ArmorBreakerEnchant(), Commonness.RARE, EnchantTargets.Weapons, true
    );


    private final CustomEnchantment enchantment;
    private final Commonness rarity;
    private final List<Material> applicableItems;
    private final boolean craftable;

    CustomEnchantmentType(CustomEnchantment enchantment, Commonness rarity, List<Material> applicableItems, boolean craftable) {
        this.enchantment = enchantment;
        this.rarity = rarity;
        this.applicableItems = applicableItems;
        this.craftable = craftable;
    }

    public ItemStack createBook(int level) {
        return enchantment.createBook(level);
    }

    public String getName() {
        return enchantment.getName();
    }

    public Commonness getRarity() {
        return rarity;
    }

    public List<Material> getApplicableItems() {
        return applicableItems;
    }



    public static CustomEnchantmentType getByDisplayName(String input) {
        String normalizedInput = normalize(input);
        for (CustomEnchantmentType type : values()) {
            String normalizedName = normalize(type.getName());
            if (normalizedInput.equals(normalizedName)) {
                return type;
            }
        }
        return null;
    }

    private static String normalize(String input) {
        if (input == null) return "";
        String stripped = ChatColor.stripColor(input).toLowerCase();
        String normalized = Normalizer.normalize(stripped, Normalizer.Form.NFD);
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
                .matcher(normalized)
                .replaceAll("")
                .replaceAll("[^a-z0-9]", ""); // Csak az angol betűk és számok maradnak
    }

}








