package me.gerab.allEnchantments;

import me.gerab.allEnchantments.SettingsForCE.CustomEnchantmentType;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private final AllEnchantments plugin;

    public CommandHandler(AllEnchantments plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§cHasználat: /ce give <enchant> vagy /ce rl");
            return true;
        }

        if (args[0].equalsIgnoreCase("rl")) {
            plugin.reloadConfig();
            sender.sendMessage("§aPlugin újratöltve!");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cCsak játékosok használhatják ezt a parancsot.");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("§cHasználat: /ce give <enchant neve>");
                return true;
            }

            String displayNameInput = args[1];
            CustomEnchantmentType type = CustomEnchantmentType.getByDisplayName(args[1]);

            if (type == null) {
                player.sendMessage("§cIsmeretlen enchant név: " + displayNameInput);
                return true;
            }

            ItemStack book = type.createBook(1);
            player.getInventory().addItem(book);
            player.sendMessage("§aMegkaptad a(z) " + type.getName() + " enchant könyvet!");
            return true;
        }
        return false; // Ha nem ismert parancs, akkor visszatérünk false-szal, hogy a Bukkit tudja kezelni a hibát.
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("give");
            suggestions.add("rl");
            return suggestions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return Arrays.stream(CustomEnchantmentType.values())
                    .map(type -> normalize(type.getName()))
                    .filter(name -> name.startsWith(normalize(args[1])))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private String normalize(String input) {
        if (input == null) return "";
        String stripped = ChatColor.stripColor(input).toLowerCase();
        String normalized = Normalizer.normalize(stripped, Normalizer.Form.NFD);
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
                .matcher(normalized)
                .replaceAll("")
                .replaceAll("[^a-z0-9]", "");
    }

}

