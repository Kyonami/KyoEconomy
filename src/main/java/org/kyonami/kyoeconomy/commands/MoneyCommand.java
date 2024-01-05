package org.kyonami.kyoeconomy.commands;

import org.kyonami.kyoeconomy.money.MoneyInfo;
import org.kyonami.kyoeconomy.money.MoneyInfos;
import org.kyonami.kyoeconomy.utils.BukkitAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(ChatColor.RED + "You cant.");
            return false;
        }

        if(args.length <= 0)
        {
            MoneyInfo info = MoneyInfos.getInstance().getMoneyInfo(player);
            player.sendMessage("you have " + info.getMoneyString() + "$.");
            return true;
        }

        switch (args[0])
        {
            case "send":
                if(args.length != 3)
                {
                    player.sendMessage("/money send [name] [value]");
                    break;
                }
                sendMoney(player, args[1], Double.parseDouble(args[2]));
                break;

            case "issuance":
                if(args.length != 2)
                {
                    player.sendMessage("/money issuance [value]");
                    break;
                }
                issuanceMoney(player, Double.parseDouble(args[1]));
                break;

            case "price":
                if(args.length != 1)
                {
                    player.sendMessage("/money price");
                    break;
                }
                showProductList(player);
                break;

            case "exchange":
                if(args.length != 3)
                {
                    player.sendMessage("/money exchange [item name] [amount]");
                    break;
                }
                exchangeItem(player, args[1], args[2]);
                break;

            case "help":
            default:
                /*
                    send        [name] [value]
                    issuance    [name] [value]
                    price
                    exchange    [item name] [amount]
                 */
                sender.sendMessage("send [name] [value]\n" +
                        "issuance [value]\n" +
                        "price\n" +
                        "exchange [item name] [amount]\n");
                break;
        }

        return true;
    }

    private void sendMoney(Player sender, String targetPlayerName, double money){
        MoneyInfo moneyInfo = MoneyInfos.getInstance().getMoneyInfo(sender);

        if(!moneyInfo.compareMoney(money))
        {
            sender.sendMessage("Not enough money.");
            return;
        }

        Player receiver = BukkitAPI.findOnlinePlayer(targetPlayerName);
        if(receiver != null)    // 받는 사람이 온라인이면
        {
            MoneyInfos.getInstance().addMoney(sender, -money);
            MoneyInfos.getInstance().addMoney(receiver, money);
            sender.sendMessage("You sent " + money + "$ to " + receiver.getName() + ".");
            receiver.sendMessage("You received " + money + " from " + sender.getName() + ".");
            return;
        }

        OfflinePlayer offlineReceiver = BukkitAPI.findOfflinePlayer(targetPlayerName);
        if(offlineReceiver != null) {
            MoneyInfos.getInstance().addMoney(sender, -money);
            MoneyInfos.getInstance().addOfflinePlayerMoney(offlineReceiver.getUniqueId(), money);
            sender.sendMessage("You sent " + money + "$ to " + offlineReceiver.getName() + ".");
            return;
        }

        sender.sendMessage("There is no player named like that.");
    }

    private void issuanceMoney(Player player, double money){

        MoneyInfo moneyInfo = MoneyInfos.getInstance().getMoneyInfo(player);
        if(!moneyInfo.compareMoney(money))
        {
            player.sendMessage("Not enough money");
            return;
        }

        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();    // 널일 수 없음
        itemMeta.setDisplayName("Check");
        itemMeta.setLore(Arrays.asList(money + "$"));
        itemStack.setItemMeta(itemMeta);

        moneyInfo.getPlayer().getInventory().addItem(itemStack);
        MoneyInfos.getInstance().addMoney(player, -money);

        player.sendMessage("You've received" + money + "$ check.\nNow you have " + MoneyInfos.getInstance().getMoneyInfo(player).getMoneyString() + "$ left.");
    }

    private void showProductList(Player player){
        player.sendMessage(MoneyInfos.getInstance().getProductList());
    }
    private void exchangeItem(Player player, String itemString, String amountString){

        int amount;
        try{
            amount = Integer.parseInt(amountString);
            if(amount <= 0) {
                player.sendMessage("Amount value is not available.");
                return;
            }
        }
        catch(Exception e)
        {
            player.sendMessage("Amount value is not available.");
            return;
        }

        Material item;
        try{
            item = Material.valueOf(itemString);
        }
        catch(Exception e)
        {
            player.sendMessage("Item is not available.");
            return;
        }

        if(!MoneyInfos.getInstance().hasPrice(item))    // 플레이어가 이상한 값을 보냈을 때도 여기서 걸리므로 퉁침.
        {
            player.sendMessage("That item is not exist or has no price.");
            return;
        }

        double price = MoneyInfos.getInstance().getPrice(item);

        MoneyInfo moneyInfo = MoneyInfos.getInstance().getMoneyInfo(player);
        if(!moneyInfo.compareMoney(price * amount))
        {
            player.sendMessage("Not enough money");
            return;
        }
        ItemStack itemStack = new ItemStack(item, amount);
        moneyInfo.getPlayer().getInventory().addItem(itemStack);
        MoneyInfos.getInstance().addMoney(player, -(price * amount));

        player.sendMessage("you got " + amount + " " + itemStack.getItemMeta().getLocalizedName());
    }
}
