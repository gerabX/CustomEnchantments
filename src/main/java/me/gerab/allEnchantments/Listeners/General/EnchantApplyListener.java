package me.gerab.allEnchantments.Listeners.General;

import me.gerab.allEnchantments.SettingsForCE.CustomEnchantmentType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EnchantApplyListener implements Listener {

    @EventHandler
    public void onBookApply(InventoryClickEvent event) {
        if (event.getClick().isShiftClick() || event.getAction() == null) return;

        ItemStack cursorItem = event.getCursor();
        ItemStack targetItem = event.getCurrentItem();

        if (cursorItem == null || targetItem == null) return;
        if (cursorItem.getType() != Material.ENCHANTED_BOOK) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemMeta cursorMeta = cursorItem.getItemMeta();
        if (cursorMeta == null || !cursorMeta.hasDisplayName()) return;

        String displayName = ChatColor.stripColor(cursorMeta.getDisplayName());
        CustomEnchantmentType enchant = null;
        int newLevel = 1;

        // Enchant és szint lekérése
        for (CustomEnchantmentType type : CustomEnchantmentType.values()) {
            String strippedName = ChatColor.stripColor(type.getName());
            if (displayName.startsWith(strippedName)) {
                enchant = type;
                String[] split = displayName.split("\\+");
                if (split.length == 2) {
                    try {
                        newLevel = Integer.parseInt(split[1].trim());
                    } catch (NumberFormatException ignored) {}
                }
                break;
            }
        }

        if (enchant == null) return;

        // Típus ellenőrzés
        if (!enchant.getApplicableItems().contains(targetItem.getType())) {
            player.sendMessage("§cEz az enchant nem használható erre az eszközre.");
            return;
        }

        ItemMeta targetMeta = targetItem.getItemMeta();
        if (targetMeta == null) return;

        List<String> lore = targetMeta.hasLore() ? targetMeta.getLore() : new java.util.ArrayList<>();
        String strippedEnchantName = ChatColor.stripColor(enchant.getName());
        boolean found = false;

        for (int i = 0; i < lore.size(); i++) {
            String loreLineStripped = ChatColor.stripColor(lore.get(i));
            if (loreLineStripped.startsWith(strippedEnchantName)) {
                found = true;

                // Szint kivonása
                String[] parts = loreLineStripped.split("\\+");
                if (parts.length == 2) {
                    try {
                        int existingLevel = Integer.parseInt(parts[1].trim());
                        if (existingLevel >= newLevel) {
                            player.sendMessage("§cEz az enchant már rajta van vagy magasabb szinten.");
                            return;
                        } else {
                            // Lecseréljük alacsonyabb szintet
                            lore.set(i, enchant.getName() + " +" + newLevel);
                            targetMeta.setLore(lore);
                            targetItem.setItemMeta(targetMeta);
                            cursorItem.setAmount(cursorItem.getAmount() - 1);
                            player.sendMessage("§aFrissítetted az enchantot: " + enchant.getName() + " +" + newLevel);
                            return;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        if (!found) {
            // Új enchant hozzáadása
            lore.add(enchant.getName() + " " + newLevel);
            targetMeta.setLore(lore);
            targetItem.setItemMeta(targetMeta);
            cursorItem.setAmount(cursorItem.getAmount() - 1);
            player.sendMessage("§aFelraktad az enchantot: " + enchant.getName() + " " + newLevel);
        }
    }
}



