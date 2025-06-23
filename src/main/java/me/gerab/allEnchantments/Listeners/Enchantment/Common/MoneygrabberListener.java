package me.gerab.allEnchantments.Listeners.Enchantment.Common;

import me.gerab.allEnchantments.SettingsForCE.EnchantTargets;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class MoneygrabberListener implements Listener {

    private final Random random = new Random();
    private final List<Material> hoes = EnumSet.of(Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE).stream().toList();

     // Érvényes eszközök listája

    // Érvényes eszközök

    @EventHandler
    public void onToolUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // Csak jobbklikk blokkra vagy levegőbe
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if (tool == null || !hoes.contains(tool.getType())) return;
        if (!hasGreedyLore(tool)) return;
        // 7% esély
        if (random.nextInt(100) < 5) {
            player.getInventory().addItem(new ItemStack(Material.EMERALD));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool == null || !EnchantTargets.Tools.contains(tool.getType())) return;
        if (!hasGreedyLore(tool)) return;
        // 7% esély smaragdra
        if (random.nextInt(100) < 5) {
            player.getInventory().addItem(new ItemStack(Material.EMERALD));

        }
    }

    private boolean hasGreedyLore(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return false;

        List<String> lore = meta.getLore();
        if (lore == null) return false;

        return lore.stream().anyMatch(line ->
                line != null && org.bukkit.ChatColor.stripColor(line).toLowerCase().startsWith("pénzéhes")
        );
    }
}
