package org.kyonami.kyoeconomy.commands;

import org.bukkit.Bukkit;
import org.kyonami.kyoeconomy.money.MoneyInfo;
import org.kyonami.kyoeconomy.money.MoneyInfos;
import org.kyonami.kyoeconomy.utils.BukkitAPI;
import org.kyonami.kyoeconomy.utils.UserConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EconomyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(ChatColor.RED + "NO");
            return false;
        }

        if(args.length <= 0) {
            MoneyInfo info = MoneyInfos.getInstance().getMoneyInfo(player.getUniqueId());
            player.sendMessage("잔액 : " + info.getMoneyString() + "$.");
            return true;
        }

        switch (args[0])
        {
            case "send":
                if(args.length != 3) {
                    player.sendMessage("/economy send [받는 사람 닉네임] [금액]");
                    break;
                }
                sendMoney(player, args[1], Long.parseLong(args[2]));
                break;

            case "issuance":
                if(args.length != 2) {
                    player.sendMessage("/economy issuance [금액]");
                    break;
                }
                issuanceMoney(player, Long.parseLong(args[1]));
                break;
            case "give":
                if(args.length != 2) {
                    player.sendMessage("/economy give [금액]");
                }
                if(!Bukkit.getServer().getPlayer(player.getName()).isOp()) {
                    player.sendMessage("권한이 없습니다.");
                    break;
                }
                MoneyInfos.getInstance().addMoney(player.getUniqueId(), Long.parseLong(args[1]));
                player.sendMessage(Long.parseLong(args[1]) + "$가 지급되었습니다.");
                break;

            case "help":
            default:
                sender.sendMessage("send [받는 사람 이름] [금액]\n" +
                        "issuance [금액]\n" +
                        "price\n");
                break;
        }

        return true;
    }

    private void sendMoney(Player sender, String targetPlayerName, long money){
        MoneyInfo moneyInfo = MoneyInfos.getInstance().getMoneyInfo(sender.getUniqueId());

        long charge = money / 100 * UserConfig.getInstance().sendingChargeRatio;
        if(!moneyInfo.hasEnoughMoney(money + charge)) {
            sender.sendMessage("돈이 부족합니다. (수수료: " + charge + ")");
            return;
        }

        Player receiver = BukkitAPI.findOnlinePlayer(targetPlayerName);
        if(receiver != null) {     // 받는 사람이 온라인이면
            MoneyInfos.getInstance().addMoney(sender.getUniqueId(), -(money - charge));
            MoneyInfos.getInstance().addMoney(receiver.getUniqueId(), money);
            sender.sendMessage(receiver.getName() + "에게 " + money + "$ 를 송금합니다.");
            receiver.sendMessage(sender.getName() + "에게서 " + money + "$ 가 입금되었습니다.");
            return;
        }
        sender.sendMessage("받는 분의 이름이 잘못되었거나 오프라인 상태입니다.");
    }

    private void issuanceMoney(Player player, long money){
        if(money <= 0) {
            player.sendMessage("잘못된 금액을 입력하셨습니다.");
            return;
        }

        MoneyInfo moneyInfo = MoneyInfos.getInstance().getMoneyInfo(player.getUniqueId());
        if(!moneyInfo.hasEnoughMoney(money)) {
            player.sendMessage("돈이 부족합니다.");
            return;
        }

        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();    // 널일 수 없음
        itemMeta.setDisplayName("수표");
        itemMeta.setLore(Arrays.asList(money + "$"));
        itemStack.setItemMeta(itemMeta);

        moneyInfo.getPlayer().getInventory().addItem(itemStack);
        MoneyInfos.getInstance().addMoney(player.getUniqueId(), -money);

        player.sendMessage(+ money + "$수표가 발행되었습니다.\n 잔액이 " + MoneyInfos.getInstance().getMoneyInfo(player.getUniqueId()).getMoneyString() + "$ 남았습니다.");
    }
}
