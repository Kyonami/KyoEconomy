package org.kyonami.kyoeconomy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kyonami.kyoeconomy.commands.MoneyCommand;
import org.kyonami.kyoeconomy.events.OnPlayerEvent;
import org.kyonami.kyoeconomy.money.MoneyInfos;

import java.util.logging.Level;

public final class KyoEconomy extends JavaPlugin {
    private static KyoEconomy _instance = null;
    public static KyoEconomy getInstance() { return _instance; }

    @Override
    public void onEnable(){
        _instance = this;

        MoneyInfos.getInstance();

        getServer().getPluginManager().registerEvents(new OnPlayerEvent(), this);

        getCommand("money").setExecutor(new MoneyCommand());

        Bukkit.getLogger().log(Level.INFO, "[KyoEconomy] Enable");
    }

    @Override
    public void onDisable(){
        MoneyInfos.getInstance().saveAll();

        Bukkit.getLogger().log(Level.INFO, "[simplemoney] Disable");
    }
}
