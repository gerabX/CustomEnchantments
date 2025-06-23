package me.gerab.allEnchantments.Listeners.Enchantment.Common;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class WitherListener implements Listener {

    private final Random random = new Random();
    private double chance = 0;
    private final Pattern witherPattern = Pattern.compile(ChatColor.translateAlternateColorCodes('&', "&8Wither (\\d+)"));

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        if (weapon.getItemMeta() == null || weapon.getItemMeta().getLore() == null) return;

        List<String> lore = weapon.getItemMeta().getLore();
        if (lore == null) return;

        for (String line : lore) {
            String stripped = ChatColor.stripColor(line);
            if (stripped.startsWith("Wither")) {
                int level = parseLevelFromLine(stripped);
                if (level > 0) {

                    switch (level) {
                        case 1 -> chance = 5;
                        case 2 -> chance = 7;
                        case 3 -> chance = 10;
                    }

                    if (random.nextDouble() * 100 <= chance) {
                        applyBlindness(target, 5); // 5 másodperc
                        attacker.sendMessage("§8[Vakítás] §7Sikeresen megvakítottad a célpontot!");
                    }
                    break;
                }
            }
        }
    }

    private int parseLevelFromLine(String line) {
        // Line: "Vakítás 3"
        try {
            String[] parts = line.split(" ");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
        }
    }

    private void applyBlindness(LivingEntity entity, int seconds) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, seconds * 20, 0));
    }
}
