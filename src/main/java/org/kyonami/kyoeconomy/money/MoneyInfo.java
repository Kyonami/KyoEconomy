package org.kyonami.kyoeconomy.money;
import org.kyonami.kyoeconomy.utils.Config;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyInfo {
    private Player player;
    private long money;
    private final Config config;
    public Player getPlayer() { return this.player; }
    public long getMoney() { return this.money; }
    public String getMoneyString() { return Long.toString(this.money);}
    public Boolean hasEnoughMoney(double money) { return money <= this.money; }

    public MoneyInfo(Player player){
        this.player = player;
        this.config = new Config("plugins/KyoEconomy/Users/" + this.player.getUniqueId() + ".yml");
        this.money = config.getLong("money");
    }

    public MoneyInfo(UUID uuid){
        this.config = new Config("plugins/KyoEconomy/Users/" + uuid + ".yml");
        this.money = config.getLong("money");
    }

    public void setMoney(long money){
        this.money = money;
    }

    public void save() {
        this.config.set("money", this.money);
        this.config.save();
    }

    public void reload(){
        this.config.reload();
        this.money = config.getLong("money");
    }
}
