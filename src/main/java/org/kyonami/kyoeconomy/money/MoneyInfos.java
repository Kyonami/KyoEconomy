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
    public void addPlayer(UUID playerID){
        if(this.infoMap.containsKey(playerID))
            return;

        this.infoMap.put(playerID, new MoneyInfo(playerID));
    }

    public void removePlayer(UUID playerID){
        MoneyInfo moneyInfo = this.infoMap.remove(playerID);
        if (moneyInfo != null)
            moneyInfo.save();
    }

    // 플레이어가 온라인이 아니어도 보낼 수 있음
    public void addMoney(UUID playerUUID, long money){
        Player receiver = Bukkit.getPlayer(playerUUID);
        if(receiver != null)
        {
            MoneyInfo moneyInfo = getMoneyInfo(playerUUID);
            moneyInfo.setMoney(moneyInfo.getMoney() + money);
        }
        else
            addOfflinePlayerMoney(playerUUID, money);
    }

    public void addOfflinePlayerMoney(UUID playerUUID, long money){
        MoneyInfo moneyInfo = new MoneyInfo(playerUUID);
        moneyInfo.setMoney(moneyInfo.getMoney() + money);
        moneyInfo.save();
    }

    public MoneyInfo getMoneyInfo(UUID playerID){
        if(this.infoMap.get(playerID) == null)
            addPlayer(playerID);
        return this.infoMap.get(playerID);
    }
}