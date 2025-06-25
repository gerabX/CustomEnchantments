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
    private final int ITEMS_PER_PAGE = 28;
    private final Map<UUID, Integer> playerPages = new HashMap<>();

    public void open(Player player) {
        openPage(player, 0);
    }

    private void openPage(Player player, int page) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);

        // Dekoráció
        ItemStack glass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < 54; i++) gui.setItem(i, glass);

        // Enchantok
        List<CustomEnchantmentType> allEnchantments = Arrays.stream(CustomEnchantmentType.values())
                .sorted(Comparator.comparing(e -> e.getRarity().ordinal()))
                .collect(Collectors.toList());

        int startIndex = page * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, allEnchantments.size());

        for (int i = startIndex, slot = 10; i < endIndex && slot < 44; i++) {
            CustomEnchantmentType enchant = allEnchantments.get(i);
            ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§b📘 " + enchant.getName());
                meta.setLore(List.of(
                        ChatColor.GRAY + "Ritkaság: §e" + enchant.getRarity().name(),
                        ChatColor.GRAY + "Típus: §f" + enchant.getApplicableItems().stream().map(Enum::name).collect(Collectors.joining(", "))
                ));
                item.setItemMeta(meta);
            }
            gui.setItem(slot, item);
            slot++;
            if ((slot + 1) % 9 == 0) slot += 2; // Ugrás a következő sorba a szegély miatt
        }

        // Lapozó gombok
        if (page > 0) gui.setItem(45, createButton(Material.ARROW, "§a⬅ Előző oldal"));
        if (endIndex < allEnchantments.size()) gui.setItem(53, createButton(Material.ARROW, "§aKövetkező oldal ➡"));

        playerPages.put(player.getUniqueId(), page);
        player.openInventory(gui);
    }

    private ItemStack createButton(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

        int currentPage = playerPages.getOrDefault(player.getUniqueId(), 0);

        if (displayName.equalsIgnoreCase("Előző oldal")) {
            openPage(player, currentPage - 1);
        } else if (displayName.equalsIgnoreCase("Következő oldal")) {
            openPage(player, currentPage + 1);
        }
    }
}


