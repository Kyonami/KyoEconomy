package org.kyonami.kyoeconomy.money;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public class MoneyInfos {
    private static MoneyInfos _instance = null;
    public static MoneyInfos getInstance() {
        if(_instance == null)
            _instance = new MoneyInfos();

        return _instance;
    }

    private Map<UUID, MoneyInfo> infoMap;
    private MoneyInfos() {
        this.infoMap = new HashMap<>();
    }

    public void saveAll(){
        for(MoneyInfo info : this.infoMap.values()){
            info.save();
        }
    }
    public void addPlayer(Player player){
        if(this.infoMap.containsKey(player.getUniqueId()))
            return;

        this.infoMap.put(player.getUniqueId(), new MoneyInfo(player));
    }

    public void removePlayer(Player player){
        MoneyInfo moneyInfo = this.infoMap.remove(player.getUniqueId());
        if (moneyInfo != null)
            moneyInfo.save();
    }

    public void addMoney(Player player, double money){
        MoneyInfo moneyInfo = getMoneyInfo(player);
        moneyInfo.setMoney(moneyInfo.getMoney() + money);
    }

    public void addOfflinePlayerMoney(UUID playerUUID, double money){
        MoneyInfo moneyInfo = new MoneyInfo(playerUUID);
        moneyInfo.setMoney(moneyInfo.getMoney() + money);
        moneyInfo.save();
    }

    // 만약 player 에 맞는 moneyInfo 없으면 새로 추가.
    public MoneyInfo getMoneyInfo(Player player) {
        MoneyInfo info = this.infoMap.get(player.getUniqueId());
        if(info == null)
            addPlayer(player);

        return this.infoMap.get(player.getUniqueId());
    }
}