package org.kyonami.kyoeconomy.utils;

import org.bukkit.Bukkit;
import org.kyonami.kyoeconomy.KyoEconomy;
import org.kyonami.kyoeconomy.money.MoneyInfos;

import java.util.logging.Level;

public class UserConfig {
    private static UserConfig _instance = null;
    public static UserConfig getInstance() {
        if(_instance == null)
            _instance = new UserConfig();
        return _instance;
    }


    public int sendingChargeRatio;

    public UserConfig(){
         sendingChargeRatio = KyoEconomy.getInstance().getConfig().getInt("SENDING_CHARGE", -1);
    }
}
