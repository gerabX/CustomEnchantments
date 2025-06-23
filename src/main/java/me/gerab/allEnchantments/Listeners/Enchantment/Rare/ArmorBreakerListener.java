package me.gerab.allEnchantments.Listeners.Enchantment.Rare;

import org.bukkit.event.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class ArmorBreakerListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta()) return;

        int level = getBreakerLevel(weapon);
        if (level <= 0) return;

        int chance = level * 5;
        if (random.nextInt(100) >= chance) return; // Nincs aktiválódás

        // Páncél életerő számítás
        double totalArmorHealth = 0;
        for (ItemStack armor : victim.getInventory().getArmorContents()) {
            if (armor != null && armor.getType().getMaxDurability() > 0) {
                int max = armor.getType().getMaxDurability();
                int current = max - armor.getDurability();
                totalArmorHealth += current;
            }
        }

        double damage = totalArmorHealth * 0.05;
        if (damage > 0) {
            victim.damage(damage, attacker);
            attacker.sendMessage("§7Páncéltörő aktiválódott! §cSebzés: " + String.format("%.1f", damage));
        }
    }

    private int getBreakerLevel(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return 0;

        List<String> lore = meta.getLore();
        if (lore == null) return 0;

        return lore.stream()
                .filter(line -> ChatColor.stripColor(line).toLowerCase().startsWith("páncéltörő"))
                .map(line -> ChatColor.stripColor(line).replace("Páncéltörő", "").trim())
                .mapToInt(s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .findFirst().orElse(0);
    }
}

