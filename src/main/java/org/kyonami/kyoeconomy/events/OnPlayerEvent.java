package org.kyonami.kyoeconomy.events;

import org.kyonami.kyoeconomy.money.MoneyInfos;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnPlayerEvent implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        MoneyInfos.getInstance().addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        MoneyInfos.getInstance().removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event){
        Action action = event.getAction();

        switch (action) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                Player player = event.getPlayer();
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if(Material.AIR == mainHandItem.getType())
                    break;

                if(mainHandItem.hasItemMeta()) {
                    ItemMeta itemMeta = mainHandItem.getItemMeta();

                    if(itemMeta == null || !itemMeta.hasDisplayName())
                        break;

                    if (itemMeta.getDisplayName().equals("Check")) {
                        exchangeCheck(player, mainHandItem, itemMeta);
                        break;
                    }
                }

        }
    }

    // 수표는 하나씩 사용하고 아이템은 전부 사용하므로 함수 분리
    private void exchangeCheck(Player player, ItemStack mainHandItem, ItemMeta itemMeta){
        if(!itemMeta.hasLore())
            return;

        String lore = itemMeta.getLore().getFirst();
        if(!lore.contains("$"))
            return;

        lore = lore.replace("$", "");

        double money = Double.parseDouble(lore);
        mainHandItem.setAmount(mainHandItem.getAmount() - 1);
        MoneyInfos.getInstance().addMoney(player, money);
        player.sendMessage("You've received " + money + "$ check.\nNow you have " + MoneyInfos.getInstance().getMoneyInfo(player).getMoneyString() + "$ left.");
    }
}
