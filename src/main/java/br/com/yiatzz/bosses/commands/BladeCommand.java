package br.com.yiatzz.bosses.commands;

import br.com.yiatzz.bosses.blade.BladeUtils;
import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BladeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.colorIt("&eComandos da Lâmina:"));
            sender.sendMessage(ColorUtil.colorIt("&f /lamina give <player> <dano>"));
            sender.sendMessage("");
            return false;
        }

        if (args[0].equalsIgnoreCase("give")) {

            if (args.length < 3) {
                sender.sendMessage(ColorUtil.colorIt("&cUtilize /lamina give <player> <dano>"));
                return false;
            }

            String userName = args[1];
            Player player = Bukkit.getPlayer(userName);
            if (player == null) {
                sender.sendMessage(ColorUtil.colorIt("&cUsuário inválido."));
                return false;
            }

            int damage;
            if (args[2].equalsIgnoreCase("hitkill")) {
                damage = Integer.MAX_VALUE;
            } else {
                Integer tryParse = Ints.tryParse(args[2]);
                if (tryParse == null) {
                    sender.sendMessage(ColorUtil.colorIt("&cDano inválido."));
                    return false;
                }

                damage = tryParse;
            }

            if (player.getInventory().firstEmpty() == -1) {
                sender.sendMessage(ColorUtil.colorIt("&cUsuário sem espaço no inventário."));
                return false;
            }

            ItemStack bladeItem = BladeUtils.asItemStack(damage);

            player.getInventory().addItem(bladeItem);

            sender.sendMessage(ColorUtil.colorIt("&aLâmina enviada com sucesso."));
            return true;
        }

        return true;
    }
}
