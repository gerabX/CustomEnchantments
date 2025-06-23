package me.gerab.allEnchantments;

import me.gerab.allEnchantments.Guis.GUI;
import me.gerab.allEnchantments.Listeners.Enchantment.Common.*;
import me.gerab.allEnchantments.Listeners.Enchantment.Legendary.EternityListener;
import me.gerab.allEnchantments.Listeners.Enchantment.Rare.ArmorBreakerListener;
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
        getServer().getPluginManager().registerEvents(new GUI(), this);

        getServer().getPluginManager().registerEvents(new EternityListener(), this);
        getServer().getPluginManager().registerEvents(new FreezeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlindnessListener(), this);
        getServer().getPluginManager().registerEvents(new WitherListener(), this);
        getServer().getPluginManager().registerEvents(new MoneygrabberListener(), this);
        getServer().getPluginManager().registerEvents(new SpikeHelmetListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorBreakerListener(), this);
    }
}

