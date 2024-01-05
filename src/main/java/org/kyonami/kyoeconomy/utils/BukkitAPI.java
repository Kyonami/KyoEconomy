package org.kyonami.kyoeconomy.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BukkitAPI {
    public static Player findOnlinePlayer(String playerName){
        for(Player player : Bukkit.getOnlinePlayers())
            if(player.getName().equals(playerName))
                return player;

        return null;
    }

    public static OfflinePlayer findOfflinePlayer(String playerName){
        for(OfflinePlayer player : Bukkit.getOfflinePlayers())
            if(player.getName().equals(playerName))
                return player;

        return null;
    }
}
