package org.kyonami.kyoeconomy.money;
import org.kyonami.kyoeconomy.utils.Config;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyInfo {
    private Player player;
    private double money;
    private final Config config;
    public Player getPlayer() { return this.player; }
    public double getMoney() { return this.money; }
    public String getMoneyString() { return String.format("%.2f", this.money);}
    public Boolean compareMoney(double money) { return money < this.money; }

    public MoneyInfo(Player player){
        this.player = player;
        this.config = new Config("plugins/kyoeconomy/Users/" + this.player.getUniqueId() + ".yml");
        this.money = config.getDouble("money");
    }

    public MoneyInfo(UUID uuid){
        this.config = new Config("plugins/kyoeconomy/Users/" + uuid + ".yml");
        this.money = config.getDouble("money");
    }

    public void setMoney(double money){
        this.money = money;
    }

    public void save() {
        this.config.set("money", this.money);
        this.config.save();
    }

    public void reload(){
        this.config.reload();
        this.money = config.getDouble("money");
    }
}
