package me.gerab.allEnchantments.Guis;

import me.gerab.allEnchantments.SettingsForCE.Commonness;
import me.gerab.allEnchantments.SettingsForCE.CustomEnchantmentType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class EnchantListGUI implements Listener {

    private final String GUI_TITLE = ChatColor.translateAlternateColorCodes('&', "&b&lElérhető Enchantok");

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);

        // Dekorációs keret
        ItemStack glass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, glass);
        }

        // Enchant csoportok szerint
        Map<Commonness, List<CustomEnchantmentType>> grouped = Arrays.stream(CustomEnchantmentType.values())
                .collect(Collectors.groupingBy(CustomEnchantmentType::getRarity));

        int slot = 10;
        for (Commonness rarity : Commonness.values()) {
            List<CustomEnchantmentType> enchantments = grouped.getOrDefault(rarity, Collections.emptyList());
            for (CustomEnchantmentType enchant : enchantments) {
                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(enchant.getName());
                    meta.setLore(List.of(
                            ChatColor.GRAY + "Típus: " + rarity.name(),
                            ChatColor.GRAY + "Használható: " + enchant.getApplicableItems().size() + " itemen"
                    ));
                    item.setItemMeta(meta);
                }
                gui.setItem(slot, item);
                slot++;

                if ((slot + 1) % 9 == 0) slot += 2; // kihagyás keret miatt
                if (slot >= 53) break;
            }
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;
        event.setCancelled(true); // ne lehessen kivenni semmit
    }
}

