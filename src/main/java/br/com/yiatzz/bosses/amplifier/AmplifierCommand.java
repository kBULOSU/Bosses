package br.com.yiatzz.bosses.amplifier;

import br.com.yiatzz.bosses.misc.utils.ColorUtil;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AmplifierCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.colorIt("&eComandos do Amplificador:"));
            sender.sendMessage(ColorUtil.colorIt("&f /amplificador give <player> <dano>"));
            sender.sendMessage("");
            return false;
        }

        if (args[0].equalsIgnoreCase("give")) {

            if (args.length < 3) {
                sender.sendMessage(ColorUtil.colorIt("&cUtilize /amplificador give <player> <dano>"));
                return false;
            }

            String userName = args[1];
            Player player = Bukkit.getPlayer(userName);
            if (player == null) {
                sender.sendMessage(ColorUtil.colorIt("&cUsuário inválido."));
                return false;
            }

            Integer damage = Ints.tryParse(args[2]);
            if (damage == null) {
                sender.sendMessage(ColorUtil.colorIt("&cDano inválido."));
                return false;
            }

            if (player.getInventory().firstEmpty() == -1) {
                sender.sendMessage(ColorUtil.colorIt("&cUsuário sem espaço no inventário."));
                return false;
            }

            ItemStack amplifierItem = AmplifierUtil.asItemStack(damage, 1);

            player.getInventory().addItem(amplifierItem);

            sender.sendMessage(ColorUtil.colorIt("&aAmplificador enviada com sucesso."));
            return true;
        }

        return true;
    }
}