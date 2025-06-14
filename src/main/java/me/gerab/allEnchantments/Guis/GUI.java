package me.gerab.allEnchantments.Guis;

import me.gerab.allEnchantments.SettingsForCE.CustomEnchantmentType;
import me.gerab.allEnchantments.SettingsForCE.Commonness;
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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GUI implements Listener {

    private final String GUI_TITLE = ChatColor.translateAlternateColorCodes('&', "&6&lFő GUI");

    public void openMainGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Dekoráció
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glass.setItemMeta(glassMeta);
        }
        int[] decoSlots = {0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26};
        for (int slot : decoSlots) {
            gui.setItem(slot, glass);
        }

        // Gombok
        gui.setItem(11, createButton(Material.BOOK, "&a&lGyakori Enchant", List.of("&7Kapsz egy random", "&aGyakori &7enchanted könyvet", "&e20 XP &7szintért")));
        gui.setItem(13, createButton(Material.ENCHANTED_BOOK, "&9&lRitka Enchant", List.of("&7Kapsz egy random", "&9Ritka &7enchanted könyvet", "&e50 XP &7szintért")));
        gui.setItem(15, createButton(Material.NETHER_STAR, "&6&lLegendás Enchant", List.of("&7Kapsz egy random", "&6Legendás &7enchanted könyvet", "&e75 XP &7szintért")));
        gui.setItem(22, createButton(Material.KNOWLEDGE_BOOK, "&b&lÖsszes Enchant", List.of("&7Kattints az összes", "&7elérhető enchant megtekintéséhez")));

        player.openInventory(gui);
    }

    private ItemStack createButton(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            meta.setLore(lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getView().getTitle().equals(GUI_TITLE)) {
            event.setCancelled(true);

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            String display = clicked.getItemMeta() != null ? ChatColor.stripColor(clicked.getItemMeta().getDisplayName()) : "";
            int xpLevel = player.getLevel();

            switch (display) {
                case "Gyakori Enchant":
                    if (xpLevel >= 20) {
                        giveRandomBook(player, Commonness.COMMON);
                        player.setLevel(xpLevel - 20);
                    } else player.sendMessage("§cNincs elég XP szinted (20 kell)");
                    break;
                case "Ritka Enchant":
                    if (xpLevel >= 50) {
                        giveRandomBook(player, Commonness.RARE);
                        player.setLevel(xpLevel - 50);
                    } else player.sendMessage("§cNincs elég XP szinted (50 kell)");
                    break;
                case "Legendás Enchant":
                    if (xpLevel >= 75) {
                        giveRandomBook(player, Commonness.LEGENDARY);
                        player.setLevel(xpLevel - 75);
                    } else player.sendMessage("§cNincs elég XP szinted (75 kell)");
                    break;
                case "Összes Enchant":
                    new EnchantListGUI().open(player);
                    break;
            }
        }
    }

    private void giveRandomBook(Player player, Commonness rarity) {
        List<CustomEnchantmentType> matches = List.of(CustomEnchantmentType.values()).stream()
                .filter(e -> e.getRarity() == rarity)
                .collect(Collectors.toList());

        if (!matches.isEmpty()) {
            CustomEnchantmentType chosen = matches.get(new Random().nextInt(matches.size()));
            player.getInventory().addItem(chosen.createBook(1));
            player.sendMessage("§aKaptál egy: " + chosen.getName());
        }
    }
}

