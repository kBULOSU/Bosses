package br.com.yiatzz.bosses.commands;

import br.com.yiatzz.bosses.object.Boss;
import br.com.yiatzz.bosses.inventories.holders.IndexHolder;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import br.com.yiatzz.bosses.registry.BossRegistry;
import com.google.common.primitives.Ints;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BossCommand implements CommandExecutor {

    private final BossRegistry bossRegistry;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtil.colorIt("&cSomente jogadores podem utilizar este comando."));
                return false;
            }

            ((Player) sender).openInventory(new IndexHolder().getInventory());

            return false;
        }

        if (args[0].equalsIgnoreCase("give")) {

            if (args.length < 4) {
                sender.sendMessage(ColorUtil.colorIt("&cUtilize /boss give <player> <boss> <quantidade>"));
                return false;
            }

            String userName = args[1];
            Player player = Bukkit.getPlayer(userName);
            if (player == null) {
                sender.sendMessage(ColorUtil.colorIt("&cUsuário inválido."));
                return false;
            }

            String bossId = args[2];
            Boss boss = bossRegistry.get(bossId);
            if (boss == null) {
                sender.sendMessage(ColorUtil.colorIt("&cBoss inválido."));
                return false;
            }

            String rawAmount = args[3];
            Integer amount = Ints.tryParse(rawAmount);
            if (amount == null) {
                sender.sendMessage(ColorUtil.colorIt("&cQuantidade inválida."));
                return false;
            }

            if (player.getInventory().firstEmpty() == -1) {
                sender.sendMessage(ColorUtil.colorIt("&cUsuário sem espaço no inventário."));
                return false;
            }

            player.getInventory().addItem(boss.asItemStack(amount));

            sender.sendMessage(ColorUtil.colorIt("&aBoss(es) enviados com sucesso."));

            return true;
        }

        return true;
    }
}
