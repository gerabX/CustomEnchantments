package me.gerab.allEnchantments.Listeners.Enchantment.Common;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SpikeHelmetListener implements Listener {

    @EventHandler

    public void onPlayerDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;

        for (ItemStack item : victim.getInventory().getArmorContents()) {
            if (item == null || !item.hasItemMeta()) continue;
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasLore()) continue;

            List<String> lore = meta.getLore();
            if (lore == null) continue;

            boolean hasThorns = lore.stream().anyMatch(line ->
                    line != null && ChatColor.stripColor(line).toLowerCase().startsWith("visszaszúrás")
            );

            if (hasThorns) {
                victim.damage(4.0); // 4.0 = 2 szív
                break;
            }
        }
    }
}


