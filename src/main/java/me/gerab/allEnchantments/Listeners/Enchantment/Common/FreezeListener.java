package me.gerab.allEnchantments.Listeners.Enchantment.Common;

import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import me.gerab.allEnchantments.AllEnchantments;

import java.util.Random;

public class FreezeListener implements Listener {

    private final Random random = new Random();
    private final AllEnchantments plugin;

    public FreezeListener(AllEnchantments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        if (!weapon.hasItemMeta() || weapon.getItemMeta().getLore() == null) return;

        int level = getFreezeLevel(weapon);

        if (level == 0) return;

        if (random.nextInt(100) >= 10) return; // 10% esély

        Entity target = event.getEntity();
        int freezeTicks = (level == 1) ? 40 : 100; // 2 vagy 5 másodperc

        if (target instanceof Mob mob) {
            mob.setAI(false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    mob.setAI(true);
                }
            }.runTaskLater(plugin, freezeTicks);

        } else if (target instanceof Player victim) {
            Location frozenLocation = victim.getLocation().clone();

            new BukkitRunnable() {
                int ticksRemaining = freezeTicks;

                @Override
                public void run() {
                    if (ticksRemaining <= 0 || !victim.isOnline()) {
                        this.cancel();
                        return;
                    }

                    if (!victim.getLocation().getBlock().equals(frozenLocation.getBlock())) {
                        victim.teleport(frozenLocation);
                    }

                    ticksRemaining--;
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }

    private int getFreezeLevel(ItemStack item) {
        return item.getItemMeta().getLore().stream()
                .map(ChatColor::stripColor)
                .filter(line -> line.toLowerCase().contains("fagyasztás"))
                .map(line -> line.replaceAll("[^0-9]", ""))
                .mapToInt(levelStr -> {
                    try {
                        return Integer.parseInt(levelStr);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .findFirst()
                .orElse(0);
    }

}

