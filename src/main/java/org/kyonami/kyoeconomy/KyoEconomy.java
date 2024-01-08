package org.kyonami.kyoeconomy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kyonami.kyoeconomy.commands.EconomyCommand;
import org.kyonami.kyoeconomy.events.OnPlayerEvent;
import org.kyonami.kyoeconomy.money.MoneyInfos;
import org.kyonami.kyoeconomy.utils.UserConfig;

import java.util.logging.Level;

public final class KyoEconomy extends JavaPlugin {
    private static KyoEconomy _instance = null;
    public static KyoEconomy getInstance() { return _instance; }

    @Override
    public void onEnable(){
        _instance = this;

        this.saveDefaultConfig();
        MoneyInfos.getInstance();

        getServer().getPluginManager().registerEvents(new OnPlayerEvent(), this);

        getCommand("economy").setExecutor(new EconomyCommand());

        Bukkit.getLogger().log(Level.INFO, "[KyoEconomy] Enable");
    }

    @Override
    public void onDisable(){
        MoneyInfos.getInstance().saveAll();

        Bukkit.getLogger().log(Level.INFO, "[KyoEconomy] Disable");
    }
}
