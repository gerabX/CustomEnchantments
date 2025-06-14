package me.gerab.allEnchantments;

import me.gerab.allEnchantments.Guis.GUI;
import me.gerab.allEnchantments.Listeners.Enchantment.EternityEnchantListener;
import me.gerab.allEnchantments.Listeners.General.EnchantApplyListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class AllEnchantments extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
        getLogger().info("AllEnchantments plugin betöltve.");
    }

    public void registerCommands() {
        getCommand("ce").setExecutor(new CommandHandler(this));

    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new EnchantApplyListener(), this);
        getServer().getPluginManager().registerEvents(new EternityEnchantListener(), this);
        getServer().getPluginManager().registerEvents(new GUI(), this);
    }
}

